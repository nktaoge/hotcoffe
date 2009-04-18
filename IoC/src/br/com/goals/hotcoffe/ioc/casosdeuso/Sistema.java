package br.com.goals.hotcoffe.ioc.casosdeuso;

import java.io.PrintWriter;

import org.apache.log4j.Logger;

public class Sistema {
	private static Logger logger = Logger.getLogger(Sistema.class);
	private UmCasoDeUso umCasoDeUso;
	public Sistema(UmCasoDeUso umCasoDeUso) {
		this.umCasoDeUso=umCasoDeUso;
	}

	/**
	 * Mostra uma mensagem
	 * @param mensagem message to show
	 */
	public void mostrar(String mensagem){
		try{
			umCasoDeUso.getTemplate().adicionarMensagem(mensagem);
		}catch(Exception e){
			logger.error("Erro ao mostrar mensagem '" + mensagem + "'", e);
		}
	}

	public void perguntar(String mensagem) {
		try{
			umCasoDeUso.getTemplate().adicionarMensagem(mensagem);
		}catch(Exception e){
			logger.error("Erro ao mostrar mensagem '" + mensagem + "'", e);
		}	
	}
}
