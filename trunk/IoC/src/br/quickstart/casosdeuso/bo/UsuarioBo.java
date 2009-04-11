package br.quickstart.casosdeuso.bo;

import br.quickstart.casosdeuso.entidade.Usuario;

public class UsuarioBo {
	public static boolean isGay(Usuario usuario) {
		if(usuario.getNome().equals("Clecle")){
			return true;
		}
		return false;
	}
}
