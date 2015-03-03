package crawler.noticias.search;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import crawler.Utiles;
import crawler.db.MongoDB;
import crawler.noticias.Noticia;

public class NoticiasJornalESTADAO extends Noticia{

	private static final String URL_ESTADAO = "http://busca.estadao.com.br/?editoria[]=Economia&pagina=";
	private static int NUM_PAGINA = 1;
	private static final String CONSULTA = "&q=";
	
	private DBCollection mongoCollection = null;

	private static final Map<String, String> mesesDoAno;
	static {
		Map<String, String> aMap = new HashMap<String, String>();
		aMap.put("janeiro", "01");
		aMap.put("fevereiro", "02");
		aMap.put("março", "03");
		aMap.put("abril", "04");
		aMap.put("maio", "05");
		aMap.put("junho", "06");
		aMap.put("julho", "07");
		aMap.put("agosto", "08");
		aMap.put("setembro", "09");
		aMap.put("outubro", "10");
		aMap.put("novembro", "11");
		aMap.put("dezembro", "12");

		mesesDoAno = Collections.unmodifiableMap(aMap);
	}

	public NoticiasJornalESTADAO(){

	}

	public NoticiasJornalESTADAO(long timestamp, String subFonte,
			String titulo, String subTitulo, String conteudo, 
			String emissor, String url, long repercussao) {

		super(timestamp, subFonte, titulo, subTitulo, conteudo, emissor, url, repercussao);
	}

