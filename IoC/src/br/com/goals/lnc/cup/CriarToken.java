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
	public static String workspaceSrc = null;
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
		
		StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stackTraceElement.length; i++) {
			logger.debug(stackTraceElement[i].getFileName() + 
				" - " + stackTraceElement[i].getMethodName() +
				":" + stackTraceElement[i].getLineNumber()
			);
		}
		
	}
	
	/**
	 * Cria a classe no pacote token
	 * @param className
	 * @throws ClassNotFoundException 
	 */
	private void criarToken(String className) throws ClassNotFoundException {
		try {
			
			workspaceSrc = getControlador().getInitParameter("workspaceSrc");
			
			String classCode = 
				"package " + TOKEN_PACKAGE + ";\n\n" +
				"import org.apache.log4j.Logger;\n" +
				"import br.com.goals.lnc.vo.UmaPalavra;\n" +
				"public class " + className + " extends UmaPalavra{\n" +
				"\tprivate static Logger logger = Logger.getLogger("+className+".class);\n"+
				"\tpublic " + className + "(){\n" + 
				"\t\tlogger.debug(\""+className+" instanciado...\");\n" +
				"\t}\n"+
				"}";
			File arq = null;
			File directory = null;
			if(workspaceSrc==null){
				directory = getClassDirectory(TOKEN_SRC_JAVA);
			}else{
				directory = new File(workspaceSrc + TOKEN_SRC_JAVA);
			}
			//escreve a classe
			arq = new File(directory,className + ".java");
			logger.debug("Criada a classe " + arq.getAbsolutePath());
			FileUtils.writeStringToFile(arq, classCode);
			compilar(className,arq,workspaceSrc);
		} catch (IOException e) {
			logger.error("Erro ao escrever classe '" + className + "'",e);
		}
	}
	
	private void compilar(String className,File arq,String workspaceSrc) throws ClassNotFoundException{
		File directory = getClassDirectory("");
		File webInf = directory.getParentFile();
		String comando = "javac -classpath " +
				webInf.getAbsolutePath() + File.separatorChar + "lib" + File.separatorChar + "log4j-1.2.15.jar;" +
				webInf.getAbsolutePath() + File.separatorChar + "lib" + File.separatorChar + "commons-io-1.4.jar;" +
				webInf.getAbsolutePath() + File.separatorChar + "provided" + File.separatorChar + "servlet-api.jar" +
				" -sourcepath "+workspaceSrc+" "+ 
				arq.getAbsolutePath() +
				" -d " + directory.getAbsolutePath();
		try {
			logger.debug("exec " + comando);
			Runtime.getRuntime().exec(comando);
		} catch (IOException e) {
			logger.error("Erro ao compilar classe '" + className + "'\n" + comando,e);
		}
	}
	
	private File getClassDirectory(String dir) throws ClassNotFoundException{
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = File.separatorChar + dir;
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(dir + " (" + directory
					+ ") does not appear to be a valid package");
		}
		return directory;
	}
}
