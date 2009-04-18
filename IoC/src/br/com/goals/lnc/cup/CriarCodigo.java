package br.com.goals.lnc.cup;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
import br.com.goals.lnc.vo.Comando;

public class CriarCodigo extends UmCasoDeUso{
	private static Logger logger = Logger.getLogger(CriarCodigo.class);
	
	private static String formatar2ClassName(String s){
		String retorno = s.toLowerCase();
		for (int i = 0; i < retorno.length(); i++) {
			char c = retorno.charAt(i);
			if(!Character.isDigit(c) && (c>'z' ||c<'a')){
				retorno = retorno.substring(0,i)+ "C" + (int)c + retorno.substring(i+1); 
			}
		}
		retorno = "P"+retorno;
		
		logger.debug("formatar2ClassName("+s+") = " + retorno);
		return retorno;
	}
	@Override
	protected void iniciar() throws Exception {
		while (true) {
			Comando comando= new Comando();
			ator.preencher(comando);
			tratar(comando);
		}
	}
	private void tratar(Comando comando) throws Exception {
		String s =comando.getComando();
		String[] token = s.split("\\s");
		for (int i = 0; i < token.length; i++) {
			String className = formatar2ClassName(token[i]);
			try {
				Object c = Class.forName(CriarToken.TOKEN_PACKAGE+'.'+className).newInstance();
			} catch (ClassNotFoundException e) {
				CriarToken criarToken = new CriarToken();
				criarToken.setDuvida(token[i]);
				criarToken.setClassName(className);
				usar(criarToken);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}		
	}
}
