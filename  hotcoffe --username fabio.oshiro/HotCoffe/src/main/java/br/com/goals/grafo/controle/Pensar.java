package br.com.goals.grafo.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.grafo.CAL;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

public class Pensar {
	private CAL cal;
	private ExecutarVerbo executarVerbo;
	private PontoDao pontoDao = PontoDao.getInstance();
	public Pensar(CAL cal){
		this.cal = cal;
		executarVerbo = new ExecutarVerbo(this);		
	}
	
	public List<Ponto> pensar(List<Ponto> listPontos) {
		//verificar se é uma pergunta
		boolean perguntou = verificarPergunta(listPontos);
		if(perguntou){
			return responderPergunta(listPontos);
		}else{
			Ponto verbo = acharVerbo(listPontos);
			executarVerbo.executar(verbo,listPontos);
			return listPontos;
		}
	}
	private List<Ponto> responderPergunta(List<Ponto> listPontos) {
		System.out.println("Pensar.responderPergunta()");
		ArrayList<Ponto> resposta = new ArrayList<Ponto>(); 
		
		for(Ponto ponto:listPontos){
			System.out.println("-->" + ponto.getNome());
			if(ponto.equals(Conceitos.quem)){
				resposta = acharQuem(listPontos);
				return resposta;
			}
		}
		return resposta;
	}

	private ArrayList<Ponto> acharQuem(List<Ponto> listPontos) {
		System.out.println("Pensar.acharQuem(){");
		Ponto verbo = acharVerbo(listPontos);
		ArrayList<Ponto> semQuem = new ArrayList<Ponto>();
		semQuem.add(verbo);
		System.out.println("verbo:\n\t" + verbo.toString().replace("\n","\n\t"));
		for(Ponto ponto:listPontos){
			if(!ponto.equals(Conceitos.quem)
					&& !ponto.getNome().equals("?")
					){
				boolean inserido = false;
				//tenta achar um predicado
				for(Ponto pontoA : ponto.getLigacaoA()){
					if(pontoA.getDescricao()!=null && pontoA.getDescricao().equals(Conceitos.INSTANCIA_PREDICADO)){
						semQuem.add(pontoA);
						System.out.println("\t-->"+pontoA.toString().replace("\n","\n\t-->"));
						inserido = true;
						break;
					}
				}
				if(!inserido){
					//System.out.println("\t-->não é predicado:"+ponto.toString().replace("\n","\n\t-->"));
					//semQuem.add(ponto);
				}
			}
		}
		//
		ArrayList<Ponto> resposta = new ArrayList<Ponto>();
		
		List<Ponto> pontosA = pontoDao.acharPontosAComum(semQuem);
		System.out.println("\ttratando grupos:");
		for(Ponto pontoA:pontosA){
			System.out.println("\t"+pontoA.getPontoId() + " " + pontoA.getNome());
			//achar o item que nao está no grupo semQuem
			List<Ponto> ligacaoB = pontoDao.getLigacaoB(pontoA);
			for(Ponto pontoB:ligacaoB){
				if(!semQuem.contains(pontoB)){
					resposta.add(pontoB);
				}
			}
		}
		System.out.println("\trespostas:");
		for(Ponto pontoA:resposta){
			System.out.println("\t"+pontoA.getPontoId() + " " + pontoA.getNome());
		}
		System.out.println("}");
		return resposta;
	}

	/**
	 * 
	 * @param listPontos
	 * @return true caso seja uma pergunta
	 */
	private boolean verificarPergunta(List<Ponto> listPontos) {
		for(Ponto ponto:listPontos){
			if(ponto.getNome().equals("?")){
				return true;
			}
		}
		return false;
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
