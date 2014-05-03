package crawler.twitter.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.caelum.vraptor.Resource;

import com.google.gson.Gson;

import crawler.Crawlable;
import crawler.Informacoes;
import crawler.Utiles;
import crawler.twitter.TweetsJson;
import dao.InformacoesDAO;


@Resource
public class TwitterSearch implements Crawlable {
	
	private InformacoesDAO twitterDAO;
	
	public InformacoesDAO getTwitterDAO() {
		return twitterDAO;
	}


	public void setTwitterDAO(InformacoesDAO twitterDAO) {
		this.twitterDAO = twitterDAO;
	}


	public void insereInformacao(String dataInicial, String dataFinal, String sobreTipo, String consulta, InformacoesDAO infDAO){

		setTwitterDAO(infDAO);
		long unixTimesTampDataInicial = 0; 
		long unixTimesTampDataFinal = 0;

		unixTimesTampDataInicial = Utiles.dataToTimestamp(dataInicial, "0000");
		unixTimesTampDataFinal = Utiles.dataToTimestamp(dataFinal, "2359");

		consulta = Utiles.encondeConsulta(consulta);
		Document paginaInicial = obtemPaginaInicial(consulta);
		String dataScrollCursor = obtemdataScrollCursor(paginaInicial);
		Elements twts = obtemElementosTweets(paginaInicial);
		System.out.println("Qtd twitters:"+twts.size());

		boolean limiteAlcancado =  verificaLimiteInformacao(twts, unixTimesTampDataInicial, unixTimesTampDataFinal, sobreTipo, consulta);

		Gson gson = new Gson();

		while(!limiteAlcancado){

			BufferedReader in = obtemPaginasConsecutivas(consulta, dataScrollCursor);
			TweetsJson twets = gson.fromJson(in, TweetsJson.class);
			Element e = paginaInicial.createElement("novaRequisicao");
			e.html(twets.getItems_html());
			twts = obtemElementosTweets(e);
			System.out.println("Qtd novos tweets:"+twts.size());
			dataScrollCursor = twets.getScroll_cursor();
			limiteAlcancado =  verificaLimiteInformacao(twts, unixTimesTampDataInicial, unixTimesTampDataFinal, sobreTipo, consulta);

		}
	}


	private BufferedReader obtemPaginasConsecutivas(String consulta, String dataScrollCursor){

		URL tweetPage = null;
		try {
			tweetPage = new URL("https://twitter.com/i/search/timeline?q="+consulta+"&src=typd&f=realtime&include_available_features=1&include_entities=1&scroll_cursor="+dataScrollCursor);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}

		BufferedReader in = null;

		try {
			in = new BufferedReader(
					new InputStreamReader(tweetPage.openStream()));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return in;

	}

	public Document obtemPaginaInicial(String consulta){

		Connection.Response res;
		Document paginaInicial = null;

		try {
			res = Jsoup.connect("https://twitter.com/search?q="+consulta+"&src=typd&f=realtime")
					.method(Method.GET)
					.execute();
			paginaInicial = res.parse();
		} catch (IOException e) {

			System.out.println(e.getMessage());
		}

		return paginaInicial;

	}

	private String obtemdataScrollCursor(Document paginaInicial){
		Element element = paginaInicial.select(".stream-container ").first();
		String dataScrollCursor = element.attr("data-scroll-cursor");
		return dataScrollCursor;

	}

	private Elements obtemElementosTweets(Element paginaInicial){

		return paginaInicial.select(".content");
	}

	private int getRetweets(String id){

		URL ajaxPage = null;
		final int QTD_LETRAS_ANTES_DO_NUMERO = 12;
		String retw = "Retweetado ";
		int retweet = 0;

		String link = "https://twitter.com/i/expanded/batch/"+id+"?facepile_max=1&include[]=social_proof&page_context=search&section_context=universal_all";

		try{
			ajaxPage = new URL(link);
			BufferedReader in = null;

			in = new BufferedReader(
					new InputStreamReader(ajaxPage.openStream()));
			String inf = in.readLine();
			int inicio = inf.lastIndexOf(retw);
			if(inicio>0){
				retw = inf.substring(inicio, inicio+QTD_LETRAS_ANTES_DO_NUMERO);
				retw = retw.split(" ")[1];
				retweet = Integer.parseInt(retw);
			}

		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return retweet;
	} 


	public Informacoes criaInformacao(Element element,String tipo, String consulta){
		Element itemHeader = element.select(".stream-item-header").first();
		Element itemTime = element.select(".time a").first();

		String idUsuario = itemHeader.select("a").attr("data-user-id");
		String referenciaUsuario = itemHeader.select("a").attr("href");
		String nomeUtilizadoPeloUsario = itemHeader.select("strong").text();

		String imagem = itemHeader.select("img").attr("src");
		String citacaoTwitter = "@"+itemHeader.select("span b").text();

		String momentoFormatado = itemTime.attr("title");
		String referenciaTweet = itemTime.attr("href");
		String shortTimesTamp = itemTime.select("span").attr("data-time");

		String textoTweet = element.select("p").text();

		String retweet = element.select(".tweet-timestamp").attr("href");
		int status = retweet.lastIndexOf("/");
		retweet = retweet.substring(status+1, retweet.length());
		int repercussao = getRetweets(retweet);
		
		Informacoes t = new Informacoes (Long.valueOf(shortTimesTamp), textoTweet, referenciaTweet, 
				"MÍDIAS SOCIAIS", "TWITTER",  tipo,  consulta,  citacaoTwitter, imagem, repercussao);
		
		getTwitterDAO().inserir(t);
		return t;
	}

	public boolean verificaLimiteInformacao(Elements twts,
			long unixTimesTampDataInicial, long unixTimesTampDataFinal, String sobreTipo, String consulta ){

		Informacoes tw = null;
		if(!twts.isEmpty()){

			if(unixTimesTampDataFinal == Utiles.ZERO){
				for (Element el : twts) {
					tw = criaInformacao(el, sobreTipo, consulta);
					if(tw.getTimestamp() < unixTimesTampDataInicial ){
						return true;
					}
				}
				return false;

			}else{

				for (Element el : twts) {
					tw = criaInformacao(el,sobreTipo, consulta);
					if((tw.getTimestamp() <= unixTimesTampDataFinal) && 
							(tw.getTimestamp() >= unixTimesTampDataInicial)){
						return false;
					}else if(tw.getTimestamp() < unixTimesTampDataInicial){
						return true;
					}	
				}
				return false;
			} 
		}
		return true;

	}

}
