package crawler.noticias;

import crawler.Informacao;


public class Noticia extends Informacao {

	private String noticiaNaIntegra;
	private String tituloResumo;
	
	public Noticia(String titulo, String noticiaNaIntegra, long timestamp, String URL, 
			String fonteJornalista, String tituloResumo){
	
		super(timestamp, titulo, URL, fonteJornalista);
		setNoticiaNaIntegra(noticiaNaIntegra);
		setTituloResumo(tituloResumo);
	}

	public String getTituloResumo() {
		return tituloResumo;
	}
	
	public void setTituloResumo(String tituloResumo) {
		this.tituloResumo = tituloResumo;
	}
	
	public String getNoticiaNaIntegra() {
		return noticiaNaIntegra;
	}
	
	public void setNoticiaNaIntegra(String noticiaNaIntegra) {
		this.noticiaNaIntegra = noticiaNaIntegra;
	}
	
	@Override
	public String toString() {
		return "Noticia [noticiaNaIntegra=" + noticiaNaIntegra
				+ ", tituloResumo=" + tituloResumo + ", timestamp=" + timestamp
				+ ", conteudo=" + conteudo + ", URL=" + URL + ", fonte="
				+ fonte + "]";
	}
	
}
