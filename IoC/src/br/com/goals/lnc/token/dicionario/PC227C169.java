package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class PC227C169 extends UmaPalavra{
	private static Logger logger = Logger.getLogger(PC227C169.class);
	public PC227C169(){
		logger.debug("PC227C169 instanciado...");
		getPodeSerClasseGramatical().add("Um verbo");
	}
	public String getEscrita(){
		return "Ã©";
	}
}