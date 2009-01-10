package br.com.goals.grafo.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.grafo.CAL;
import br.com.goals.grafo.modelo.DuvidaException;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;
/**
 * Tentar colocar o conceito de expressoes<br>
 * Achar grupos dos maiores para os menores.
 * @author Fabio Issamu Oshiro
 *
 */
public class Entender {
	private PontoDao pontoDao = PontoDao.getInstance();
	private CAL cal;
	public Entender(CAL cal) {
		this.cal = cal;
	}
	private List<Ponto> tryAll(List<Ponto> listPontos){
		ArrayList<Ponto> significados = new ArrayList<Ponto>();
		List<Ponto> temp = pontoDao.acharGrupo(listPontos,Conceitos.grupo);
		if(temp!=null && temp.size()>0){
			return temp;
		}
		int t=listPontos.size();
		int ini;
		boolean achou;
		//loop de ini até t
		for(ini=0;ini<t;ini++){
			achou = false;
			//de t até ini
			for(int j=t;j>ini;j--){
				List<Ponto> listGrupo = sublist(listPontos,ini,j);
				temp = pontoDao.acharGrupo(listGrupo,Conceitos.grupo);
				if(temp!=null && temp.size()>0){
					significados.add(temp.get(0));
					achou = true;
					ini=--j;
					break;
				}
			}
			if(!achou){
				significados.add(listPontos.get(ini));
			}
		}
		return significados;
	}
	
	/**
	 * for(int i=ini;i<fim;i++)
	 * @param original
	 * @param ini
	 * @param fim
	 * @return nova lista
	 */
	private List<Ponto> sublist(List<Ponto> original,int ini,int fim){
		List<Ponto> listGrupo = new ArrayList<Ponto>();
		for(int i=ini;i<fim;i++){
			listGrupo.add(original.get(i));
		}
		return listGrupo;
	}
	/**
	 * Tenta resolver o significado de cada coisa
	 * @param listListPontos
	 * @return lista de pontos "significados"
	 */
	public List<Ponto> entender(List<Ponto> listPontos) throws DuvidaException {
		//TODO testar!!
		listPontos = tryAll(listPontos);
		ArrayList<Ponto> duvidas = new ArrayList<Ponto>();
		// pontos com significados unicos
		ArrayList<Ponto> significados = new ArrayList<Ponto>();
		for(Ponto ponto:listPontos){
			//carregar o primeiro level de conexao A
			List<Ponto> ligacaoA = pontoDao.getLigacaoA(ponto,Conceitos.significa);
			ponto.setLigacaoA(ligacaoA);
			significados.add(ponto);
			
			//se for um conceito basico, já basta :-)
			if(Conceitos.ehConceitoBasico(ponto)){
				continue;
			}
			//nao eh um conceito basico
			if(ponto.getLigacaoA().size()==1){
				//TODO DFS?				
			}else if(ponto.getLigacaoA().size()==0){
				duvidas.add(ponto);
			}else{
				//TODO ambiguidade?
				duvidas.add(ponto);
			}
			
		}
		
		if(duvidas.size()>0){
			Ponto verbo = cal.getPensar().acharVerbo(listPontos);
			if(verbo!=null && verbo.equals(Conceitos.verboSerPresente)){
				//cancelar a duvida
				return significados;
			}
			System.out.println("duvida verbo "+verbo);
			throw new DuvidaException(duvidas);
		}
		return significados;
	}
	
}
