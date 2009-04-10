package br.com.goals.hotcoffe.ioc.entidade;

public class Time {
	private String nome;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getFundacao() {
		return fundacao;
	}
	public void setFundacao(String fundacao) {
		this.fundacao = fundacao;
	}
	public String getTxtResumo() {
		return txtResumo;
	}
	public void setTxtResumo(String txtResumo) {
		this.txtResumo = txtResumo;
	}
	private String fundacao,txtResumo;
}
