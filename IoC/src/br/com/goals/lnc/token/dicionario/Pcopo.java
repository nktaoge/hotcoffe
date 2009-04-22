package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pcopo extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pcopo.class);
	public Pcopo(){
		logger.debug("Pcopo instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "copo";
	}
}