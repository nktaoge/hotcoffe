package br.com.goals.lnc.sig;

import org.apache.log4j.Logger;
public class Scomputador {
	private static Logger logger = Logger.getLogger(Scomputador.class);
	
	/**
	 * Verbo(s): é, 
	 */
	public boolean exzPC233( Sfeio sfeio){
		return true;
	}
	
public Scomputador(){
		logger.debug("Scomputador instanciado...");
	}
}