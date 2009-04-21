package br.com.goals.lnc.bo;

import java.util.List;

import br.com.goals.hotcoffe.ioc.QuickStart;
import br.com.goals.lnc.vo.FraseSintatica;
import br.com.goals.lnc.vo.UmaPalavra;

public class AnalisadorSintaticoFactory {
	public FraseSintatica analisar(List<UmaPalavra> list){
		FraseSintatica retorno = null;
		try {
			List<Class> listAnalisadores = QuickStart.getClasses("br.com.goals.lnc.bo.sintaticos");
			for (Class class1 : listAnalisadores) {
				try {
					AnalisadorSintatico analisador = (AnalisadorSintatico)class1.newInstance();
					retorno = analisador.analisar(list);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return retorno;
	}
}
