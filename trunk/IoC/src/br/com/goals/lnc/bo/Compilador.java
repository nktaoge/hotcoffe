package br.com.goals.lnc.bo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class Compilador {
	private static Logger logger = Logger.getLogger(Compilador.class);
	
	public static String workspaceSrc = null;
	public static void criarSig(String sigPack, String className,	String sigSrc) throws ClassNotFoundException, IOException {
		String classCode = 
			"package " + sigPack + ";\n\n" +
			"import org.apache.log4j.Logger;\n" +
			"public class " + className + " {\n" +
			"\tprivate static Logger logger = Logger.getLogger("+className+".class);\n"+
			"\tpublic " + className + "(){\n" + 
			"\t\tlogger.debug(\""+className+" instanciado...\");\n" +
			"\t}\n"+
			"}";
		File arq = null;
		File directory = null;
		if(workspaceSrc==null){
			directory = getClassDirectory(sigSrc);
		}else{
			directory = new File(workspaceSrc + sigSrc);
		}
		//escreve a classe
		arq = new File(directory,className + ".java");
		logger.debug("Criada a classe " + arq.getAbsolutePath());
		FileUtils.writeStringToFile(arq, classCode);
		compilar(className,arq,workspaceSrc);
	}
	/**
	 * Cria a classe no pacote token
	 * @param className
	 * @throws ClassNotFoundException 
	 */
	public static void criarToken(String pack,String className,String classeGramatical,String escrita,String srcJava) throws ClassNotFoundException {
		try {
			String classCode = 
				"package " + pack + ";\n\n" +
				"import org.apache.log4j.Logger;\n" +
				"import br.com.goals.lnc.vo.UmaPalavra;\n" +
				"public class " + className + " extends UmaPalavra{\n" +
				"\tprivate static Logger logger = Logger.getLogger("+className+".class);\n"+
				"\tpublic " + className + "(){\n" + 
				"\t\tlogger.debug(\""+className+" instanciado...\");\n" +
				"\t\tgetPodeSerClasseGramatical().add(\""+classeGramatical+"\");\n" +
				"\t}\n"+
				//Sem DB
				"\tpublic String getEscrita(){\n"+
				"\t\treturn \""+escrita.replace("\r", "").replace("\n", "\\n").replace("\"", "\\\"")+"\";\n" +
				"\t}\n"+
				"}";
			File arq = null;
			File directory = null;
			if(workspaceSrc==null){
				directory = getClassDirectory(srcJava);
			}else{
				directory = new File(workspaceSrc + srcJava);
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
	
	private static void compilar(String className,File arq,String workspaceSrc) throws ClassNotFoundException{
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
	
	private static File getClassDirectory(String dir) throws ClassNotFoundException{
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
