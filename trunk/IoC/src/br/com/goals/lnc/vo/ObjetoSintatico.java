package br.com.goals.lnc.vo;

import java.util.ArrayList;
import java.util.List;

public class ObjetoSintatico {
	private List<UmaPalavra> palavras = new ArrayList<UmaPalavra>();
	public List<UmaPalavra> getPalavras() {
		return palavras;
	}
	public void setPalavras(List<UmaPalavra> palavras) {
		this.palavras = palavras;
	}
	@Override
	public String toString() {
		String retorno = "";
		for(UmaPalavra palavra: palavras){
			retorno +=palavra.getEscrita() + ",";
		}
		return retorno;
	}
}
