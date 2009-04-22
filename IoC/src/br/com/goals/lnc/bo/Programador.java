package br.com.goals.lnc.bo;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import br.com.goals.lnc.cup.CriarToken;

public class Programador {
	private static Logger logger = Logger.getLogger(Programador.class);
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
	public void criarMetodo(String sigName, String metodoVerbo,String comments) throws Exception {
		String metodoBody = "" +
				"public boolean " + metodoVerbo + "(Object obj){\n" +
				"\t\treturn true;\n" +
				"\t}\n\n\t";
		
		
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
}
