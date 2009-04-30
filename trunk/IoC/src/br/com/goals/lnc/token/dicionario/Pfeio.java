package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pfeio extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pfeio.class);
	public Pfeio(){
		this.getSignificados().add("Sfeio");
		logger.debug("Pfeio instanciado...");
		getPodeSerClasseGramatical().add("Um adjetivo");
	}
	public String getEscrita(){
		return "feio";
	}
}