package br.com.goals.grafo.controle.verbo;

import java.util.List;

import br.com.goals.grafo.CAL;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

public abstract class SuperVerbo {
	protected CAL cal;
	protected PontoDao pontoDao = PontoDao.getInstance();
	public abstract List<Ponto> executar(Ponto sujeito,Ponto predicado);
	public CAL getCal() {
		return cal;
	}
	public void setCal(CAL cal) {
		this.cal = cal;
	}
}
