package crawler;

public class Informacao {

	public long timestamp;
	public String conteudo;
	public String URL;
	public String fonte;
	
	
	public Informacao(long timestamp, String conteudo, String URL, String fonte){
		
		setTimestamp(timestamp);
		setConteudo(conteudo);
		setURL(URL);
		setFonte(fonte);
	}
	
	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	
	
}
