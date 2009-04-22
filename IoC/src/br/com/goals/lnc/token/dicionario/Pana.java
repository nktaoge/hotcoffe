package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pana extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pana.class);
	public Pana(){
		logger.debug("Pana instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "ana";
	}
}