package br.com.goals.lnc.vo;

import java.util.ArrayList;
import java.util.List;

public class UmaPalavra {
	public String getEscrita() {
		return null;
	}
	/**
	 * metodo = verbo
	 * adjetivo = atributo?
	 * classe = substantivo?
	 */
	private List<String> podeSerClasseGramatical = new ArrayList<String>();
	public List<String> getPodeSerClasseGramatical() {
		return podeSerClasseGramatical;
	}
	
	
	
	/**
	 * Nomes das classes significado em sig
	 */
	private List<String> podeSignificar = new ArrayList<String>();
	public List<String> getSignificados() {
		return podeSignificar;
	}
}
