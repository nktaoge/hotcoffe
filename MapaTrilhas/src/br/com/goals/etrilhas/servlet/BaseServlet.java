package br.com.goals.etrilhas.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.goals.etrilhas.dao.BaseDao;
import br.com.goals.etrilhas.facade.CamadaFacade;
import br.com.goals.etrilhas.facade.FacadeException;
import br.com.goals.etrilhas.facade.MapaFacade;
import br.com.goals.etrilhas.facade.MapaItemFacade;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.template.Template;

public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BaseServlet.class);
	protected static MapaFacade mapaFacade = MapaFacade.getInstance();
	protected static CamadaFacade camadaFacade = CamadaFacade.getInstance();
	protected static MapaItemFacade mapaItemFacade = MapaItemFacade.getInstance();
    public BaseServlet() {
        super();
    }
    @Override
    public final void init(ServletConfig config) throws ServletException {
    	String chroot = config.getInitParameter("chroot");
    	logger.debug("chroot '" + chroot+"' especifico do servlet");
    	if(chroot==null){
    		chroot = config.getServletContext().getInitParameter("chroot");
    		logger.debug("chroot '" + chroot +"' geral da aplicacao");
    	}
    	if(chroot!=null){
    		logger.debug("Trocando o root para " + chroot);
    		BaseDao.setBasePath(chroot+File.separatorChar+"data"+File.separatorChar);
    	}
    	super.init(config);
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
