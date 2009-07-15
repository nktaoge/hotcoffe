package br.com.goals.etrilhas.modelo;

public class Video extends Base{
	private static final long serialVersionUID = -8460141478677514943L;
	private String urlRelativaFlv;
	private String txtDescricao;
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
	public String getTxtDescricao() {
		return txtDescricao;
	}

	/**
	 * @param txtDescricao the txtDescricao to set
	 */
	public void setTxtDescricao(String txtDescricao) {
		this.txtDescricao = txtDescricao;
	}
	public String getUrlRelativaFlv() {
		return urlRelativaFlv;
	}

	public void setUrlRelativaFlv(String urlRelativaFlv) {
		this.urlRelativaFlv = urlRelativaFlv;
	}
}
