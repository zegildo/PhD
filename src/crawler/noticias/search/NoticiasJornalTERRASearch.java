package crawler.noticias.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawler.Crawlable;
import crawler.DataToTimesTamp;
import crawler.Informacao;
import crawler.Utiles;
import crawler.noticias.Noticia;

public class NoticiasJornalTERRASearch implements Crawlable {

	private final String URL_TERRA = "http://economia.terra.com.br/ultimas/?&vgnpage=";
	private int NUM_PAGINA = 1;
	private final String PORTAL_TERRA = "PORTAL_TERRA";

	private static final Map<String, String> mesesDoAno;
    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("Janeiro", "01");
        aMap.put("Fevereiro", "02");
        aMap.put("Março", "03");
        aMap.put("Abril", "04");
        aMap.put("Maio", "05");
        aMap.put("Junho", "06");
        aMap.put("Julho", "07");
        aMap.put("Agosto", "08");
        aMap.put("Setembro", "09");
        aMap.put("Outubro", "10");
        aMap.put("Novembro", "11");
        aMap.put("Dezembro", "12");

        mesesDoAno = Collections.unmodifiableMap(aMap);
    }
    
	@Override
	public List<Informacao> getInformacao(String dataInicial, String dataFinal,
			String consulta) {
		
		long unixTimesTampDataInicial = 0; 
		long unixTimesTampDataFinal = 0;
		
		try{
			unixTimesTampDataInicial = Utiles.dataToTimestamp(dataInicial, false);
			unixTimesTampDataFinal = Utiles.dataToTimestamp(dataFinal, true);
		}catch(DataToTimesTamp e){
			System.out.println(e.getMessage());
		}
		
		
		Document pagina = obtemPagina(NUM_PAGINA);
		List<Informacao> noticias = new ArrayList<Informacao>();
		
		//Obtem todos os dias da primeira pagina.
		Elements noticiasTerraPrimeiraPagina = pagina.select(".list");
		boolean limiteAlcancado =  verificaLimiteInformacao(noticiasTerraPrimeiraPagina, noticias, unixTimesTampDataInicial, unixTimesTampDataFinal);

		while(!limiteAlcancado){
			NUM_PAGINA++;
			pagina = obtemPagina(NUM_PAGINA);
			noticiasTerraPrimeiraPagina = pagina.select(".list");
			limiteAlcancado =  verificaLimiteInformacao(noticiasTerraPrimeiraPagina, noticias, unixTimesTampDataInicial, unixTimesTampDataFinal);
		}
		
		return noticias;
	}
	
	private String padronizaFormatoDDMMYYYY(String data){

		String diaMesAno [] = data.split("de");
		String dia = diaMesAno[0];
		String mes = diaMesAno[1];
		String ano = diaMesAno[2];
		
		mes = mesesDoAno.get(mes);
		
		return dia+mes+ano;

	}
	
	@Override
	public boolean verificaLimiteInformacao(Elements dias,
			List<Informacao> informacao, long unixTimesTampDataInicial,
			long unixTimesTampDataFinal) {

		if(!dias.isEmpty()){
			
			if(unixTimesTampDataFinal == Utiles.ZERO){
				
				for (Element dia : dias) {
					List<Informacao> noticiasDoDia = criaInformacao(dia);
					/*Se a ultima noticia valor da lista(o mais antiga) ainda for menor
					 * que o valor que busco, então toda a lista deve ser adicionada.
					 */
					Informacao primeiraDoDia = noticiasDoDia.get(noticiasDoDia.size()-1);
					if(primeiraDoDia.getTimestamp() >= unixTimesTampDataInicial){
						informacao.addAll(noticiasDoDia);
					}else{
						return true;
					}
				}
				
			}else{
				
				for (Element dia : dias) {
					List<Informacao> noticiasDoDia = criaInformacao(dia);
					Informacao primeiraDoDia = noticiasDoDia.get(noticiasDoDia.size()-1);
					
					if((primeiraDoDia.getTimestamp() <= unixTimesTampDataFinal) && 
							(primeiraDoDia.getTimestamp() >= unixTimesTampDataInicial)){
						informacao.addAll(noticiasDoDia);
					}else if(primeiraDoDia.getTimestamp() < unixTimesTampDataInicial){
						return true;
					}	
					
				}
				return false;
			}
		}
		
		return true;
	}
	
	

	public List<Informacao> criaInformacao(Element el){
		
			String data = el.select("h2").text();
			String timesTamp = padronizaFormatoDDMMYYYY(data);
			Elements notics = el.select("a");
			List<Informacao> noticiasDoDia = new ArrayList<Informacao>();
			for (Element notic : notics) {
				String url = notic.attr("href");
				String hora = notic.select("em").text();
				hora=hora.replaceFirst("h", "");
				timesTamp=timesTamp+hora;
				String tituloResumo = notic.select("strong").text();

				Connection.Response noticiaIntegra;
				Document integra = null;
				try {
					noticiaIntegra = Jsoup.connect(url)
							.method(Method.GET)
							.execute();
					integra = noticiaIntegra.parse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String tituloCompleto = integra.select(".ttl-main").text();
				String texto = integra.select(".text").text();
				
				noticiasDoDia.add(new Noticia(tituloCompleto, texto, timesTamp, url, PORTAL_TERRA, tituloResumo));

			}
			return noticiasDoDia;


	}

	private Document obtemPagina(int pagina){
		
		Connection.Response res;
		Document paginaInicial = null;

		try {
			res = Jsoup.connect(URL_TERRA)
					.method(Method.GET)
					.execute();
			paginaInicial = res.parse();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return paginaInicial;
	}

	public static void main(String args[]) throws IOException{

		String searchDateStart= "01/01/2014";
		String searchDateFinish="02/01/2014";
		
		NoticiasJornalTERRASearch terra = new NoticiasJornalTERRASearch();
		List<Informacao> noticias = terra.getInformacao(searchDateStart, searchDateFinish, "");
		
		Noticia noticia = (Noticia) noticias.get(0);
		System.out.println(noticia);

	}

	


}
