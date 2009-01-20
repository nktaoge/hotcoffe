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
	private Sysou sysou = new Sysou(this,0);
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
				break;
			}
		}
		sysou.onExitFunction(1,"responderPergunta");
		return resposta;
	}

	private ArrayList<Ponto> acharQuem(List<Ponto> listPontos) {
		sysou.onEnterFunction(1,"acharQuem");
		
			
		Ponto pDado = null;
		ArrayList<Ponto> resposta = new ArrayList<Ponto>();
		//retirar o quem
		listPontos.remove(Conceitos.quem);
		//encontrar o verbo
		Ponto verbo = acharVerbo(listPontos);
		terminarLoop:
		//remover o verbo
		for(Ponto ponto:listPontos){
			List<Ponto> siginificados =ponto.getLigacao(Conceitos.significa); 
			for(Ponto sig:siginificados){
				if(sig.equals(verbo)){
					listPontos.remove(ponto);
					break terminarLoop;
				}
			}			
		}
		listPontos.remove(verbo);
		//remover o ponto de interrogacao
		listPontos.remove(Conceitos.ponto_interrogacao);
		
		if(listPontos.size()==1){
			Ponto pTemp = listPontos.get(0);
			if(pTemp.getLigacaoA().size()>0){
				//TODO escolher o melhor significado
				pDado = pTemp.getLigacaoA().get(0);
			}else{
				pDado = pTemp;
			}
		}
		sysou.println(1,"verbo = " + verbo);
		sysou.println(1,"listPontos.size() = " + listPontos.size());
		for(Ponto ponto:listPontos){
			sysou.println(1,ponto);
		}
		List<Ponto> rA = pontoDao.getLigacaoA(pDado, verbo);
		List<Ponto> rB = pontoDao.getLigacaoB(pDado, verbo);
		resposta.addAll(rA);
		resposta.addAll(rB);
		sysou.println(1,"resposta.size() = " + resposta.size());
		for(Ponto ponto:resposta){
			sysou.println(1,ponto);
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
			if(ponto.getNome()!=null && ponto.getNome().equals("?")){
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
