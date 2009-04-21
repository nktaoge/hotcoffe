package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Ptem extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Ptem.class);
	public Ptem(){
		logger.debug("Ptem instanciado...");
		getPodeSerClasseGramatical().add("Um verbo");
	}
	public String getEscrita(){
		return "tem";
	}
}