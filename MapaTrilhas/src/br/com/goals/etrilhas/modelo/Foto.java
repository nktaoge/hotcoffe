package br.com.goals.etrilhas.modelo;

public class Foto extends Base{

	private static final long serialVersionUID = 1208424388838652916L;

	private String txtDescricao;
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
	private String urlRelativaJpg;

	
	public String getUrlRelativaJpg() {
		return urlRelativaJpg;
	}

	public void setUrlRelativaJpg(String urlRelativaJpg) {
		this.urlRelativaJpg = urlRelativaJpg;
	}

	
}
