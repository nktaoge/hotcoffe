package br.com.goals.grafo.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.grafo.CAL;
import br.com.goals.grafo.modelo.Emissor;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;
/**
 * Processa a escuta das palavras
 * @author Fabio Issamu Oshiro
 *
 */
public class Escutar {
	private static PontoDao pontoDao = PontoDao.getInstance();
	private CAL parent;
	public Escutar(CAL parent){
		this.parent = parent;
	}
	public List<Ponto> escutar(String texto, Emissor emissor){
		if(emissor==null){
			emissor=new Emissor();
			parent.setEmissor(emissor);
		}
		Ponto umaMensagemDeAlgo = pontoDao.acharOuCriarPorDescricao(Conceitos.MENSAGEM_AO_CAL);
		Ponto instanciaDeUmaMensagem = new Ponto(Conceitos.INSTANCIA_MENSAGEM);
		pontoDao.criar(instanciaDeUmaMensagem);
		pontoDao.ligar(umaMensagemDeAlgo,instanciaDeUmaMensagem);
		if(emissor.getPonto()==null){
			Ponto algoMensageiro = new Ponto(Conceitos.INSTANCIA_ALGO);
			Ponto algo = pontoDao.acharOuCriarPorDescricao(Conceitos.ALGO_MENSAGEIRO);
			emissor.setPonto(algoMensageiro);
			algoMensageiro = pontoDao.criar(algoMensageiro);
			//ligar o algo com o ponto
			pontoDao.ligar(algo,algoMensageiro);
			parent.setEmissor(emissor);
		}
		ArrayList<Ponto> listTexto = new ArrayList<Ponto>();  
		String token[] = texto.split("\\s+");
		
		pontoDao.ligar(instanciaDeUmaMensagem, emissor.getPonto());
		for (int i = 0; i < token.length; i++) {
			String strToken = token[i];
			if(strToken.contains("?")){
				String subToken[] = strToken.split("\\?");
				for(int j=0;j<subToken.length;j++){
					Ponto pToken = retornarToken(subToken[j]);
					pontoDao.ligar(instanciaDeUmaMensagem, pToken);			
					listTexto.add(pToken);
					
					Ponto pTokenInterrogacao = retornarToken("?");
					pontoDao.ligar(instanciaDeUmaMensagem, pTokenInterrogacao);			
					listTexto.add(pTokenInterrogacao);
				}
			}else{
				Ponto pToken = retornarToken(strToken);
				pontoDao.ligar(instanciaDeUmaMensagem, pToken);			
				listTexto.add(pToken);
			}
		}
		return listTexto;
	}
	private Ponto retornarToken(String strToken){
		Ponto pToken = pontoDao.acharPorNome(strToken);
		if(pToken==null){
			pToken = new Ponto();
			pToken.setNome(strToken);
			pToken = pontoDao.criar(pToken);
			pontoDao.ligar(Conceitos.duvida,pToken);
		}
		return pToken;
	}
}
