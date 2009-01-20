package br.com.goals.grafo.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.debug.Sysou;
import br.com.goals.grafo.controle.verbo.SuperVerbo;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

public class ExecutarVerbo {
	private Ponto sujeito=null;
	private Ponto predicado=null;
	private Pensar pensar;
	private PontoDao pontoDao = PontoDao.getInstance();
	private Sysou sysou = new Sysou(this,0);
	public ExecutarVerbo(Pensar pensar){
		this.pensar = pensar;
	}
	public void executar(Ponto pontoVerbo,List<Ponto> pontos){
		sysou.onEnterFunction(1,"executar");
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
		sysou.onExitFunction(1,"executar");
	}
	private Ponto acharPredicado(Ponto verb, List<Ponto> pontos) {
		sysou.onEnterFunction(1,"acharPredicado");
		Ponto retorno = null;
		int t = pontos.size();
		int r=0;
		
		for(int i=0;i<t;i++){
			Ponto ponto = pontos.get(i);
			sysou.print(1,"'"+ponto.getNome()+"'");
			for(Ponto pontoA:ponto.getLigacaoA()){
				if(pontoA.equals(verb)){
					sysou.println(1," é o verbo");
					r=++i;
					break;
				}
			}
			sysou.println(1," não é verbo");
		}
		//achar grupo x
		ArrayList<Ponto> listGrupo = new ArrayList<Ponto>();
		for(int i = r;i<t;i++){
			listGrupo.add(pontos.get(i));
		}
		if(listGrupo.size()==1){
			//não precisa criar o grupo
			//achar o significado
			Ponto pontoSingle = listGrupo.get(0);
			if(pontoSingle.getLigacaoA().size()>0){
				retorno = pontoSingle.getLigacaoA().get(0);
			}else{
				Ponto pSig = new Ponto();
				pSig = pontoDao.criar(pSig);
				pontoDao.ligar(pSig, pontoSingle, Conceitos.significa);
				retorno = pSig;
			}
		}else{
			//TODO deveria achar uma lista?
			List<Ponto> ponto = pontoDao.acharGrupo(listGrupo,Conceitos.grupo);
			if(ponto==null || ponto.size()==0){
				//criar o grupo
				sysou.println(1,"Criado o grupo");
				Ponto pontoA = new Ponto(Conceitos.INSTANCIA_PREDICADO);
				Ponto pontoGrupo = pontoDao.criarGrupo(pontoA,listGrupo,Conceitos.grupo);
				Ponto pSig = new Ponto();
				pSig = pontoDao.criar(pSig);
				pontoDao.ligar(pSig, pontoGrupo, Conceitos.significa);
				retorno = pSig;
			}else{
				//TODO melhorar isso
				if(ponto.size()>0){
					Ponto pontoGrupo = ponto.get(0);
					List<Ponto>rA = pontoDao.getLigacaoA(pontoGrupo, Conceitos.significa);
					if(rA.size()>0){
						//TODO melhorar isso
						retorno = rA.get(0);
					}
				}
			}
		}
		sysou.onExitFunction(1,"acharPredicado");
		return retorno;
	}
	/**
	 * Muitas vezes entre o artigo e o verbo
	 * @param verb
	 * @param pontos
	 * @return sujeito
	 */
	private Ponto acharSujeito(Ponto verb, List<Ponto> pontos){
		//TODO melhorar este algoritimo
		/*
		 * fazer um grupo antes e depois do verbo
		 */
		sysou.onEnterFunction(1,"acharSujeito");
		Ponto retorno = null;
		Ponto lastPonto = null;
		for(Ponto ponto:pontos){
			sysou.print(1,ponto.getNome());
			for(Ponto pontoA:ponto.getLigacaoA()){
				if(pontoA.equals(verb)){
					sysou.println(1," é o verbo");
					retorno = lastPonto;
				}
			}
			if(retorno!=null) break;
			sysou.println(1," não é verbo");
			lastPonto=ponto;
		}
		sysou.print(1,"sujeito = " + retorno);
		sysou.onExitFunction(1,"acharSujeito");
		return retorno;
	}
}
