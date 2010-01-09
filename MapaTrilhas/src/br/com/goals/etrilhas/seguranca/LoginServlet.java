package br.com.goals.etrilhas.seguranca;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.jpa4google.PMF;

@SuppressWarnings("unchecked")
public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Template template = new Template();
    	template.setTemplateFile(this.getClass().getSimpleName()+".html");
    	
    	if("1".equals(req.getParameter("erro"))){
			template.setMensagem("Preencha os campos.");
		}else if("2".equals(req.getParameter("erro"))){
			template.setMensagem("Login ou senha inválida.");
		}else{
			template.setMensagem("");
		}
    	
    	resp.setContentType("text/html");
    	resp.getWriter().write(template.toString());
    	resp.getWriter().close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean ok = false;
		if(req.getParameter("login")==null || req.getParameter("senha")==null){
			resp.sendRedirect("/login?erro=1");
			return;
		}
		String login = req.getParameter("login").replace("%", "").trim();
		String senha = req.getParameter("senha");
		if(login.equals("") || senha.equals("")){
			resp.sendRedirect("/login?erro=1");
			return;
		}
		if(login.equals("admin") 
				&& senha.equals("girafa")){
			//chave mestre
			ok=true;
		}else{
			//procurar no banco
			PersistenceManager pm = PMF.getPersistenceManager();
			Query q = pm.newQuery("SELECT FROM " + Usuario.class.getName() + " WHERE login == fname PARAMETERS String fname");
			List<Usuario> results = (List) q.execute(login);
			for(Usuario usuario:results){
				if(usuario.getSenha().equals(senha)){
					ok=true;
				}
			}
		}
		
		if(ok){
			req.getSession().setAttribute(LoginInterceptor.LOGIN_KEY, "true");
			resp.sendRedirect("admin/TrilhaEditar");
		}else{
			resp.sendRedirect("/login?erro=2");
		}
	}
}
