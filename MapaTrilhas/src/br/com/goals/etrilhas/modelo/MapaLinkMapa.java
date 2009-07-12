package br.com.goals.etrilhas.modelo;

/**
 * Link de um mapa para outro
 * @author fabio
 */
public class MapaLinkMapa extends MapaItem{
	private static final long serialVersionUID = -3896666377172911729L;
	private Mapa origem;
	private Mapa destino;
	public Mapa getOrigem() {
		return origem;
	}
	public void setOrigem(Mapa origem) {
		this.origem = origem;
	}
	public Mapa getDestino() {
		return destino;
	}
	public void setDestino(Mapa destino) {
		this.destino = destino;
	}
}
