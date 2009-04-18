package br.com.goals.lnc.cup;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
import br.com.goals.hotcoffe.ioc.casosdeuso.util.Opcoes;

public class CriarToken extends UmCasoDeUso{
	public static final String TOKEN_PACKAGE = "br.com.goals.lnc.token.dicionario";
	public static final String TOKEN_SRC_JAVA = "br/com/goals/lnc/token/dicionario/";
	
	public static final String UM_SUBSTANTIVO = "Um substantivo";
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
		Opcoes opcoes = new Opcoes();
		opcoes.add("um verbo");//um metodo
		opcoes.add(UM_SUBSTANTIVO);//classe ou instancia
		opcoes.add("um adjetivo");//um atributo
		logger.debug("Responder opcoes");
		ator.responder(opcoes);
		logger.debug("respondido");
		sistema.mostrar("Ok " + opcoes.getEscolha());
		if(UM_SUBSTANTIVO.equals(opcoes.getEscolha())){
			criarToken(className);
		}
	}
	
	/**
	 * Cria a classe no pacote token
	 * @param className
	 * @throws ClassNotFoundException 
	 */
	private static void criarToken(String className) throws ClassNotFoundException {
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
			
			// Get a File object for the package
			File directory = null;
			try {
				ClassLoader cld = Thread.currentThread().getContextClassLoader();
				if (cld == null) {
					throw new ClassNotFoundException("Can't get class loader.");
				}
				String path = '/' + TOKEN_SRC_JAVA;
				URL resource = cld.getResource(path);
				if (resource == null) {
					throw new ClassNotFoundException("No resource for " + path);
				}
				directory = new File(resource.getFile());
			} catch (NullPointerException x) {
				throw new ClassNotFoundException(TOKEN_SRC_JAVA + " (" + directory
						+ ") does not appear to be a valid package");
			}
			
			//escreve a classe
			File arq = new File(directory,className + ".java");
			logger.debug("Criada a classe " + arq.getAbsolutePath());
			FileUtils.writeStringToFile(arq, classCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
