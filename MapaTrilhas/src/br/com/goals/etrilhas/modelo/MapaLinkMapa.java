package br.com.goals.etrilhas.modelo;

/**
 * Link de um mapa para outro
 * @author fabio
 */
public class MapaLinkMapa extends Base{
	private static final long serialVersionUID = -8381999942073742047L;
	private String url;
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
