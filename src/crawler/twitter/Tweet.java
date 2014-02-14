package crawler.twitter;

import crawler.Informacao;


public class Tweet extends Informacao{

	private String idUsuario ;
	private String hrefUsuarioNoTwitter;
	private String nomeUtilizadoPeloUsario;
	private String imagem;
	private String arrobaIdentifTwitter;
	private String dataFormatada;
	
	public Tweet(String idUsuario, String hrefUsuarioNoTwitter, String nomeUtilizadoPeloUsario,
			String imagem, String arrobaIdentifTwitter, String dataFormatada,
			String referenciaDoTweet, String shortTimesTamp,String textoTweet){
		
		super(Long.valueOf(shortTimesTamp),textoTweet,referenciaDoTweet, "TWITTER");
		setIdUsuario(idUsuario);
		setHrefUsuarioNoTwitter(hrefUsuarioNoTwitter);
		setNomeUtilizadoPeloUsario(nomeUtilizadoPeloUsario);
		setImagem(imagem);
		setArrobaIdentifTwitter(arrobaIdentifTwitter);
		setDataFormatada(dataFormatada);		
		
	}
	public String getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getHrefUsuarioNoTwitter() {
		return hrefUsuarioNoTwitter;
	}
	
	public void setHrefUsuarioNoTwitter(String hrefUsuarioNoTwitter) {
		this.hrefUsuarioNoTwitter = hrefUsuarioNoTwitter;
	}
	
	public String getNomeUtilizadoPeloUsario() {
		return nomeUtilizadoPeloUsario;
	}
	
	public void setNomeUtilizadoPeloUsario(String nomeUtilizadoPeloUsario) {
		this.nomeUtilizadoPeloUsario = nomeUtilizadoPeloUsario;
	}
	
	public String getImagem() {
		return imagem;
	}
	
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	
	public String getArrobaIdentifTwitter() {
		return arrobaIdentifTwitter;
	}
	
	public void setArrobaIdentifTwitter(String arrobaIdentifTwitter) {
		this.arrobaIdentifTwitter = arrobaIdentifTwitter;
	}
	
	public String getDataFormatada() {
		return dataFormatada;
	}
	
	public void setDataFormatada(String momentoFormatado) {
		this.dataFormatada = momentoFormatado;
	}
	
	@Override
	public String toString() {
		return "Tweet [idUsuario=" + idUsuario + ", hrefUsuarioNoTwitter="
				+ hrefUsuarioNoTwitter + ", nomeUtilizadoPeloUsario="
				+ nomeUtilizadoPeloUsario + ", imagem=" + imagem
				+ ", arrobaIdentifTwitter=" + arrobaIdentifTwitter
				+ ", dataFormatada=" + dataFormatada + "]";
	}
	
}
