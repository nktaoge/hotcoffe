package br.com.goals.template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {
	public static String substituiAtributoTag(String html, String tag, String atributo, String novoValor) {
		String retorno = "";
		String reg = "<" + tag + "(\\s|\\s.*?\\s)" + atributo + "=\".*?\"(.*?)>";
		Pattern pat = Pattern.compile(reg, Pattern.DOTALL);
		Matcher mat = pat.matcher(html);
		if (mat.find()) {
			String ini = mat.group(1);
			String fim = mat.group(2);
			retorno = mat.replaceAll("<" + tag + ini + atributo + "=\"" + novoValor + "\" " + fim + ">");
		}
		return retorno;
	}
	public static String getConteudoTexto(String conteudo){
		Pattern patTag = Pattern.compile("<.*?>",Pattern.DOTALL);
		Matcher matTag = patTag.matcher(conteudo);
		if(matTag.find()){			
			conteudo = matTag.replaceAll(" ");
		}	
		patTag = Pattern.compile("\\s\\s\\s*",Pattern.DOTALL);
		matTag = patTag.matcher(conteudo);
		
		if(matTag.find()){			
			conteudo=matTag.replaceAll(" ");
		}	
		
		return conteudo;
	}
}
