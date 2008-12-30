package br.com.goals.grafo.controle.verbo;

import java.util.List;

import br.com.goals.grafo.controle.Conceitos;
import br.com.goals.grafo.modelo.Ponto;

public class SerPresente extends SuperVerbo{
	@Override
	public List<Ponto> executar(Ponto sujeito, Ponto predicado) {
		sysou.onEnterFunction(1,"executar");
		sysou.print(1,"sujeito '" + sujeito.getPontoId()+"'");
		sysou.print(1," é '" + predicado.getPontoId()+"'");		
		pontoDao.ligar(sujeito,predicado,Conceitos.verboSerPresente);
		sysou.onExitFunction(1,"executar");
		return null;
	}
}
