package br.com.goals.etrilhas.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.cafeina.view.tmp.Template;


/**
 * Base 
 */
public abstract class BaseServletV2 extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public Template getTemplate(HttpServletRequest request) throws IOException{
    	Template template = new Template();
    	String packageName = this.getClass().getPackage().getName();
    	int i = packageName.indexOf("servlet");
    	if(i!=-1){
    		template.setTemplateFile(
    			//Pasta dentro dos templates
    			packageName.substring(i+7).replace('.',File.separatorChar)
    			+File.separatorChar+
    			this.getClass().getSimpleName()+".html");
    	}else{
    		template.setTemplateFile(this.getClass().getSimpleName()+".html");
    	}
    	try{
    		Long id = getCurrentMapaItemId(request);
    		if(id!=null){
    			template.setArea("menu",
    					"<a href=\"definirMapaItem?id="+id+"\">Voltar a editar o item do mapa</a>"
    			);	
    		}else{
    			template.retirarArea("menu");
    		}
    		
    	}catch(Exception e){
    		
    	}
    	return template;
    }
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		template.setMensagem("");
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}
}
