package br.com.goals.lnc.token.dicionario;

import org.apache.log4j.Logger;
import br.com.goals.lnc.vo.UmaPalavra;
public class Pfaculdade extends UmaPalavra{
	private static Logger logger = Logger.getLogger(Pfaculdade.class);
	public Pfaculdade(){
		logger.debug("Pfaculdade instanciado...");
	}
}