	public Map<String,String> getMesesDoAno(){
		return mesesDoAno;
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

	public void insereInformacao(String dataInicial, String dataFinal,
			String consulta) throws IOException {

		mongoCollection = MongoDB.getInstance();

		long unixTimesTampDataInicial = 0; 
		long unixTimesTampDataFinal = 0;

		unixTimesTampDataInicial = Utiles.dataToTimestamp(dataInicial, "0000");
		unixTimesTampDataFinal = Utiles.dataToTimestamp(dataFinal, "2359");

		String url = URL_ESTADAO+NUM_PAGINA+CONSULTA+consulta;
		Document pagina = obtemPagina(url);

		while(pagina == null){
			pagina = obtemPagina(url);
		}

		Elements noticiasEstadaoPagina = pagina.select(".listaultimas");
		String data = noticiasEstadaoPagina.select(".listaultimasdata").text();
		noticiasEstadaoPagina = noticiasEstadaoPagina.select("ul .ultimas");

		boolean limiteAlcancado =  verificaLimiteInformacao(data, noticiasEstadaoPagina, 
				unixTimesTampDataInicial, unixTimesTampDataFinal, consulta);

		while(!limiteAlcancado){
			NUM_PAGINA++;
			System.out.println("PAGINA: "+NUM_PAGINA);
			pagina = obtemPagina(URL_ESTADAO+NUM_PAGINA+CONSULTA+consulta);
			while(pagina == null){
				pagina = obtemPagina(URL_ESTADAO+NUM_PAGINA+CONSULTA+consulta);
			}
			noticiasEstadaoPagina = pagina.select(".listaultimas ");
			data = noticiasEstadaoPagina.select(".listaultimasdata").text();
			noticiasEstadaoPagina = noticiasEstadaoPagina.select("ul .ultimas");
			limiteAlcancado =  verificaLimiteInformacao(data,noticiasEstadaoPagina, 
					unixTimesTampDataInicial, unixTimesTampDataFinal, consulta);
		}
		//gravarArq.close();

	}

	private long timestampDoDia(String data){

		String timesTamp = padronizaFormatoDDMMYYYY(data);
		return Utiles.dataToTimestamp(timesTamp,"0000");

	}

	private String padronizaFormatoDDMMYYYY(String data){
		//tratamento especial para o mes de [de]zembro
		String diaMesAno [] = data.split(" ");

		String dia = diaMesAno[0].trim();
		String mes = diaMesAno[2].trim();
		String ano = diaMesAno[4].trim();

		mes = getMesesDoAno().get(mes);


		return dia+"/"+mes+"/"+ano;

	}

	public boolean verificaLimiteInformacao(String data, Elements dias, long unixTimesTampDataInicial,
			long unixTimesTampDataFinal, String consulta) throws IOException {

		long timesTampDia = timestampDoDia(data);

		if(!dias.isEmpty()){

			if(unixTimesTampDataFinal == Utiles.ZERO){

				for (Element dia : dias) {
					if(timesTampDia >= unixTimesTampDataInicial){
						Noticia noticia = criaInformacao(data,dia, consulta);
						if(noticia != null){
							mongoCollection.insert(converterToMap(noticia));
						}
					}else{
						return true;
					}
				}

			}else{

				for (Element dia : dias) {

					if((timesTampDia <= unixTimesTampDataFinal) && 
							(timesTampDia >= unixTimesTampDataInicial)){
						Noticia noticia = criaInformacao(data, dia, consulta);
						if(noticia != null){
							mongoCollection.insert(converterToMap(noticia));
						}
					}else if(timesTampDia < unixTimesTampDataInicial){
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
	public Noticia criaInformacao(String data, Element el, String consulta){

		String hora = el.select(".listainfo span").text();
		data = padronizaFormatoDDMMYYYY(data);
		long timestamp = Utiles.dataToTimestamp(data, hora.replace("h", ""));		

		String url = el.select(".listadesc a").attr("href");
		String titulo = el.select(".listadesc h3").text();
		System.out.println("\t -"+titulo);

		Document doc = obtemPagina(url);
		while(doc == null){
			doc = obtemPagina(url);
		}

		if(doc.baseUri().isEmpty()){
			return null;
		}
		
		String subTitulo = doc.select(".chapeu p").text();
		String emissor = el.select(".credito").text();
		String conteudo = doc.select(".noticiainterna p").text();
		
		if(conteudo.isEmpty()){
			conteudo = doc.select(".texto p").text();
		}
		
		conteudo = conteudo.replace("|", "");
		conteudo = conteudo.replace("\"", "");
		conteudo = conteudo.replace("\'", "");
		
		String guid = doc.select("#guid_noticia").attr("value");
		int repercussao = calculaRepercussao(url, guid);
		
		return new NoticiasJornalESTADAO(timestamp, "ESTADAO",
				titulo, subTitulo, conteudo, 
				emissor, url, repercussao);

	}

	public int calculaRepercussao(String url, String guid){
		
		
		final String comentarios = "http://economia.estadao.com.br/servicos/comentarios/contador/?guid[]="+guid;
		Document pagina = obtemPagina(comentarios);
		while(pagina == null){
			pagina = obtemPagina(comentarios);
		}
		System.out.println(pagina);
		final String tweeter = "https://cdn.api.twitter.com/1/urls/count.json?url="+url+"&callback=jQuery11100053468162895262794_1425342668803&_=1425342668804";
		System.out.println(obtemPagina(tweeter));
		final String facebook = "https://graph.facebook.com/fql?q=SELECT%20url,%20normalized_url,%20share_count,%20like_count,%20comment_count,%20total_count,commentsbox_count,%20comments_fbid,%20click_count%20FROM%20link_stat%20WHERE%20url=%27"+url+"%27&callback=jQuery11100053468162895262794_1425342668801&_=1425342668802";
		System.out.println(obtemPagina(facebook));
		final String linkedIn = "https://www.linkedin.com/countserv/count/share?format=jsonp&url="+url+"&callback=jQuery11100053468162895262794_1425342668805&_=1425342668806";
		System.out.println(obtemPagina(linkedIn));
		final String googleplus = "http://economia.estadao.com.br/estadao/sharrre.php?url="+url+"&type=googlePlus";
		System.out.println(obtemPagina(googleplus));
		return 0;
	}

	

	public static void main(String args[]) throws IOException{

		String searchDateStart= "01/05/2000";
		String searchDateFinish="01/03/2015";
		NoticiasJornalESTADAO n = new NoticiasJornalESTADAO();
		n.insereInformacao(searchDateStart, searchDateFinish, "economia");

	}

}
