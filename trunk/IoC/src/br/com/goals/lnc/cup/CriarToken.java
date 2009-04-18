package br.com.goals.lnc.cup;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
import br.com.goals.hotcoffe.ioc.casosdeuso.util.Opcoes;

public class CriarToken extends UmCasoDeUso{
	private String duvida=null;
	private static Logger logger = Logger.getLogger(CriarToken.class);
	public void setDuvida(String duvida) {
		this.duvida = duvida;
	}
	
	public String getDuvida() {
		return duvida;
	}
	@Override
	protected void iniciar() throws Exception {
		sistema.perguntar("O que é "+duvida+"?");
		Opcoes opcoes = new Opcoes();
		opcoes.add("um verbo");//um metodo
		opcoes.add("um substantivo");//classe ou instancia
		opcoes.add("um adjetivo");//um atributo
		logger.debug("Responder opcoes");
		ator.responder(opcoes);
		logger.debug("respondido");
		sistema.mostrar("Ok " + opcoes.getEscolha());
	}

}
