package br.com.goals.hotcoffe.ioc.casosdeuso;

import br.com.goals.hotcoffe.ioc.entidade.Usuario;

public class CadastrarUsuario extends UmCasoDeUso{

	@Override
	public void iniciar() throws Exception {
		try{
			System.out.println("Caso de uso Cadastrar Usuario Aguardando");
			Usuario usuario = new Usuario();
			ator.preencher(usuario);
			
			System.out.println("Respondido " + usuario.getNome());
			//UsuarioBo valida
	
			//ator preenche endereco
			//verificamos
			
			//UsuarioDao salva
		}catch(Exception e){
			
		}
	}
}
