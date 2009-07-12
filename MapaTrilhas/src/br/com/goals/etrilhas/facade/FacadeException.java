package br.com.goals.etrilhas.facade;

import java.util.ArrayList;
import java.util.List;

public class FacadeException extends Exception{
	private static final long serialVersionUID = -1988688259819260084L;
	private List<String> erros = new ArrayList<String>();
	public FacadeException(String string) {
		super(string);
		getErros().add(string);
	}
	public List<String> getErros() {
		return erros;
	}
	public void setErros(List<String> erros) {
		this.erros = erros;
	}
}
