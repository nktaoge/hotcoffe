package br.com.goals.etrilhas.servlet.icones;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.cafeina.view.tmp.RsItemCustomizado;
import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.etrilhas.dao.JdoFile;
import br.com.goals.etrilhas.servlet.BaseServletV2;
import br.com.goals.jpa4google.PMF;

public class ListarIcone extends BaseServletV2 {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		PersistenceManager pm = PMF.getPersistenceManager();
    	if(request.getParameter("delId")!=null){
    		try{
    			JdoFile us = pm.getObjectById(JdoFile.class,Long.parseLong(request.getParameter("delId")));
    			if(us!=null){
	    			pm.deletePersistent(us);
	    			template.setMensagem("Apagado.");
    			}else{
    				template.setMensagem("Arquivo inexistente.");
    			}
    		}catch (Exception e) {
    			template.setMensagem("Erro: " + e.getMessage());
			}
    	}
		Query q = pm.newQuery("SELECT FROM " + JdoFile.class.getName() + " where fileName.startsWith(\"media/\")");
		template.setRsItemCustomizado(new RsItemCustomizado(){
			@Override
			public String tratar(Object obj, String item) {
				if(obj instanceof JdoFile){
					JdoFile file=(JdoFile)obj;
					item = item.replace("href=\"#del\"","href=\"?delId="+file.getId()+"\"");
				}
				return item;
			}				
		});
		template.encaixaResultSet((List<JdoFile>)q.execute());
		template.setMensagem("");
		response.setContentType("text/html");
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}
}
