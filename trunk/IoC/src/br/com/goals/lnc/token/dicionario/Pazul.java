package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pazul extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pazul.class);
	public Pazul(){
		logger.debug("Pazul instanciado...");
		getPodeSerClasseGramatical().add("Um adjetivo");
	}
	public String getEscrita(){
		return "azul";
	}
}