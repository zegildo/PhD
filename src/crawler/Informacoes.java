package crawler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Informacoes {

	@Id
	public String url;
	public long data;
	
	@Column(columnDefinition="TEXT")
	public String conteudo;
	public String fonte;
	public String subFonte;
	public String sobreTipo;
	public String sobre;
	public String emissor;
	public String imagem;
	public int repercussao;
	
	public Informacoes(long timestamp, String conteudo, String URL, String fonte,
			String subFonte, String sobreTipo, String sobre, String emissor,
			String imagem,int repercussao){
		
		setTimestamp(timestamp);
		setConteudo(conteudo);
		setURL(URL);
		setFonte(fonte);
		setSubFonte(subFonte);
		setSobreTipo(sobreTipo);
		setSobre(sobre);
		setEmissor(emissor);
		setImagem(imagem);
		setRepercussao(repercussao);
		
	}
	public String getEmissor() {
		return emissor;
	}
	public void setEmissor(String emissor) {
		this.emissor = emissor;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	public int getRepercussao() {
		return repercussao;
	}
	public void setRepercussao(int repercussao) {
		this.repercussao = repercussao;
	}
	public String getSubFonte() {
		return subFonte;
	}

	public void setSubFonte(String subFonte) {
		this.subFonte = subFonte;
	}

	public String getSobreTipo() {
		return sobreTipo;
	}

	public void setSobreTipo(String sobreTipo) {
		this.sobreTipo = sobreTipo;
	}

	public String getSobre() {
		return sobre;
	}

	public void setSobre(String sobre) {
		this.sobre = sobre;
	}
	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	
	public long getTimestamp() {
		return data;
	}
	public void setTimestamp(long timestamp) {
		this.data = timestamp;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public String getURL() {
		return url;
	}
	public void setURL(String uRL) {
		url = uRL;
	}
	
	
}
