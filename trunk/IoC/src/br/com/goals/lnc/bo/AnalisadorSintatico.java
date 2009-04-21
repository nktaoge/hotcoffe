package br.com.goals.lnc.bo;

import java.util.List;

import br.com.goals.lnc.vo.FraseSintatica;
import br.com.goals.lnc.vo.UmaPalavra;

public interface AnalisadorSintatico {
	public FraseSintatica analisar(List<UmaPalavra> list) throws Exception;
}
