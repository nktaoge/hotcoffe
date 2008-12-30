package br.com.goals.grafo.modelo;

import java.util.List;

public class DuvidaException extends Exception{
	private static final long serialVersionUID = 1L;
	private List<Ponto> pontos;
	public DuvidaException(List<Ponto> pontos){
		this.pontos = pontos;
	}
	public List<Ponto> getPontos() {
		return pontos;
	}
}
