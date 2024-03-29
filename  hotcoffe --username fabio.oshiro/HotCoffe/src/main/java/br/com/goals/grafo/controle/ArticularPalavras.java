package br.com.goals.grafo.controle;

import java.util.List;

import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

public class ArticularPalavras {
	private PontoDao pontoDao = PontoDao.getInstance();
	/**
	 * 
	 * @param listPontos
	 * @return string com o nome dos pontos
	 */
	public String responder(List<Ponto> listPontos) {
		String resposta = "";
		if(listPontos==null) return "";
		for(Ponto ponto : listPontos){
			resposta +=buscaNome(ponto)+" ";
		}
		return resposta;
	}
	public String buscaNome(Ponto ponto){
		String retorno="";
		if(ponto.getNome()==null){
			List<Ponto> rB = pontoDao.getLigacaoB(ponto,Conceitos.significa);
			if(rB.size()>0){
				if(rB.get(0).getNome()==null){
					retorno = buscaNome(rB.get(0));
				}else{
					retorno = rB.get(0).getNome();
				}
			}else{
				rB = pontoDao.getLigacaoB(ponto,Conceitos.grupo);
				for(Ponto pB:rB){
					retorno+= pB.getNome()+" ";
				}
			}
		}else{
			retorno = ponto.getNome();
		}
		return retorno;
	}
}
