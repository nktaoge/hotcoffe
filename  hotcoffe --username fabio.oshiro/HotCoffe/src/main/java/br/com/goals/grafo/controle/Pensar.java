package br.com.goals.grafo.controle;

import java.util.List;

import br.com.goals.grafo.CAL;
import br.com.goals.grafo.modelo.Ponto;

public class Pensar {
	private CAL cal;
	private ExecutarVerbo executarVerbo;
	public Pensar(CAL cal){
		this.cal = cal;
		executarVerbo = new ExecutarVerbo(this);		
	}
	
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
	public Ponto acharVerbo(List<Ponto> listPontos){
		for(Ponto ponto:listPontos){
			List<Ponto> ligacaoA = ponto.getLigacaoA();
			for(Ponto pontoA:ligacaoA){
				if(pontoA.equals(Conceitos.verbo)){
					return ponto;
				}
				if(pontoA.getClasse()!=null){
					return pontoA;
				}
			}
		}
		System.out.println("Sem verbo em:");
		for(Ponto ponto:listPontos){
			System.out.println(ponto.toString().replace("\n","\n\t"));
			System.out.println();
		}
		return null;
	}

	public CAL getCal() {
		return cal;
	}

	public void setCal(CAL cal) {
		this.cal = cal;
	}
}
