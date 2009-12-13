package br.com.goals.etrilhas.seguranca;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.jpa4google.PMF;

public class EditarUsuario extends UsuarioBaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		Template template = new Template();
    	template.setTemplateFile("usuario/" + this.getClass().getSimpleName()+".html");
    	PersistenceManager pm = PMF.getPersistenceManager();
    	try{
    		Long id = Long.parseLong(req.getParameter("usuario.id"));
    		Usuario usuario = pm.getObjectById(Usuario.class,id);
    		template.setInput("usuario.id", id);
    		template.setInput("login",usuario.getLogin());
    	}finally{
    		pm.close();
    	}
    	template.setMensagem("");
    	response.getWriter().write(template.toString());
    	response.getWriter().close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		Template template = new Template();
    	template.setTemplateFile("usuario/" + this.getClass().getSimpleName()+".html");
    	PersistenceManager pm = PMF.getPersistenceManager();
    	try{
    		Long id = Long.parseLong(req.getParameter("usuario.id"));
    		template.setInput("usuario.id", id);
    		Usuario usuario = pm.getObjectById(Usuario.class,id);
    		template.setInput("login",usuario.getLogin());
    		validar(req,id);
    		String login = req.getParameter("login").replace("%", "").trim();
			String senha = req.getParameter("senha");
			usuario.setLogin(login);
			usuario.setSenha(senha);
			pm.flush();
    		template.setInput("usuario.id", id);
    		template.setInput("login",usuario.getLogin());
    		template.setMensagem("Atualizado com sucesso.");
    	} catch (Exception e) {
    		template.setTemplateFile("usuario/" + this.getClass().getSimpleName()+".html");
    		template.setInput("usuario.id", req.getParameter("usuario.id"));
    		template.setInput("login",req.getParameter("login"));
	    	template.setMensagem("Erro: " + e.getMessage());
	    	response.setContentType("text/html");
	    	response.getWriter().write(template.toString());
	    	response.getWriter().close();
	    	return;
    	}finally{
    		pm.close();
    	}
    	response.getWriter().write(template.toString());
    	response.getWriter().close();
	}
}
