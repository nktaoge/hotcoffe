package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Puma extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Puma.class);
	public Puma(){
		logger.debug("Puma instanciado...");
		getPodeSerClasseGramatical().add("Um artigo");
	}
	public String getEscrita(){
		return "uma";
	}
}