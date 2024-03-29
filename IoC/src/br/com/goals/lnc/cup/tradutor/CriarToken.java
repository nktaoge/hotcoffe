package br.com.goals.lnc.cup.tradutor;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.util.Opcoes;
import br.com.goals.lnc.bo.Compilador;
import br.com.goals.lnc.bo.Programador;
import br.com.goals.lnc.cup.BaseCup;
import br.com.goals.lnc.vo.ClasseGramatical;

public class CriarToken extends BaseCup implements ClasseGramatical{
	public static final String TOKEN_PACKAGE = "br.com.goals.lnc.token.dicionario";
	public static final String TOKEN_SRC_JAVA = "br/com/goals/lnc/token/dicionario/";
	

	private String duvida=null;
	private String className = null;
	private static Logger logger = Logger.getLogger(CriarToken.class);

	public void setDuvida(String duvida) {
		this.duvida = duvida;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDuvida() {
		return duvida;
	}
	@Override
	protected void iniciar() throws Exception {
		
		sistema.perguntar("O que significa '"+duvida+"'?");
		
		//Carregando opcoes do que poderia ser
		Opcoes opcoes = new Opcoes();
		opcoes.add(UM_VERBO);//um metodo
		opcoes.add(UM_SUBSTANTIVO);//classe ou instancia
		opcoes.add(UM_ADJETIVO);//um atributo

		//ator responde
		ator.responder(opcoes);
		logger.debug("respondido");
		sistema.mostrar("Ok " + opcoes.getEscolha());
		if(UM_SUBSTANTIVO.equals(opcoes.getEscolha())){
			Compilador.criarToken(TOKEN_PACKAGE,className,UM_SUBSTANTIVO,duvida,TOKEN_SRC_JAVA);
		}else if(UM_VERBO.equals(opcoes.getEscolha())){
			//criar o metodoopcoes
			Compilador.criarToken(TOKEN_PACKAGE,className,UM_VERBO,duvida,TOKEN_SRC_JAVA);
		}else if(UM_ADJETIVO.equals(opcoes.getEscolha())){
			//perguntar o atributo
			sistema.perguntar("'"+duvida+"' é um atributo de? Ex.: beleza");
			Opcoes texto = new Opcoes();
			ator.responder(texto);
			logger.debug("respondido " + texto.getEscolha());
			/*
			 * informar o atributo para ser colocado no substantivo/sujeito
			 * no significado
			 */
			//criar o adjetivo
			Programador.criarAdjetivo(className,duvida,texto.getEscolha());			
		}else if(UM_ARTIGO.toLowerCase().equals(opcoes.getEscolha().toLowerCase())){
			//criar o artigo
			Compilador.criarToken(TOKEN_PACKAGE,className,UM_ARTIGO,duvida,TOKEN_SRC_JAVA);
		}else{
			//none above
		}
	}
	
	private void doing(){
		StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stackTraceElement.length; i++) {
			logger.debug(stackTraceElement[i].getFileName() + 
				" - " + stackTraceElement[i].getMethodName() +
				":" + stackTraceElement[i].getLineNumber()
			);
		}
	}
	
}
