package br.com.goals.etrilhas.seguranca;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.cafeina.view.tmp.RsItemCustomizado;
import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.jpa4google.PMF;

@SuppressWarnings("unchecked")
public class ListarUsuario extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Template template = new Template();
    	template.setTemplateFile("usuario/" + this.getClass().getSimpleName()+".html");
    	
    	
    	PersistenceManager pm = PMF.getPersistenceManager();
    	if(req.getParameter("delId")!=null){
    		try{
    			Usuario us = pm.getObjectById(Usuario.class,Long.parseLong(req.getParameter("delId")));
    			if(us!=null){
	    			pm.deletePersistent(us);
	    			template.setMensagem("Apagado.");
    			}else{
    				template.setMensagem("Usu&aacute;rio inexistente.");
    			}
    		}catch (Exception e) {
    			template.setMensagem("Erro: " + e.getMessage());
			}
    	}
		Query q = pm.newQuery("SELECT FROM " + Usuario.class.getName());
		template.setRsItemCustomizado(new RsItemCustomizado(){
			@Override
			public String tratar(Object obj, String item) {
				if(obj instanceof Usuario){
					Usuario user=(Usuario)obj;
					item = item.replace("href=\"#del\"","href=\"?delId="+user.getId()+"\"");
					item = item.replace("href=\"#upd\"", "href=\"EditarUsuario?usuario.id="+user.getId()+"\"");
				}
				return item;
			}				
		});
		template.encaixaResultSet((List<Usuario>)q.execute());
		template.setMensagem("");
    	resp.setContentType("text/html");
    	resp.getWriter().write(template.toString());
    	resp.getWriter().close();
	}
	
}
