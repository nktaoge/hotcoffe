package br.com.goals.grafo.controle;

import java.util.List;

import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

public class Pensar {
	private PontoDao pontoDao = PontoDao.getInstance();
	private ExecutarVerbo executarVerbo = new ExecutarVerbo();
	public List<Ponto> pensar(List<Ponto> listPontos) {
		Ponto verbo = acharVerbo(listPontos);
		executarVerbo.executar(verbo,listPontos);
		//for(Ponto ponto:listPontos){
		//	executarVerbo.executar(ponto,listPontos);
		//}
		return listPontos;
	}
	/**
	 * O verbo muitas vezes é uma função que modifica um objeto
	 * @param listPontos
	 * @return ponto do verbo ligado a uma classe ou não
	 */
	private Ponto acharVerbo(List<Ponto> listPontos){
		for(Ponto ponto:listPontos){
			List<Ponto> ligacaoA = ponto.getLigacaoA();
			for(Ponto pontoA:ligacaoA){
				if(pontoA.equals(Conceitos.verbo)){
					return ponto;
				}
			}
		}
		System.out.println("Sem verbo em:");
		for(Ponto ponto:listPontos){
			System.out.println(ponto.toString().replace("\n","\t\n"));
		}
		return null;
	}
}
