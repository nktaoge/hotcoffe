package br.quickstart.casosdeuso.jogo;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
import br.quickstart.entidade.Jogador;

public class CadastrarProjSoft extends UmCasoDeUso{
	private static Logger logger = Logger.getLogger(CadastrarProjSoft.class);
	@Override
	public void iniciar() throws Exception {
		try{
			ator.preencher(new Jogador());
		}catch(Exception e){
			logger.error("Erro ao qq coisa",e);
		}
	}
	
}
