package br.com.goals.hotcoffe.ioc.bo;

import br.com.goals.hotcoffe.ioc.entidade.Usuario;

public class UsuarioBo {
	public static boolean isGay(Usuario usuario) {
		if(usuario.getNome().equals("Clecle")){
			return true;
		}
		return false;
	}
}
