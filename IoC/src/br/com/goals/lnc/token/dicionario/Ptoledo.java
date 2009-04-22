package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Ptoledo extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Ptoledo.class);
	public Ptoledo(){
		logger.debug("Ptoledo instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "toledo";
	}
}