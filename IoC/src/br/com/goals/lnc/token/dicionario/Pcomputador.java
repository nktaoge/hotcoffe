package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pcomputador extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pcomputador.class);
	public Pcomputador(){
		this.getSignificados().add("Scomputador");
		logger.debug("Pcomputador instanciado...");
		getPodeSerClasseGramatical().add("Um substantivo");
	}
	public String getEscrita(){
		return "computador";
	}
}