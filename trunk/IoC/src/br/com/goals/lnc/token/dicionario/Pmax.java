package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pmax extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pmax.class);
	public Pmax(){
		logger.debug("Pmax instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "max";
	}
}