package br.com.goals.grafo.controle;

import java.util.List;

import br.com.goals.grafo.controle.verbo.SuperVerbo;
import br.com.goals.grafo.modelo.Ponto;

public class ExecutarVerbo {
	public void executar(Ponto pontoVerbo,List<Ponto> pontos){
		if(pontoVerbo.getClasse()!=null && !pontoVerbo.getClasse().equals("")){
			try{
				SuperVerbo superVerbo =(SuperVerbo)Class.forName("br.com.goals.grafo.controle.verbo."+pontoVerbo.getClasse()).newInstance();
				Ponto sujeito = acharSujeito(pontoVerbo,pontos);
				superVerbo.executar(sujeito,null);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/**
	 * Muitas vezes entre o artigo e o verbo
	 * @param verb
	 * @param pontos
	 * @return
	 */
	private Ponto acharSujeito(Ponto verb, List<Ponto> pontos){
		for(Ponto ponto:pontos){
			if(ponto.equals(Conceitos.verbo)){
				return ponto;
			}
		}
		return null;
	}
}
