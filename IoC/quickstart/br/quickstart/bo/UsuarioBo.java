package br.quickstart.bo;

import br.quickstart.entidade.Usuario;

public class UsuarioBo {
	public static boolean isGay(Usuario usuario) {
		if(usuario.getNome().equals("Clecle")){
			return true;
		}
		return false;
	}
}
