package br.com.goals.lnc.bo;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import br.com.goals.lnc.cup.CriarToken;
import br.com.goals.lnc.vo.ClasseGramatical;
import br.com.goals.lnc.vo.Metodo;
/**
 * Modifica a classe
 * @author Fabio Issamu Oshiro
 *
 */
public class Programador implements ClasseGramatical{
	private static Logger logger = Logger.getLogger(Programador.class);
	
	/**
	 * Cria a classe do adjetivo no pacote token
	 * @param className
	 * @throws ClassNotFoundException 
	 */
	public static void criarAdjetivo(String className,String escrita, String atributo) throws ClassNotFoundException {
		try {
			String classCode = 
				"package " + CriarToken.TOKEN_PACKAGE + ";\n\n" +
				"import org.apache.log4j.Logger;\n" +
				"import br.com.goals.lnc.vo.UmAdjetivo;\n" +
				//TODO herdar UmAdjetivo, que deve ter o nome da propriedade, ex.: fulano.beleza = feio;
				"public class " + className + " extends UmAdjetivo{\n" +
				"\tprivate static Logger logger = Logger.getLogger("+className+".class);\n"+
				"\tpublic " + className + "(){\n" + 
				"\t\tlogger.debug(\""+className+" instanciado...\");\n" +
				"\t\tgetPodeSerClasseGramatical().add(\""+UM_ADJETIVO+"\");\n" +
				"\t}\n"+
				/*
				 * O que este adjetivo caracteriza
				 */
				"\tpublic String caracteriza(){\n"+
				"\t\treturn \"" + atributo.replace("\r", "").replace("\n", "\\n").replace("\"", "\\\"")+"\";\n" +
				"\t}\n"+
				/*
				 * Sem DB
				 */
				"\tpublic String getEscrita(){\n"+
				"\t\treturn \"" + escrita.replace("\r", "").replace("\n", "\\n").replace("\"", "\\\"")+"\";\n" +
				"\t}\n"+
				"}"; 
			Compilador.compilar(CriarToken.TOKEN_PACKAGE, CriarToken.TOKEN_SRC_JAVA, className, classCode);
		} catch (Exception e) {
			logger.error("Erro ao escrever classe '" + className + "'",e);
		}
	}
	
	/**
	 * Coloca na lista de getSignificados()
	 * @param palavraClassName
	 * @param sigClassName
	 * @throws Exception
	 */
	public void colocarSignificadoEmPalavra(String palavraClassName,String sigClassName) throws Exception {
		File arqJava = new File(Compilador.workspaceSrc + CriarToken.TOKEN_SRC_JAVA + palavraClassName + ".java");
		String codigo = FileUtils.readFileToString(arqJava);
		logger.info(codigo);
		codigo = codigo.replace("public "+palavraClassName + "(){", 
				"public "+palavraClassName + "(){\n" + 
				"\t\tthis.getSignificados().add(\""+sigClassName+"\");"
		);
		logger.info(codigo);
		
		FileUtils.writeStringToFile(arqJava, codigo);
		Compilador.compilar(arqJava);
	}
	
	/**
	 * Cria um metodo que retorna um boolean na classe sigName,br>
	 * recebe um Object como parametro
	 * 
	 * @param sigName nome da classe de significado
	 * @param metodoVerbo nome do metodo
	 * @throws Exception
	 */
	public void criarMetodo(String sigName, Metodo metodo) throws Exception {
		String metodoBody = "\n\t" + metodo.toString().replace("\n", "\n\t") + "\n\t";	
		File arqJava = new File(Compilador.workspaceSrc + AnalisadorSemantico.SIG_SRC + sigName + ".java");
		String codigo = FileUtils.readFileToString(arqJava);
		logger.info(codigo);
		codigo = codigo.replace("public "+sigName + "(){",
				metodoBody + 
				"public "+sigName + "(){"
		);
		logger.info(codigo);
		
		FileUtils.writeStringToFile(arqJava, codigo);
		Compilador.compilar(arqJava);
	}

	/**
	 * Transforma NomeDeClasse para nomeDeClasse
	 * @param param
	 * @return nomeDeAtributo ou nomeDeVariavel
	 */
	public static String escreverNomeVariavel(String param) {
		if(param.length()>1){
			return Character.toLowerCase(param.charAt(0))+param.substring(1);
		}else{
			return param.toLowerCase();
		}
	}
}
