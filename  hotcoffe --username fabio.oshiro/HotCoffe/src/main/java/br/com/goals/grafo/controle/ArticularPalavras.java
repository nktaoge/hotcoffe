package br.com.goals.grafo.controle;

import java.util.List;

import br.com.goals.grafo.modelo.Ponto;

public class ArticularPalavras {

	/**
	 * 
	 * @param listPontos
	 * @return string com o nome dos pontos
	 */
	public String responder(List<Ponto> listPontos) {
		String resposta = "";
		if(listPontos==null) return "";
		for(Ponto ponto : listPontos){
			if(ponto!=null){
				resposta +=ponto.getNome()+" ";
			}
		}
		return resposta;
	}

}
