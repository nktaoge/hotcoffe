package br.quickstart.casosdeuso;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
import br.com.goals.hotcoffe.ioc.entidade.Arbitro;
import br.com.goals.hotcoffe.ioc.entidade.Jogador;
import br.com.goals.hotcoffe.ioc.entidade.Tecnico;
import br.com.goals.hotcoffe.ioc.entidade.OcorrenciaLance;
import br.com.goals.hotcoffe.ioc.entidade.PublicoRenda;

public class CadastrarProjSoft extends UmCasoDeUso{
	private static Logger logger = Logger.getLogger(CadastrarProjSoft.class);
	@Override
	public void iniciar() throws Exception {
		try{
			ator.preencher(new Jogador());
		}catch(Exception e){

		}
	}
}
