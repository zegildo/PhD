package crawler.noticias;

import crawler.Informacao;

public class Noticia extends Informacao {

	private String noticiaNaIntegra;
	private String tituloResumo;
	
	public Noticia(String titulo, String noticiaNaIntegra, String timestamp, String URL, 
			String fonteJornalista, String tituloResumo){
	
		super(Long.valueOf(timestamp), titulo, URL, fonteJornalista);
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
	
}
