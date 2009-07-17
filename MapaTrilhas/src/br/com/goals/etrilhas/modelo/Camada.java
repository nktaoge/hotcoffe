package br.com.goals.etrilhas.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Camada extends Base implements Serializable{
	private static final long serialVersionUID = -6443776154995413804L;
	private String nome;
	private String descricao;
	private Integer ordem;
	/**
	 * pertence a 
	 */
	private Mapa mapa;
	private List<MapaItem> items = new ArrayList<MapaItem>();
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
		
	/**
	 * pertence ao mapa 
	 */
	public Mapa getMapa() {
		return mapa;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	}
	

	
	public List<MapaItem> getItems() {
		return items;
	}
	public void setItems(List<MapaItem> items){
		this.items = items;
	}
}
