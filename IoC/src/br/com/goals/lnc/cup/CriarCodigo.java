package br.com.goals.lnc.cup;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;

public class CriarCodigo extends UmCasoDeUso{

	@Override
	protected void iniciar() throws Exception {
		CriarToken criarToken = new CriarToken();
		criarToken.setDuvida("Fábio");
		usar(criarToken);
	}
}
