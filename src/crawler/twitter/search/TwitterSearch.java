package crawler.twitter.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import crawler.Crawlable;
import crawler.Informacao;
import crawler.Utiles;
import crawler.twitter.Tweet;
import crawler.twitter.TweetsJson;



public class TwitterSearch implements Crawlable {

	public List<Informacao> getInformacao(String dataInicial, String dataFinal, String consulta){

		long unixTimesTampDataInicial = 0; 
		long unixTimesTampDataFinal = 0;


		unixTimesTampDataInicial = Utiles.dataToTimestamp(dataInicial, "0000");
		unixTimesTampDataFinal = Utiles.dataToTimestamp(dataFinal, "2359");



		consulta = Utiles.encondeConsulta(consulta);
		Document paginaInicial = obtemPaginaInicial(consulta);
		String dataScrollCursor = obtemdataScrollCursor(paginaInicial);
		Elements twts = obtemElementosTweets(paginaInicial);
		System.out.println("Qtd twitters:"+twts.size());
		List<Informacao> tweets = new ArrayList<Informacao>();

		boolean limiteAlcancado =  verificaLimiteInformacao(twts,tweets, unixTimesTampDataInicial, unixTimesTampDataFinal);

		Gson gson = new Gson();

		while(!limiteAlcancado){

			BufferedReader in = obtemPaginasConsecutivas(consulta, dataScrollCursor);
			TweetsJson twets = gson.fromJson(in, TweetsJson.class);
			Element e = paginaInicial.createElement("novaRequisicao");
			e.html(twets.getItems_html());
			twts = obtemElementosTweets(e);
			System.out.println("Qtd novos tweets:"+twts.size());
			dataScrollCursor = twets.getScroll_cursor();
			limiteAlcancado =  verificaLimiteInformacao(twts,tweets, unixTimesTampDataInicial, unixTimesTampDataFinal);

		}

		return tweets;

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

	public Informacao criaInformacao(Element element){
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

		return new Tweet(idUsuario,referenciaUsuario,nomeUtilizadoPeloUsario,
				imagem,citacaoTwitter,momentoFormatado,referenciaTweet,
				shortTimesTamp,textoTweet);
	}

	public boolean verificaLimiteInformacao(Elements twts, List<Informacao> tweets, 
			long unixTimesTampDataInicial, long unixTimesTampDataFinal){

		Informacao tw = null;
		if(!twts.isEmpty()){

			if(unixTimesTampDataFinal == Utiles.ZERO){
				for (Element el : twts) {
					tw = criaInformacao(el);
					if(tw.getTimestamp() >= unixTimesTampDataInicial ){
						tweets.add(tw);
					}else{
						return true;
					}
				}
				return false;

			}else{

				for (Element el : twts) {
					tw = criaInformacao(el);
					if((tw.getTimestamp() <= unixTimesTampDataFinal) && 
							(tw.getTimestamp() >= unixTimesTampDataInicial)){
						tweets.add(tw);
					}else if(tw.getTimestamp() < unixTimesTampDataInicial){
						return true;
					}	
				}
				return false;
			} 
		}
		return true;

	}

	public static List<Tweet> obterTweetsStream(){
		//		Element element = paginaInicial.select(".stream-container ").first();
		//
		//		String dataScrollCursor = element.attr("data-scroll-cursor");
		//		String dataRefreshCursor = element.attr("data-refresh-cursor");
		//		String teste = "https://twitter.com/i/search/timeline?q=abcdef&src=typd&f=realtime&composed_count=0&include_available_features=1&include_entities=1&include_new_items_bar=true&interval=300000&latent_count=13&refresh_cursor=TWEET-410079109881282560-410118733638549504";

		return null;
	}


	public static void main(String args[]){

		String searchDateStart = "10/12/2013";
		String searchDateFinish = "";
		String consulta =  "petrobrás";

		TwitterSearch search = new TwitterSearch();

		List<Informacao> tweets = search.getInformacao(searchDateStart,searchDateFinish, consulta);
		System.out.println(tweets.size());
		//System.out.println(tweets);	

		Tweet primeiroTweet = (Tweet) tweets.get(0);

		Tweet ultimoTweet = (Tweet) tweets.get(tweets.size()-1);

		System.out.println(primeiroTweet);
		System.out.println(ultimoTweet);

	}

}
