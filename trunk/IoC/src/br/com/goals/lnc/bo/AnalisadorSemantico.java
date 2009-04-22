package br.com.goals.lnc.bo;

import org.apache.log4j.Logger;

import br.com.goals.lnc.vo.FraseSintatica;
import br.com.goals.lnc.vo.UmaPalavra;

public class AnalisadorSemantico {
	private static Logger logger = Logger.getLogger(AnalisadorSemantico.class);
	public static final String SIG_PACK="br.com.goals.lnc.sig";
	public static final String SIG_SRC="br/com/goals/lnc/sig/";
	public synchronized static String analisar(FraseSintatica fraseSintatica) {
		String retorno = null;
		//Pegar o sig(s) da palavra
		for(UmaPalavra umaPalavra:fraseSintatica.getSujeito().getPalavras()){
			boolean temSignificado = false;
			if(umaPalavra.getSignificados()!=null){
				if(umaPalavra.getSignificados().size()==1){
					try {
						Class.forName(SIG_PACK + '.' + umaPalavra.getSignificados().get(0));
						temSignificado = true;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			if(!temSignificado){
				String className = 'S' + umaPalavra.getClass().getSimpleName().substring(1);
				try{
					Compilador.criarSig(SIG_PACK, className, SIG_SRC);
					umaPalavra.getSignificados().add(className);
				}catch(Exception e){
					logger.error("Erro ao criar sig " + className);
				}
			}
		}
		
		return retorno;
	}

}
