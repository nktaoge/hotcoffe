package br.com.goals.lnc.cup.pergunta;

import br.com.goals.lnc.vo.Comando;


/**
 * Responsavel por dizer se o comando é uma pergunta
 * @author fabio
 *
 */
public class DescobrirPergunta extends BasePergunta{

	
	public DescobrirPergunta(Comando comando) {
		super(comando);
	}

	@Override
	protected void iniciar() throws Exception {
		if(comando.getComando().endsWith("?")){
			comando.definirComoPergunta();
		}
	}

}
