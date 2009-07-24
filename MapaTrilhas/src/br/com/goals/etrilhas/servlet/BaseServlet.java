package br.com.goals.etrilhas.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.facade.CamadaFacade;
import br.com.goals.etrilhas.facade.FacadeException;
import br.com.goals.etrilhas.facade.MapaFacade;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.template.Template;

public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static MapaFacade mapaFacade = MapaFacade.getInstance();
	protected static CamadaFacade camadaFacade = CamadaFacade.getInstance();
	
    public BaseServlet() {
        super();
    }
    public void setMapa(HttpServletRequest request,Mapa mapa) {
    	request.getSession().setAttribute("mapa",mapa);
	}
    public Mapa getMapa(HttpServletRequest request){
    	Mapa mapa = (Mapa)request.getSession().getAttribute("mapa");
    	if(mapa==null){
    		try {
				mapa = mapaFacade.selecionar(1L);
			} catch (FacadeException e) {
				e.printStackTrace();
			}
    	}
    	return mapa; 
    }
    protected void setCurrentMapaItemId(HttpServletRequest request,Long id){
    	request.getSession().setAttribute("CurrentMapaItemId", id);
    }
    private Long getCurrentMapaItemId(HttpServletRequest request){
    	Object obj = request.getSession().getAttribute("CurrentMapaItemId");
    	if(obj!=null){
    		return Long.parseLong(obj.toString());
    	}
    	return null;
    }
    public Template getTemplate(HttpServletRequest request) throws IOException{
    	Template template = new Template();
    	template.setTemplateFile(this.getClass().getSimpleName()+".html");
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
}
