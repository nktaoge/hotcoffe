package br.com.goals.etrilhas.seguranca;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.jpa4google.PMF;

@SuppressWarnings("unchecked")
public class CriarUsuario extends UsuarioBaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Template template = new Template();
    	template.setTemplateFile("usuario/" + this.getClass().getSimpleName()+".html");
    	
    	if("1".equals(req.getParameter("erro"))){
			template.setMensagem("Preencha os campos.");
		}else if("2".equals(req.getParameter("erro"))){
			template.setMensagem("Usuário existente.");
		}else if("3".equals(req.getParameter("erro"))){
			template.setMensagem("Senha digitada diferente.");
		}else{
			template.setMensagem("");
		}
    	
    	resp.setContentType("text/html");
    	resp.getWriter().write(template.toString());
    	resp.getWriter().close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean existe = false;
		
		//procurar no banco
		PersistenceManager pm = PMF.getPersistenceManager();
		try{
			validar(req);
			String login = req.getParameter("login").replace("%", "").trim();
			String senha = req.getParameter("senha");
			
			Query q = pm.newQuery("SELECT FROM " + Usuario.class.getName() + " WHERE login == fname PARAMETERS String fname");
			List<Usuario> results = (List) q.execute(login);
			for(Usuario usuario:results){
				if(usuario.getSenha().equals(senha)){
					existe=true;
					break;
				}
			}
			if(existe){
				resp.sendRedirect("?erro=2");
				return;
			}
			
			//podemos cadastrar
			Usuario usuario = new Usuario();
			usuario.setLogin(login);
			usuario.setSenha(senha);
			pm.makePersistent(usuario);
		} catch (Exception e) {
			Template template = new Template();
	    	template.setTemplateFile("usuario/" + this.getClass().getSimpleName()+".html");
	    	template.setMensagem("Erro: " + e.getMessage());
	    	resp.setContentType("text/html");
	    	resp.getWriter().write(template.toString());
	    	resp.getWriter().close();
	    	return;
		}finally{
			pm.close();
		}
		resp.sendRedirect("ListarUsuario");
	}

	
}
