package br.com.goals.lnc;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
/**
 * 
 * Descrever casos de uso (FrameWork IoC)
 * 
 * @author Fabio Issamu Oshiro
 *
 */
public class MainCompiler {
	private static final String TOKEN_PACKAGE = "br.com.goals.ai.compiler.token.dicionario";
	private static final String TOKEN_SRC_JAVA = "src/main/java/br/com/goals/ai/compiler/token/dicionario/";
	private static Logger logger = Logger.getLogger(MainCompiler.class);
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
	private static String processar(String s){
		String[] token = s.split("\\s");
		for (int i = 0; i < token.length; i++) {
			String className = formatar2ClassName(token[i]);
			try {
				Object c = Class.forName(TOKEN_PACKAGE+'.'+className).newInstance();
			} catch (ClassNotFoundException e) {
				criarToken(className);
				compilar();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static void compilar() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Cria a classe no pacote token
	 * @param className
	 */
	private static void criarToken(String className) {
		try {
			String classCode = 
				"package " + TOKEN_PACKAGE + ";\n\n" +
				"import org.apache.log4j.Logger;\n" +
				"import br.com.goals.ai.compiler.token.UmaPalavra;\n" +
				"public class " + className + " extends UmaPalavra{\n" +
				"\tprivate static Logger logger = Logger.getLogger("+className+".class);\n"+
				"\tpublic " + className + "(){\n" + 
				"\t\tlogger.debug(\""+className+" instanciado...\");\n" +
				"\t}\n"+
				"}";
			FileUtils.writeStringToFile(new File(TOKEN_SRC_JAVA + className + ".java"), classCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			String s = sc.nextLine();
			System.out.println(processar(s));
		}
	}
}
