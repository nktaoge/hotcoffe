package br.com.goals.etrilhas.modelo;

import java.util.ArrayList;
import java.util.List;

public class Galeria extends Base{
	private static final long serialVersionUID = 7789601976418515286L;
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
	private List<Foto> fotos = new ArrayList<Foto>();

	public List<Foto> getFotos() {
		return fotos;
	}

	public void setFotos(List<Foto> fotos) {
		this.fotos = fotos;
	}
}
