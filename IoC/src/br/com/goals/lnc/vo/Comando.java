package br.com.goals.lnc.vo;

public class Comando {
	private String comando;
	private String resposta;
	private boolean isQuestion;
	public void setComando(String comando) {
		this.comando = comando;
	}
	public String getComando() {
		return comando;
	}
	public boolean isQuestion(){
		return isQuestion;
	}
	public void definirComoPergunta(){
		isQuestion = true;
	}
	public void definirResposta(String resposta){
		this.resposta = resposta;
	}
	public String getResposta() {
		return resposta;
	}
}
