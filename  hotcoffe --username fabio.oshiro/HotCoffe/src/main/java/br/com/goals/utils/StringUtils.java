package br.com.goals.utils;

import java.net.URLEncoder;

public class StringUtils {
	/**
	 * Dexa apenas a-z A-Z 0-9 e ' ' espaco em branco
	 * @param s
	 * @return
	 */
	public static String removePontuacao(String s){
		StringBuffer buf = new StringBuffer();
		int len = (s == null ? -1 : s.length());
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c==' ') {
				buf.append(c);
			}
		}
		return buf.toString();
	}
	public static String stringEntre(String source,String a,String b) throws Exception{
		int ini = source.indexOf(a)+a.length();
		int fim = source.indexOf(b,ini);
		return source.substring(ini,fim);
	}
	public static String removeAcentos(String pStrTexto) {
		return pStrTexto.replace("á", "a").replace("ã", "a").replace("à", "a").replace("ä", "a").replace("â", "a").replace("Á", "A").replace("Ã", "A").replace("À", "A").replace("Ä", "A").replace("Â", "A").replace("é", "e").replace("è", "e").replace("ë", "e").replace("ê", "e")
				.replace("É", "E").replace("È", "E").replace("Ë", "E").replace("Ê", "E").replace("í", "i").replace("ì", "i").replace("ï", "i").replace("î", "i").replace("Í", "I").replace("Ì", "I").replace("Ï", "I").replace("Î", "I").replace("ó", "o").replace("ò", "o").replace(
						"õ", "o").replace("ö", "o").replace("ô", "o").replace("Ó", "O").replace("Ò", "O").replace("Õ", "O").replace("Ö", "O").replace("Ô", "O").replace("ú", "u").replace("ù", "u").replace("ü", "u").replace("û", "u").replace("Ú", "U").replace("Ù", "U").replace(
						"Ü", "U").replace("Û", "U").replace("ñ", "n").replace("Ñ", "N").replace("ý", "y").replace("ÿ", "y").replace("Ý", "Y").replace("ç", "c").replace("Ç", "C").replace("´", "").replace("`", "").replace("~", "").replace("^", "").replace("¨", "");
	}

	public static String tratarCaracteresEspeciaisRegex(String puro) {
		return puro.replace("\\","\\\\").replace("$", "\\$");
	}

	public static String urlEncode(String valor) {
		String retorno = "";

		try {

			retorno = URLEncoder.encode(valor, "UTF-8");

		} catch (Exception e) {
		}

		return retorno;
	}
}
