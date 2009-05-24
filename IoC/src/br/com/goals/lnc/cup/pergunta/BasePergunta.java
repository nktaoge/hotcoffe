package br.com.goals.lnc.cup.pergunta;

import br.com.goals.lnc.cup.BaseCup;
import br.com.goals.lnc.vo.Comando;

public abstract class BasePergunta extends BaseCup{
	protected Comando comando;
	public BasePergunta(Comando comando){
		this.comando = comando;
	}
	public void setComando(Comando comando) {
		this.comando = comando;
	}
	public Comando getComando() {
		return comando;
	}
}
