package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pverde extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pverde.class);
	public Pverde(){
		logger.debug("Pverde instanciado...");
		getPodeSerClasseGramatical().add("Um adjetivo");
	}
	public String getEscrita(){
		return "verde";
	}
}