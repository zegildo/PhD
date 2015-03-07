package crawler.noticias.search;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import crawler.Utiles;
import crawler.db.MongoDB;
import crawler.noticias.Noticia;

public class NoticiasJornalG1 extends Noticia {


	private static final String URL_ESTADAO = "http://g1.globo.com/dynamo/plantao/economia/";
	private static int NUM_PAGINA = 1;

	private DBCollection mongoCollection = null;

	public NoticiasJornalG1() {}

	public NoticiasJornalG1(long timestamp, String subFonte,
			String titulo, String subTitulo, String conteudo, 
			String emissor, String url, String repercussao) {

		super(timestamp, subFonte, titulo, subTitulo, conteudo, emissor, url, repercussao);
	}

	public Document obtemPagina(String url){

		Connection.Response res;
		Document paginaInicial = null;

		try {

			res = Jsoup.connect(url).method(Method.GET).execute();
			paginaInicial = res.parse();

		} catch (HttpStatusException e) {

			return null;

		} catch (IOException e) {

			return null;
		} 

		return paginaInicial;
	}

	public Document obtemPaginaIgnoringType(String url){
		Document countPage = null;
		try {
			countPage = Jsoup.connect(url).ignoreContentType(true).get();

		} catch (IOException e) {
			return null;
		}

		return countPage;

	}

	public void insereInformacao(String dataInicial, String dataFinal) throws IOException {

		mongoCollection = MongoDB.getInstance();

		long unixTimesTampDataInicial = 0; 
		long unixTimesTampDataFinal = 0;

		unixTimesTampDataInicial = Utiles.dataToTimestamp(dataInicial, "0000");
		unixTimesTampDataFinal = Utiles.dataToTimestamp(dataFinal, "2359");

		String url = URL_ESTADAO+NUM_PAGINA+".json";
		Document pagina = obtemPaginaIgnoringType(url);

		while(pagina == null){
			pagina = obtemPagina(url);
		}

		JSONArray noticiasG1Pagina = getAttr(pagina.select("body").text(),"conteudos");

		boolean limiteAlcancado =  verificaLimiteInformacao(noticiasG1Pagina, 
				unixTimesTampDataInicial, unixTimesTampDataFinal);

		while(!limiteAlcancado){
			NUM_PAGINA++;
			System.out.println("PAGINA: "+NUM_PAGINA);
			pagina = obtemPagina(URL_ESTADAO+NUM_PAGINA+".json");
			while(pagina == null){
				pagina = obtemPagina(URL_ESTADAO+NUM_PAGINA+".json");
			}
			noticiasG1Pagina = getAttr(pagina.select("body").text(),"conteudos");
			limiteAlcancado =  verificaLimiteInformacao(noticiasG1Pagina, 
					unixTimesTampDataInicial, unixTimesTampDataFinal);
		}

	}

