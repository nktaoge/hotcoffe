package br.com.goals.hotcoffe.ioc.casosdeuso.util;

import java.util.ArrayList;
import java.util.List;

public class Opcoes {
	private ArrayList<String> list = new ArrayList<String>();
	private String escolha = null;
	public void add(String string) {
		list.add(string);
	}
	public void setEscolha(String escolha) {
		this.escolha = escolha;
	}
	public String getEscolha() {
		return escolha;
	}
	public List<String> getList() {
		return list;
	}

}
