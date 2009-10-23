package br.com.goals.jpa4google.test;

import java.util.ArrayList;
import java.util.List;

public class Objeto{
	private String nome;
	private List<ObjetoFilho> lista = new ArrayList<ObjetoFilho>();
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the lista
	 */
	public List<ObjetoFilho> getLista() {
		return lista;
	}
	/**
	 * @param lista the lista to set
	 */
	public void setLista(List<ObjetoFilho> lista) {
		this.lista = lista;
	}
}