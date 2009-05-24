package br.com.goals.lnc.cup;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
import br.com.goals.lnc.bo.Compilador;
import br.com.goals.lnc.vo.Comando;

public class Alive extends UmCasoDeUso{

	@Override
	protected void iniciar() throws Exception {
		if(Compilador.workspaceSrc==null){
			Compilador.workspaceSrc = getControlador().getInitParameter("workspaceSrc");
		}
		while (true) {
			Comando comando= new Comando();
			ator.preencher(comando);
			if(comando.getComando().endsWith("?")){
				
			}else{
				
			}
			sistema.mostrar("Comando processado :-)");
		}
	}

}
