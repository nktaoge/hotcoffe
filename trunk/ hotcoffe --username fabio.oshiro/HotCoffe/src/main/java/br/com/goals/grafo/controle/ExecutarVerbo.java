package br.com.goals.grafo.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.grafo.controle.verbo.SuperVerbo;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

public class ExecutarVerbo {
	private Ponto sujeito=null;
	private Ponto predicado=null;
	private Pensar pensar;
	private PontoDao pontoDao = PontoDao.getInstance();
	public ExecutarVerbo(Pensar pensar){
		this.pensar = pensar;
	}
	public void executar(Ponto pontoVerbo,List<Ponto> pontos){
		if(pontoVerbo.getClasse()!=null && !pontoVerbo.getClasse().equals("")){
			try{
				SuperVerbo superVerbo =(SuperVerbo)Class.forName("br.com.goals.grafo.controle.verbo."+pontoVerbo.getClasse()).newInstance();
				sujeito = acharSujeito(pontoVerbo,pontos);
				predicado = acharPredicado(pontoVerbo,pontos);
				superVerbo.setCal(pensar.getCal());
				superVerbo.executar(sujeito,predicado);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private Ponto acharPredicado(Ponto verb, List<Ponto> pontos) {
		int t = pontos.size();
		int r=0;
		for(int i=0;i<t;i++){
			Ponto ponto = pontos.get(i);
			System.out.print("'"+ponto.getNome()+"'");
			for(Ponto pontoA:ponto.getLigacaoA()){
				if(pontoA.equals(verb)){
					System.out.println(" é o verbo");
					r=++i;
					break;
				}
			}
			System.out.println(" não é verbo");
		}
		//achar grupo x
		ArrayList<Ponto> listGrupo = new ArrayList<Ponto>();
		for(int i = r;i<t;i++){
			listGrupo.add(pontos.get(i));
		}
		//TODO deveria achar uma lista?
		List<Ponto> ponto = pontoDao.acharGrupo(listGrupo);
		if(ponto==null || ponto.size()==0){
			//criar o grupo
			System.out.println("Criado o grupo");
			Ponto pontoA = new Ponto("Grupo predicado");
			return pontoDao.criarGrupo(pontoA,listGrupo);
		}
		return ponto.get(0);
	}
	/**
	 * Muitas vezes entre o artigo e o verbo
	 * @param verb
	 * @param pontos
	 * @return sujeito
	 */
	private Ponto acharSujeito(Ponto verb, List<Ponto> pontos){
		//TODO melhorar este algoritimo
		Ponto lastPonto = null;
		for(Ponto ponto:pontos){
			System.out.print(ponto.getNome());
			for(Ponto pontoA:ponto.getLigacaoA()){
				if(pontoA.equals(verb)){
					System.out.println(" é o verbo");
					return lastPonto;
				}
			}
			System.out.println(" não é verbo");
			lastPonto=ponto;
		}
		return null;
	}
}
