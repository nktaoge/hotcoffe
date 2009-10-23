package br.com.goals.jpa4google.test;

import javax.persistence.Id;

public class ObjetoFilho{
	@Id
	private Long id;
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	private String nome;

	public ObjetoFilho(String s){
		nome = s;
	}
	public ObjetoFilho(){
		
	}
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
}