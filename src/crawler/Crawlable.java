package crawler;

import org.jsoup.select.Elements;

import dao.InformacoesDAO;

public interface Crawlable {
	
	public void insereInformacao(String dataInicial, String dataFinal, String consulta, String tipo, InformacoesDAO twitterDAO);
	public boolean verificaLimiteInformacao(Elements informacoesHTML, 
			long unixTimesTampDataInicial, long unixTimesTampDataFinal,String consulta, String tipo);
}
