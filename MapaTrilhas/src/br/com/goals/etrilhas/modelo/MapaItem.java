package br.com.goals.etrilhas.modelo;

import java.io.Serializable;

/**
 * Item que esta no mapa, um ponto.
 * @author Fabio Issamu Oshiro
 */
public class MapaItem extends Base implements Serializable{
	private static final long serialVersionUID = 6673407019262629113L;
	private Double x;
	private Double y;
	private String icone;
	private String nome;
	private Camada camada;
	private String tipo;
	private Integer ordem;
	private Object valor;
	public MapaItem(){
		super();
	}
	@Override
	protected String[] transients() {
		return new String[]{"camada"};
	}
	
	@Override
	public void setId(Long id) {
		super.setId(id);
	}
	
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Object getValor(){
		return valor;
	}
	public void setValor(Object valor) {
		this.valor = valor;
	}
	public Camada getCamada() {
		return camada;
	}
	public void setCamada(Camada camada) {
		this.camada = camada;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public String getIcone() {
		return icone;
	}
	public void setIcone(String icone) {
		this.icone = icone;
	}
}
