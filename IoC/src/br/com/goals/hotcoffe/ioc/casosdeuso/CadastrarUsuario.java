package br.com.goals.hotcoffe.ioc.casosdeuso;

import java.io.IOException;

import br.com.goals.hotcoffe.ioc.entidade.Usuario;

public class CadastrarUsuario extends UmCasoDeUso{

	@Override
	public void iniciar() throws IOException {
		System.out.println("Caso de uso Cadastrar Usuario Aguardando");
		Usuario usuario = new Usuario();
		ator.preencher(usuario);
		System.out.println("Respondido " + usuario.getNome());
	}
}
