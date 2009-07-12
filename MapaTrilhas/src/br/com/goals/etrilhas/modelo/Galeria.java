package br.com.goals.etrilhas.modelo;

import java.util.ArrayList;
import java.util.List;

public class Galeria extends MapaItem{
	private static final long serialVersionUID = -258494721636093119L;
	private List<Foto> fotos = new ArrayList<Foto>();

	public List<Foto> getFotos() {
		return fotos;
	}

	public void setFotos(List<Foto> fotos) {
		this.fotos = fotos;
	}
}
