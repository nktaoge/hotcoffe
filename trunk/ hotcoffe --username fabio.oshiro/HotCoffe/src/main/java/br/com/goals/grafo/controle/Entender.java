package br.com.goals.grafo.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.grafo.modelo.Duvida;
import br.com.goals.grafo.modelo.Ponto;

public class Entender {

	/**
	 * Tenta resolver o significado de cada coisa
	 * @param listListPontos
	 * @return
	 */
	public List<Ponto> entender(List<List<Ponto>> listListPontos) throws Duvida {
		ArrayList<Ponto> duvidas = new ArrayList<Ponto>();
		ArrayList<Ponto> significados = new ArrayList<Ponto>();
		for(List<Ponto> listPontos:listListPontos){
			Ponto pontoUnico = null;
			for(Ponto ponto:listPontos){
				if(ponto.getPontoId()==null){
					duvidas.add(ponto);
				}else{
					pontoUnico = ponto;
				}
			}
			significados.add(pontoUnico);
		}
		if(duvidas.size()>0){
			System.out.println("duvida " + duvidas.toArray());
			throw new Duvida(duvidas);
		}
		return significados;
	}

}
