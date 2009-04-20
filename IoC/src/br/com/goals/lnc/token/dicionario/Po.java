package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Po extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Po.class);
	public Po(){
		logger.debug("Po instanciado...");
		getPodeSerClasseGramatical().add("Um artigo");
	}
	public String getEscrita(){
		return "o";
	}
}