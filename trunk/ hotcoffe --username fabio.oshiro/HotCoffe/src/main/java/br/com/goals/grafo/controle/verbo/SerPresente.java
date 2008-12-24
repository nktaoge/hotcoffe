package br.com.goals.grafo.controle.verbo;

import java.util.List;

import br.com.goals.grafo.controle.Conceitos;
import br.com.goals.grafo.modelo.Ponto;

public class SerPresente extends SuperVerbo{
	@Override
	public List<Ponto> executar(Ponto sujeito, Ponto predicado) {
		System.out.print("sujeito '" + sujeito.getNome()+"'");
		
		
		//System.out.println(" é '" + predicado.getNome()+"'");
		
		Ponto instanciaDeSerPresente = new Ponto("Instancia de 'é'");
		pontoDao.criar(instanciaDeSerPresente);
		pontoDao.ligar(instanciaDeSerPresente,Conceitos.verboSerPresente);
		pontoDao.ligar(instanciaDeSerPresente,sujeito);
		pontoDao.ligar(instanciaDeSerPresente,predicado);
		
		return null;
	}
}
