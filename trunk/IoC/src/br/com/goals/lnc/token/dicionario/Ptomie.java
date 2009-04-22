package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Ptomie extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Ptomie.class);
	public Ptomie(){
		logger.debug("Ptomie instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "tomie";
	}
}