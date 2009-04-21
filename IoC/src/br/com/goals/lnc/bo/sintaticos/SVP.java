package br.com.goals.lnc.bo.sintaticos;

import java.util.List;

import br.com.goals.lnc.bo.AnalisadorSintatico;
import br.com.goals.lnc.vo.ClasseGramatical;
import br.com.goals.lnc.vo.FraseSintatica;
import br.com.goals.lnc.vo.Predicado;
import br.com.goals.lnc.vo.Sujeito;
import br.com.goals.lnc.vo.UmaPalavra;
import br.com.goals.lnc.vo.Verbo;

public class SVP implements AnalisadorSintatico{

	@Override
	public FraseSintatica analisar(List<UmaPalavra> list) {
		FraseSintatica frase = new FraseSintatica();
		Sujeito sujeito = new Sujeito();
		Verbo verbo = new Verbo();
		Predicado predicado = new Predicado();
		int estado = 0;
		for (int i = 0; i < list.size(); i++) {
			UmaPalavra palavra = list.get(i);
			for(String str:palavra.getPodeSerClasseGramatical()){
				if(str.equals(ClasseGramatical.UM_VERBO)){
					estado = 1;
				}else{
					if(estado==1){
						estado=2;
					}
				}
			}
			if(estado==0){
				sujeito.getPalavras().add(palavra);
			}else if(estado==1){
				verbo.getPalavras().add(palavra);
			}else if(estado==2){
				predicado.getPalavras().add(palavra);
			}
		}
		frase.setPredicado(predicado);
		frase.setSujeito(sujeito);
		frase.setVerbo(verbo);
		return frase;
	}

}
