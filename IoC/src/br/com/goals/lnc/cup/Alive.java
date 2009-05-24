package br.com.goals.lnc.cup;

import br.com.goals.lnc.bo.Compilador;
import br.com.goals.lnc.cup.pergunta.DescobrirPergunta;
import br.com.goals.lnc.cup.pergunta.ResponderPergunta;
import br.com.goals.lnc.cup.tradutor.CriarCodigo;
import br.com.goals.lnc.vo.Comando;
/**
 * Caso de Uso de Como viver
 * @author fabio
 *
 */
public class Alive extends BaseCup{

	@Override
	protected void iniciar() throws Exception {
		if(Compilador.workspaceSrc==null){
			Compilador.workspaceSrc = getControlador().getInitParameter("workspaceSrc");
		}
		while (true) {
			Comando comando = new Comando();
			ator.preencher(comando);
			DescobrirPergunta descobrirPergunta = new DescobrirPergunta(comando);
			usar(descobrirPergunta);
			if(comando.isQuestion()){
				//responder
				ResponderPergunta responderPergunta = new ResponderPergunta(comando);
				usar(responderPergunta);
			}else{
				//arquivar, pensar, sei lá
				CriarCodigo criarCodigo = new CriarCodigo();
				criarCodigo.setComando(comando);
				usar(criarCodigo);
			}
		}
	}

}
