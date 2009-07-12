package br.com.goals.etrilhas.modelo;

import java.io.Serializable;

/**
 * Item que esta no mapa
 * @author Fabio Issamu Oshiro
 */
public class MapaItem extends Base implements Serializable{
	private static final long serialVersionUID = 6673407019262629113L;
	private Integer x;
	private Integer y;
	private String icone;
	private String descricao;
	private String nome;
	private Camada camada;
	
	public MapaItem(){
		super();
	}
	@Override
	protected String[] transients() {
		return new String[]{"camada"};
	}
	public Camada getCamada() {
		return camada;
	}
	public void setCamada(Camada camada) {
		this.camada = camada;
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
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public String getIcone() {
		return icone;
	}
	public void setIcone(String icone) {
		this.icone = icone;
	}
}
