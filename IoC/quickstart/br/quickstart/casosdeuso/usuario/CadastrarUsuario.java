package br.quickstart.casosdeuso.usuario;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
import br.quickstart.bo.UsuarioBo;
import br.quickstart.entidade.Dependente;
import br.quickstart.entidade.Endereco;
import br.quickstart.entidade.Sapato;
import br.quickstart.entidade.Usuario;

public class CadastrarUsuario extends UmCasoDeUso{
	private static Logger logger = Logger.getLogger(CadastrarUsuario.class);
	@Override
	public void iniciar() throws Exception {
		try{
			Usuario usuario = new Usuario();
			logger.debug("Caso de uso Cadastrar Usuario Aguardando");

			//Chama o ator e espera
			ator.preencher(usuario);
			
			logger.debug("Ator respondeu '" + usuario.getNome()+"'");
			//UsuarioBo valida
			if(UsuarioBo.isGay(usuario)){
				Sapato sapato = new Sapato();
				ator.preencher(sapato);
			}
			
			Dependente dependente = new Dependente();
			ator.preencher(dependente);
			
			//ator preenche endereco
			Endereco endereco = new Endereco();
			ator.preencher(endereco);
			//verificamos
		
			//UsuarioDao salva
			logger.debug("gravado '" + usuario.getNome()+"'");
			sistema.mostrar("Obrigado " + usuario.getNome());
		}catch(Exception e){
			logger.error("Erro ao cadastrar usuario",e);
		}
	}
	
}
