package br.com.goals.grafo.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.debug.Sysou;
import br.com.goals.grafo.CAL;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

public class Pensar {
	private CAL cal;
	private ExecutarVerbo executarVerbo;
	private PontoDao pontoDao = PontoDao.getInstance();
	private Sysou sysou = new Sysou(this,2);
	public Pensar(CAL cal){
		this.cal = cal;
		executarVerbo = new ExecutarVerbo(this);		
	}
	
	public List<Ponto> pensar(List<Ponto> listPontos) {
		sysou.onEnterFunction(1,"pensar");
		List<Ponto> res = null;
		//verificar se é uma pergunta
		boolean perguntou = verificarPergunta(listPontos);
		if(perguntou){
			res=responderPergunta(listPontos);
		}else{
			Ponto verbo = acharVerbo(listPontos);
			executarVerbo.executar(verbo,listPontos);
			res = listPontos;
		}
		sysou.onExitFunction(1,"pensar");
		return res;
	}
	private List<Ponto> responderPergunta(List<Ponto> listPontos) {
		sysou.onEnterFunction(1,"responderPergunta");
		ArrayList<Ponto> resposta = new ArrayList<Ponto>(); 
		
		for(Ponto ponto:listPontos){
			sysou.println(1,"-->" + ponto.getNome());
			if(ponto.equals(Conceitos.quem)){
				resposta = acharQuem(listPontos);
				sysou.onExitFunction(1,"responderPergunta");
				return resposta;
			}
		}
		sysou.onExitFunction(1,"responderPergunta");
		return resposta;
	}

	private ArrayList<Ponto> acharQuem(List<Ponto> listPontos) {
		sysou.onEnterFunction(1,"acharQuem");
		Ponto verbo = acharVerbo(listPontos);
		ArrayList<Ponto> semQuem = new ArrayList<Ponto>();
		semQuem.add(verbo);
		sysou.println(1,"verbo:\n" + verbo.toString());
		for(Ponto ponto:listPontos){
			if(!ponto.equals(Conceitos.quem)
					&& !ponto.getNome().equals("?")
					){
				boolean inserido = false;
				//tenta achar um predicado
				for(Ponto pontoA : ponto.getLigacaoA()){
					if(pontoA.getDescricao()!=null && pontoA.getDescricao().equals(Conceitos.INSTANCIA_PREDICADO)){
						semQuem.add(pontoA);
						sysou.println(1,"-->"+pontoA.toString());
						inserido = true;
						break;
					}
				}
				//se nao for inserido
				if(!inserido){
					//verificar a
				}
				if(!inserido){
					sysou.println(1,"-->não é predicado:"+ponto.toString());
					//semQuem.add(ponto);
				}
			}
		}
		//
		ArrayList<Ponto> resposta = new ArrayList<Ponto>();
		
		List<Ponto> pontosA = pontoDao.acharPontosAComum(semQuem);
		sysou.println(1,"tratando grupos:");
		for(Ponto pontoA:pontosA){
			sysou.println(1,pontoA.getPontoId() + " " + pontoA.getNome());
			//achar o item que nao está no grupo semQuem
			List<Ponto> ligacaoB = pontoDao.getLigacaoB(pontoA);
			for(Ponto pontoB:ligacaoB){
				if(!semQuem.contains(pontoB)){
					resposta.add(pontoB);
				}
			}
		}
		sysou.println(1,"respostas:");
		for(Ponto pontoA:resposta){
			sysou.println(1,pontoA.getPontoId() + " " + pontoA.getNome());
		}
		sysou.onExitFunction(1,"acharQuem");
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
		sysou.onEnterFunction(1,"acharVerbo");
		for(Ponto ponto:listPontos){
			List<Ponto> ligacaoA = ponto.getLigacaoA();
			for(Ponto pontoA:ligacaoA){
				if(pontoA.equals(Conceitos.verbo)){
					sysou.onExitFunction(1,"acharVerbo");
					return ponto;
				}
				if(pontoA.getClasse()!=null){
					sysou.onExitFunction(1,"acharVerbo");
					return pontoA;
				}
			}
		}
		sysou.println(1,"Sem verbo em:");
		for(Ponto ponto:listPontos){
			sysou.println(1,ponto);
			sysou.println(1,"");
		}
		sysou.onExitFunction(1,"acharVerbo");
		return null;
	}

	public CAL getCal() {
		return cal;
	}

	public void setCal(CAL cal) {
		this.cal = cal;
	}
}
