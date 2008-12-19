package br.com.goals.grafo.controle.verbo;

import java.util.List;

import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

public class Cumprimentar extends SuperVerbo{
	PontoDao pontoDao = PontoDao.getInstance();
	@Override
	public List<Ponto> executar(Ponto sujeito, Ponto predicado) {
		return pontoDao.acharClasse(Cumprimentar.class.getName());
	}

}
