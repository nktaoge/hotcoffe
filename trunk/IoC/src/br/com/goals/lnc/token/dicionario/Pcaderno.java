package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pcaderno extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pcaderno.class);
	public Pcaderno(){
		logger.debug("Pcaderno instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "caderno";
	}
}