package br.com.goals.ui;

public class Interpretador {
	public static String acharNomeClasse(String texto){
		String arrTexto[] = texto.toLowerCase().split(" ");
		String resposta = "";
		for (int i = 0; i < arrTexto.length; i++) {
			resposta+=arrTexto[i].substring(0,1).toUpperCase() + arrTexto[i].substring(1);
		}
		return resposta.trim();
	}
}
