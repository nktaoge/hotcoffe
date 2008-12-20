package br.com.goals.grafo.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.grafo.modelo.Duvida;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

public class Entender {
	private PontoDao pontoDao = PontoDao.getInstance();
	/**
	 * Tenta resolver o significado de cada coisa
	 * @param listListPontos
	 * @return lista de pontos "significados"
	 */
	public List<Ponto> entender(List<Ponto> listPontos) throws Duvida {
		ArrayList<Ponto> duvidas = new ArrayList<Ponto>();
		// pontos com significados unicos
		ArrayList<Ponto> significados = new ArrayList<Ponto>();
		for(Ponto ponto:listPontos){
			//carregar o primeiro level de conexao A
			List<Ponto> ligacaoA = pontoDao.getLigacaoA(ponto);
			ponto.setLigacaoA(ligacaoA);

			//se for um conceito basico, já basta :-)
			if(Conceitos.ehConceitoBasico(ponto)){
				significados.add(ponto);
				continue;
			}
			
			//nao eh um conceito basico
			if(ponto.getLigacaoA().size()==1){
				//TODO DFS?
				significados.add(ponto);
			}else if(ponto.getLigacaoA().size()==0){
				duvidas.add(ponto);
			}else{
				//ambiguidade?
				duvidas.add(ponto);
			}
			
		}
		if(duvidas.size()>0){
			System.out.println("duvida " + duvidas.toArray());
			throw new Duvida(duvidas);
		}
		return significados;
	}
	
}
