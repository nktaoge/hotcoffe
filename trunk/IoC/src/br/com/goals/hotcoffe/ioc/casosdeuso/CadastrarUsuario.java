package br.com.goals.hotcoffe.ioc.casosdeuso;

import br.com.goals.hotcoffe.ioc.bo.UsuarioBo;
import br.com.goals.hotcoffe.ioc.entidade.Endereco;
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
			if(UsuarioBo.isGay(usuario)){
				System.out.println("hehehe");
			}
			//ator preenche endereco
			Endereco endereco = new Endereco();
			ator.preencher(endereco);
			//verificamos
			
			//UsuarioDao salva
		}catch(Exception e){
			
		}
	}
}
