package br.com.goals.lnc.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.goals.hotcoffe.ioc.QuickStart;
import br.com.goals.lnc.vo.FraseSintatica;
import br.com.goals.lnc.vo.UmaPalavra;

public class AnalisadorSintaticoFactory {
	private Comparator<FraseSintatica> comparator = new Comparator<FraseSintatica>(){
		public int compare(FraseSintatica a, FraseSintatica b) {
			return a.getCerteza()-b.getCerteza();
		}		
	};
	@SuppressWarnings("unchecked")
	public FraseSintatica analisar(List<UmaPalavra> list){
		List<FraseSintatica> retorno = new ArrayList<FraseSintatica>();
		try {
			List<Class> listAnalisadores = QuickStart.getClasses("br.com.goals.lnc.bo.sintaticos");
			for (Class class1 : listAnalisadores) {
				try {
					AnalisadorSintatico analisador = (AnalisadorSintatico)class1.newInstance();
					retorno.add(analisador.analisar(list));
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Collections.sort(retorno, comparator);
		return retorno.get(0);
	}
}
