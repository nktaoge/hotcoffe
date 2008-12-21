package br.com.goals.grafo.controle.verbo;

import java.util.List;

import br.com.goals.grafo.modelo.Ponto;

public class SerPresente extends SuperVerbo{
	@Override
	public List<Ponto> executar(Ponto sujeito, Ponto predicado) {
		System.out.print("sujeito '" + sujeito.getNome()+"'");
		System.out.println(" é '" + predicado.getNome()+"'");
		pontoDao.ligar(sujeito,predicado);
		return null;
	}
}
