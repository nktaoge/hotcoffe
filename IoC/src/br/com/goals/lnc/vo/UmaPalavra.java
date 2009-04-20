package br.com.goals.lnc.vo;

import java.util.ArrayList;
import java.util.List;

public class UmaPalavra {
	/**
	 * metodo = verbo
	 * adjetivo = atributo?
	 * classe = substantivo?
	 */
	private List<String> classeGramatical = new ArrayList<String>();
	public List<String> getClasseGramatical() {
		return classeGramatical;
	}
}
