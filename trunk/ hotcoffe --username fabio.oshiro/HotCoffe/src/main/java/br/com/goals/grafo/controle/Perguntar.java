package br.com.goals.grafo.controle;

import java.util.List;

import br.com.goals.grafo.modelo.Ponto;

public class Perguntar {

	public String perguntar(List<Ponto> pontos) {
		String res = "O que é " + pontos.get(0).getNome() + "?";
		return res;
	}

}
