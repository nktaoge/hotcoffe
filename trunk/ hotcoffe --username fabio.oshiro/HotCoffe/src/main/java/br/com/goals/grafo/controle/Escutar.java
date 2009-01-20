package br.com.goals.grafo.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.debug.Sysou;
import br.com.goals.grafo.CAL;
import br.com.goals.grafo.modelo.Emissor;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;
/**
 * Processa a escuta das palavras,
 * guarda o que foi escrito
 * em forma de mensagem
 * @author Fabio Issamu Oshiro
 *
 */
public class Escutar {
	private static PontoDao pontoDao = PontoDao.getInstance();
	private CAL parent;
	private Sysou sysou = new Sysou(this,0);
	public Escutar(CAL parent){
		this.parent = parent;
	}
	public List<Ponto> escutar(String texto, Emissor emissor){
		sysou.onEnterFunction(1,"escutar");
		if(emissor==null){
			emissor=new Emissor();
			parent.setEmissor(emissor);
		}
		
		Ponto umaMensagemDeAlgo = pontoDao.acharOuCriarPorDescricao(Conceitos.MENSAGEM_AO_CAL);
		Ponto instanciaDeUmaMensagem = new Ponto(Conceitos.INSTANCIA_MENSAGEM);
		pontoDao.criar(instanciaDeUmaMensagem);
		pontoDao.ligar(umaMensagemDeAlgo,instanciaDeUmaMensagem,Conceitos.p_instancia);
		if(emissor.getPonto()==null){
			Ponto algoMensageiro = new Ponto(Conceitos.INSTANCIA_ALGO);
			Ponto algo = pontoDao.acharOuCriarPorDescricao(Conceitos.ALGO_MENSAGEIRO);
			emissor.setPonto(algoMensageiro);
			algoMensageiro = pontoDao.criar(algoMensageiro);
			//ligar o algo com o ponto
			pontoDao.ligar(algo,algoMensageiro,Conceitos.p_instancia);
			parent.setEmissor(emissor);
		}
		ArrayList<Ponto> listTexto = new ArrayList<Ponto>();  
		String token[] = texto.split("\\s+");
		
		pontoDao.ligar(instanciaDeUmaMensagem, emissor.getPonto(),Conceitos.algo_mensageiro);
		for (int i = 0; i < token.length; i++) {
			String strToken = token[i];
			if(strToken.contains("?")){
				String subToken[] = strToken.split("\\?");
				for(int j=0;j<subToken.length;j++){
					Ponto pToken = recuperarPonto(subToken[j]);
					pontoDao.ligar(instanciaDeUmaMensagem, pToken,Conceitos.grupo_mensagem);			
					listTexto.add(pToken);
					Ponto pTokenInterrogacao = recuperarPonto("?");
					pontoDao.ligar(instanciaDeUmaMensagem, pTokenInterrogacao,Conceitos.grupo_mensagem);
					sysou.println(1,"add pTokenInterrogacao " + pTokenInterrogacao);
					listTexto.add(pTokenInterrogacao);
				}
			}else{
				Ponto pToken = recuperarPonto(strToken);
				pontoDao.ligar(instanciaDeUmaMensagem, pToken,Conceitos.grupo_mensagem);
				sysou.println(1,"add pToken " + pToken);
				listTexto.add(pToken);
			}
		}
		sysou.onExitFunction(1,"escutar");
		return listTexto;
	}
	/**
	 * Caso seja criado, liga o ponto criado ao ponto de dúvida
	 * @param strToken
	 * @return Ponto achado ou criado
	 */
	private Ponto recuperarPonto(String strToken){
		Ponto pToken = pontoDao.acharPorNome(strToken);
		if(pToken==null){
			pToken = new Ponto();
			pToken.setNome(strToken);
			pToken = pontoDao.criar(pToken);
			pontoDao.ligar(Conceitos.duvida,pToken,Conceitos.p_instancia);
		}
		return pToken;
	}
}
