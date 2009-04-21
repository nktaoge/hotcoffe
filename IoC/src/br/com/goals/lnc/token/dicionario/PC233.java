package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class PC233 extends UmaPalavra{
	private static Logger logger = Logger.getLogger(PC233.class);
	public PC233(){
		logger.debug("PC233 instanciado...");
		getPodeSerClasseGramatical().add("Um verbo");
	}
	public String getEscrita(){
		return "é";
	}
}