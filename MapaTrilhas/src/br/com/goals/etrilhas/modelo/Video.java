package br.com.goals.etrilhas.modelo;

public class Video extends Base{
	private static final long serialVersionUID = -8460141478677514943L;
	private String urlRelativaFlv;
	private String htmlDescricao;
	private String codTxtYoutube;
	public String getTxtCodYoutube() {
		return codTxtYoutube;
	}
	public void setTxtCodYoutube(String codTxtYoutube) {
		this.codTxtYoutube = codTxtYoutube;
	}
	/**
	 * @return the txtDescricao
	 */
	public String getHtmlDescricao() {
		return htmlDescricao;
	}

	/**
	 * @param txtDescricao the txtDescricao to set
	 */
	public void setHtmlDescricao(String txtDescricao) {
		this.htmlDescricao = txtDescricao;
	}
	public String getUrlRelativaFlv() {
		return urlRelativaFlv;
	}

	public void setUrlRelativaFlv(String urlRelativaFlv) {
		this.urlRelativaFlv = urlRelativaFlv;
	}
}
