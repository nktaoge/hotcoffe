package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Poshiro extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Poshiro.class);
	public Poshiro(){
		logger.debug("Poshiro instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "oshiro";
	}
}