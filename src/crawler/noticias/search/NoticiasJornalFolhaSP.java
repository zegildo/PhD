package crawler.noticias.search;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawler.Utiles;
import crawler.noticias.Noticia;

public class NoticiasJornalFolhaSP extends Noticia {

	private static final String URL_FOLHASP = "http://search.folha.com.br/search?";
	private static int NUM_PAGINA = 49151;
	private BufferedWriter gravarArq = null;

	public NoticiasJornalFolhaSP(){}

	public NoticiasJornalFolhaSP(long timestamp, String subFonte,
			String titulo, String subTitulo, String conteudo, 
			String emissor, String url, long repercussao) {

		super(timestamp, subFonte, titulo, subTitulo, conteudo, emissor, url, repercussao);
	}

	public Document obtemPagina(String url){

		Connection.Response res;
		Document paginaInicial = null;

		try {
			res = Jsoup.connect(url)
					.method(Method.GET)
					.execute();
			paginaInicial = res.parse();

			//Alguns links estao vazios e nao podem ser encontrados
		} catch (HttpStatusException e) {

			return new Document("");

		} catch (IOException e) {

			return null;
		} 

		return paginaInicial;
	}

	public void insereInformacao(String dataInicial, String dataFinal,
			String consulta) throws IOException {

		gravarArq = new BufferedWriter(new FileWriter("FOLHADESAOPAULO.txt"));

		long unixTimesTampDataInicial = 0; 
		long unixTimesTampDataFinal = 0;

		unixTimesTampDataInicial = Utiles.dataToTimestamp(dataInicial, "0000");
		unixTimesTampDataFinal = Utiles.dataToTimestamp(dataFinal, "2359");

		consulta = "q="+consulta;
		String complemento = "&site=online/dinheiro";
		dataInicial = "&sd="+dataInicial;
		dataFinal = "&ed="+dataFinal;
		String paginaInicial = "&sr="+NUM_PAGINA;

		String url = URL_FOLHASP+consulta+complemento+dataInicial+dataFinal+paginaInicial;
		Document pagina = obtemPagina(url);

		while(pagina == null){
			pagina = obtemPagina(url);
		}

		Elements noticiasFolhaSPPagina = pagina.select(".search-results-list li");
		System.out.println("Noticias de 1 - "+noticiasFolhaSPPagina.size());
		NUM_PAGINA += noticiasFolhaSPPagina.size();

		boolean limiteAlcancado =  verificaLimiteInformacao(noticiasFolhaSPPagina, 
				unixTimesTampDataInicial, unixTimesTampDataFinal, consulta);

		while(!limiteAlcancado){
			paginaInicial = "&sr="+NUM_PAGINA;

			pagina = obtemPagina(URL_FOLHASP+consulta+complemento+dataInicial+dataFinal+paginaInicial);

			while(pagina == null){
				pagina = obtemPagina(URL_FOLHASP+consulta+complemento+dataInicial+dataFinal+paginaInicial);
			}

			noticiasFolhaSPPagina = pagina.select(".search-results-list li");
			long valorAntigo = NUM_PAGINA;
			NUM_PAGINA += noticiasFolhaSPPagina.size();
			System.out.println("NOTICIAS DE : "+valorAntigo+" - "+NUM_PAGINA);

			limiteAlcancado =  verificaLimiteInformacao(noticiasFolhaSPPagina, 
					unixTimesTampDataInicial, unixTimesTampDataFinal, consulta);


		}
		gravarArq.close();

	}

	public boolean verificaLimiteInformacao(Elements noticias, long unixTimesTampDataInicial,
			long unixTimesTampDataFinal, String consulta) throws IOException {

		if(!noticias.isEmpty()){

			if(unixTimesTampDataFinal == Utiles.ZERO){

				for (Element noticia : noticias) {

					Noticia noticiaAtual = criaInformacao(noticia);
					if(noticiaAtual != null){
						long timesTampDia = noticiaAtual.getTimestamp();
						if(timesTampDia >= unixTimesTampDataInicial){
							gravarArq.write(noticiaAtual+"\n");
						}else{
							return true;
						}
					}

				}

			}else{

				for (Element noticia : noticias) {

					Noticia noticiaAtual = criaInformacao(noticia);

					if(noticiaAtual != null){
						long timesTampDia = noticiaAtual.getTimestamp();

						if((timesTampDia <= unixTimesTampDataFinal) && 
								(timesTampDia >= unixTimesTampDataInicial)){
							gravarArq.write(noticiaAtual+"\n");

						}else if(timesTampDia < unixTimesTampDataInicial){
							return true;
						}	
					}


				}
				return false;
			}
		}

		return true;
	}

	public Noticia criaInformacao(Element el){

		String url = el.select("a").attr("href");
		if (url == null || url.length() == 0){
	           System.out.println(el);
		}else{
			Document doc = obtemPagina(url);
			while(doc == null){
				doc = obtemPagina(url);
			}
			System.out.println(doc);
			String tempo = doc.select("time").text();
			if(tempo.isEmpty()){
				return null;
			}else{
				String data = tempo.split(" ")[0];
				String hora = tempo.split(" ")[1];
				long timestamp = Utiles.dataToTimestamp(data, hora.replace("h", ""));		

				String titulo = doc.select("article header h1").text();
				System.out.println("\t -"+titulo);
				String emissor = doc.select(".author p").text();
				String repercussao = "-1";
				String conteudo = doc.select("article .content p").text();

				return new NoticiasJornalESTADAO(timestamp, "FOLHASP",
						titulo, "", conteudo, 
						emissor, url, Long.valueOf(repercussao));
			}
		}
		return null;
		
	}



	public static void main(String args[]) throws IOException{

		String searchDateStart= "01/01/2000";
		String searchDateFinish="31/12/2014";
		NoticiasJornalFolhaSP n = new NoticiasJornalFolhaSP();
		/*
		 * Na Folha de São Paulo o nome do cardeno é MERCADO ao invés de ECONOMIA
		 * como é normalmente conhecido. 
		 */
		n.insereInformacao(searchDateStart, searchDateFinish, "mercado");

	}



}
