package br.com.goals.etrilhas.modelo;

/**
 * Link de um mapa para outro
 * @author fabio
 */
public class MapaLinkMapa extends Base{
	private static final long serialVersionUID = -8381999942073742047L;
	private String url;
	private Integer x;
	private Integer y;
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
	/**
	 * @return the x
	 */
	public Integer getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(Integer x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public Integer getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(Integer y) {
		this.y = y;
	}
}
