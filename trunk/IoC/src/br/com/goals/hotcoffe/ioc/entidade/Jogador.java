package br.com.goals.hotcoffe.ioc.entidade;

public class Jogador {
String nascimento,nome,txtResumo,listTime;
public void setListTime(String listTime) {
	this.listTime = listTime;
}
public String getNascimento() {
	return nascimento;
}

public void setNascimento(String nascimento) {
	this.nascimento = nascimento;
}

public String getNome() {
	return nome;
}

public void setNome(String nome) {
	this.nome = nome;
}

public String getTxtResumo() {
	return txtResumo;
}

public void setTxtResumo(String txtResumo) {
	this.txtResumo = txtResumo;
}
}
