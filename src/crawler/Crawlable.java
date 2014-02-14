package crawler;

import java.util.List;

import org.jsoup.select.Elements;

public interface Crawlable {
	
	public List<Informacao> getInformacao(String dataInicial, String dataFinal, String consulta);
	public boolean verificaLimiteInformacao(Elements informacoesHTML, List<Informacao> informacoes, 
			long unixTimesTampDataInicial, long unixTimesTampDataFinal);
}
