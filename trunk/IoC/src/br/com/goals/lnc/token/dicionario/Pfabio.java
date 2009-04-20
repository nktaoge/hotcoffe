package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pfabio extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pfabio.class);
	public Pfabio(){
		logger.debug("Pfabio instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "fabio";
	}
}