	public JSONArray getAttr(String json, String atributo){

		JSONParser parser = new JSONParser();
		Object obj = null;

		try {
			obj = parser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = (JSONObject) obj;
		Iterator iterator = jsonObject.keySet().iterator(); 

		while(iterator.hasNext()){
			String key = (String) iterator.next();
			if(key.equals(atributo)){
				return (JSONArray) jsonObject.get(key);
			}
		}
		return null;
	}

	public boolean verificaLimiteInformacao(JSONArray noticias, long unixTimesTampDataInicial,
			long unixTimesTampDataFinal) throws IOException {

		if(!noticias.isEmpty()){

			if(unixTimesTampDataFinal == Utiles.ZERO){

				for (Object notic : noticias.toArray()) {
					Noticia noticia = criaInformacao((JSONObject) notic);
					if(noticia.getTimestamp() >= unixTimesTampDataInicial){
						if(noticia != null){
							mongoCollection.insert(converterToMap(noticia));
						}
					}else{
						return true;
					}
				}

			}else{

				for (Object notic : noticias.toArray()) {

					Noticia noticia = criaInformacao((JSONObject) notic);
					if((noticia.getTimestamp() <= unixTimesTampDataFinal) && 
							(noticia.getTimestamp() >= unixTimesTampDataInicial)){
						if(noticia != null){
							mongoCollection.insert(converterToMap(noticia));
						}
					}else if(noticia.getTimestamp() < unixTimesTampDataInicial){
						return true;
					}	

				}
				return false;
			}
		}

		return true;
	}

	public DBObject converterToMap(Noticia noticia) {   
		//timestamp, subFonte, titulo, subTitulo, conteudo, emissor, url, repercussao
		DBObject news = new BasicDBObject();  
		news.put("timestamp", noticia.getTimestamp());  
		news.put("subFonte", noticia.getSubFonte());  
		news.put("titulo", noticia.getTitulo());  
		news.put("subTitulo", noticia.getSubTitulo());  
		news.put("conteudo", noticia.getConteudo());  
		news.put("emissor", noticia.getEmissor());  
		news.put("url", noticia.getUrl());  
		news.put("repercussao", noticia.getRepercussao());  

		return news;    
	}
	public Noticia criaInformacao(JSONObject data){

		String url = data.get("permalink").toString();
		String titulo = data.get("titulo").toString();
		String editoria = data.get("editoria_principal").toString();

		String subTitulo = "";
		
		if(data.get("subtitulo")!= null){
			subTitulo = data.get("subtitulo").toString();
		}

		System.out.println("\t -"+titulo);

		Document doc = obtemPagina(url);
		while(doc == null){
			doc = obtemPagina(url);
		}

		if(doc.baseUri().isEmpty()){
			return null;
		}

		String momento = doc.select(".materia-cabecalho .published").text();
		String dataHora[] = momento.split(" ");
		long timestamp = Utiles.dataToTimestamp(dataHora[0], dataHora[1].replace("h", ""));	

		String emissor = doc.select(".fn").text() +" "+doc.select(".locality").text();

		String conteudo = doc.select(".entry-content p").text();

		conteudo = conteudo.replace("|", "");
		conteudo = conteudo.replace("\"", "");
		conteudo = conteudo.replace("\'", "");

		String id = doc.select("input[name=item_id]").attr("value");
		
		if(!("economia".equals(editoria))){
			editoria = editoria.toLowerCase();
			editoria = editoria.replace(" ", "-");
			id="@@"+editoria+"/"+id;
		}
		
		String id2 = doc.select("#input-link-ferramentas").attr("value");

		String repercussao = calculaRepercussao(url, id, id2.replace("/", "@@"), URLEncoder.encode(titulo.trim()));

		return new NoticiasJornalESTADAO(timestamp, "G1",
				titulo, subTitulo, conteudo, 
				emissor, url, repercussao);

	}

	public String calculaRepercussao(String url, String id, String id2, String titulo){

		final String comentariosPage = "http://comentarios.globo.com/comentarios/@@jornalismo@@g1@@economia"+id+"/"+url.replace("/", "@@")+"/"+id2+"/"+titulo+"/numero";
		System.out.println(comentariosPage);
		int comentarios = getCount(comentariosPage, "numeroDeComentarios");

		final String tweeterPage = "https://cdn.api.twitter.com/1/urls/count.json?url="+url+"&callback=jQuery11100053468162895262794_1425342668803&_=1425342668804";
		int tweeter = getCount(tweeterPage, "count");

		final String facebookPage = "https://graph.facebook.com/fql?q=SELECT%20url,%20normalized_url,%20share_count,%20like_count,%20comment_count,%20total_count,commentsbox_count,%20comments_fbid,%20click_count%20FROM%20link_stat%20WHERE%20url=%27"+url+"%27&callback=jQuery11100053468162895262794_1425342668801&_=1425342668802";
		int facebook = getCount(facebookPage, "total_count");

		final String linkedInPage = "https://www.linkedin.com/countserv/count/share?format=jsonp&url="+url+"&callback=jQuery11100053468162895262794_1425342668805&_=1425342668806";
		int linkedIn = getCount(linkedInPage, "count");

		//final String googleplusPage = "http://economia.estadao.com.br/estadao/sharrre.php?url="+url+"&type=googlePlus";		
		//int googlePlus = getCount(googleplusPage, "count");

		int total = comentarios+tweeter+facebook+linkedIn;
		return "c:"+comentarios+",t:"+tweeter+",f:"+facebook+",l:"+linkedIn+",total:"+total;
	}

	public int getCount(String url, String atributo){

		Document pagina = obtemPaginaIgnoringType(url);
		while(pagina == null){
			pagina = obtemPaginaIgnoringType(url);
		}
		String json = pagina.select("body").text();

		if(json.contains("(")){
			json = json.substring(json.indexOf("(")+1, json.indexOf(")"));
		}

		int count  = 0;
		JSONParser parser = new JSONParser();
		KeyFinder finder = new KeyFinder();
		finder.setMatchKey(atributo);
		try{
			while(!finder.isEnd()){
				parser.parse(json, finder, true);
				if(finder.isFound()){
					finder.setFound(false);
					System.out.println(finder.getValue());
					count = ((Long)finder.getValue()).intValue();
					return count;
				}
			}           
		}
		catch(ParseException pe){
			pe.printStackTrace();
		}
		return count;
	}



	public static void main(String args[]) throws IOException{

		String searchDateStart= "01/01/2000";
		String searchDateFinish="01/03/2015";
		NoticiasJornalG1 n = new NoticiasJornalG1();
		n.insereInformacao(searchDateStart, searchDateFinish);

	}

}
