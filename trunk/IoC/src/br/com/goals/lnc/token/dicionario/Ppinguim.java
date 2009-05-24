package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Ppinguim extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Ppinguim.class);
	public Ppinguim(){
		this.getSignificados().add("Spinguim");
		logger.debug("Ppinguim instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "pinguim";
	}
}