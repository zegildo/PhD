package crawler.noticias;

import crawler.Informacoes;


public class Noticia extends Informacoes {

	private String noticiaNaIntegra;
	private String tituloResumo;
	
	
	public Noticia(String titulo, String noticiaNaIntegra, long timestamp, String URL, 
			String fonteJornalista, String tituloResumo,  String assunto, String detalhe){
	
		super(timestamp, titulo, URL, "JORNAL", fonteJornalista, assunto, detalhe);
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
				+ ", tituloResumo=" + tituloResumo + ", timestamp=" + data
				+ ", conteudo=" + conteudo + ", URL=" + URL + ", fonte="
				+ fonte + "]";
	}
	
}
