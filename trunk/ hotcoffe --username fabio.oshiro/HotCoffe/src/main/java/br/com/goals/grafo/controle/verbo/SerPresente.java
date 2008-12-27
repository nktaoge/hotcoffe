package br.com.goals.grafo.controle.verbo;

import java.util.List;

import br.com.goals.grafo.controle.Conceitos;
import br.com.goals.grafo.modelo.Ponto;

public class SerPresente extends SuperVerbo{
	@Override
	public List<Ponto> executar(Ponto sujeito, Ponto predicado) {
		sysou.onEnterFunction(1,"executar");
		sysou.println(1,"sujeito '" + sujeito.getNome()+"'");
		//System.out.println(" é '" + predicado.getNome()+"'");		
		pontoDao.ligar(sujeito,predicado,Conceitos.verboSerPresente);
		sysou.onExitFunction(1,"executar");
		return null;
	}
}
