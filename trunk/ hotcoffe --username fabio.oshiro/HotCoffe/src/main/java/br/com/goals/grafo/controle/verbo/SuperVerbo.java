package br.com.goals.grafo.controle.verbo;

import java.util.List;

import br.com.goals.grafo.modelo.Ponto;

public abstract class SuperVerbo {
	public abstract List<Ponto> executar(Ponto sujeito,Ponto predicado);
}
