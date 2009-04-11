package br.quickstart.casosdeuso.entidade;

public class Tecnico {
	String nome;
	String time;
	String inicio;
	public String getInicio() {
		return inicio;
	}
	public void setInicio(String inicio) {
		this.inicio = inicio;
	}
	public String getFim() {
		return fim;
	}
	public void setFim(String fim) {
		this.fim = fim;
	}
	
	String fim;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getTime() {
		return time;
	}
	public void setListTime(String time) {
		this.time = time;
	}
}
