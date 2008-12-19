package br.com.goals.grafo.modelo;

import java.util.Date;

public class Ponto {
	private Long pontoId = null;
	private String nome;
	private String classe;
	private String descricao;
	private Date dataHora;
	public Ponto(){
		dataHora = new Date();
	}
	public Ponto(String string) {
		this();
		this.descricao = string;
	}
	public Long getPontoId() {
		return pontoId;
	}
	public void setPontoId(Long pontoId) {
		this.pontoId = pontoId;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public Date getDataHora() {
		return dataHora;
	}
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
