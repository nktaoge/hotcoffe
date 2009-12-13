package br.com.goals.etrilhas.seguranca;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import br.com.goals.jpa4google.PMF;

public abstract class UsuarioBaseServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void validar(HttpServletRequest req) throws Exception {
		validar(req, null);
	}
	@SuppressWarnings("unchecked")
	protected void validar(HttpServletRequest req, Long id) throws Exception{
		if(req.getParameter("login")==null
				|| req.getParameter("senha")==null
				|| req.getParameter("senha2")==null){
			throw new Exception("Preencha os campos");
		}
		String login = req.getParameter("login");
		if(login.indexOf("%")!=-1){
			throw new Exception("Caracter '%' desabilitado para o campo login.");
		}
		String senha = req.getParameter("senha");
		String senha2 = req.getParameter("senha2");
		if(login.equals("") || senha.equals("") || senha2.equals("")){
			throw new Exception("Preencha os campos");
		}
		if(!senha.equals(senha2)){
			throw new Exception("A confirmação de senha não é igual.");
		}
		PersistenceManager pm = PMF.getPersistenceManager();
		try{
			Query q = pm.newQuery("SELECT FROM " + Usuario.class.getName() + " WHERE login == fname PARAMETERS String fname");
			List<Usuario> results = (List) q.execute(login);
			for(Usuario usuario:results){
				if(usuario.getLogin().equals(login) && !usuario.getId().equals(id)){
					throw new Exception("O login "+login+" existe. Por favor, escolha outro.");
				}
			}
		}finally{
			pm.close();
		}
	}
}
