package br.com.goals.lnc.vo;

public class TestMetodo {
	public static void main(String[] args) {
		Metodo metodo = new Metodo();
		metodo.setNome("testar");
		metodo.setComentario("meu comentario");
		metodo.setRetorno("boolean");
		metodo.setBody("return true;");
		System.out.println(metodo);
	}
}
