package br.quickstart.entidade;

public class Dependente extends Usuario{
	private String parentesco;
	public String getParentesco() {
		return parentesco;
	}
	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}
}
