package br.com.goals.grafo.controle;

import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

/**
 * Responsavel por carregar conceitos iniciais
 * @author Fabio Issamu Oshiro
 *
 */
public class Conceitos {
	private static PontoDao pontoDao = PontoDao.getInstance();
	public static final String DUVIDA = "Dúvida, algo desconhecido";
	public static final String MENSAGEM_AO_CAL = "Uma mensagem de algo emitida ao CAL";
	public static final String ALGO_MENSAGEIRO = "Algo não identificado que emite mensagem";
	public static final String INSTANCIA_ALGO = "Algo que falou";
	public static final String INSTANCIA_MENSAGEM = "Instância de uma mensagem";
	
	/**
	 * Carrega no banco alguns conceitos iniciais
	 */
	public static void carregarConceitos(){
		criar(DUVIDA);
		criar(MENSAGEM_AO_CAL);
		criar(ALGO_MENSAGEIRO);
	}
	/**
	 * Facilitador
	 * @param descricao
	 * @return ponto criado
	 */
	private static Ponto criar(String descricao){
		return pontoDao.acharOuCriarPorDescricao(descricao);
	}
}
