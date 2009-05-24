package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class PC63 extends UmaPalavra{
	private static Logger logger = Logger.getLogger(PC63.class);
	public PC63(){
		this.getSignificados().add("SC63");
		logger.debug("PC63 instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "?";
	}
}