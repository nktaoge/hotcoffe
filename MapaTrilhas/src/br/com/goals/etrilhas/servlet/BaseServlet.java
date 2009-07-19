package br.com.goals.etrilhas.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import br.com.goals.etrilhas.dao.CamadaDao;
import br.com.goals.etrilhas.facade.CamadaFacade;
import br.com.goals.etrilhas.facade.FacadeException;
import br.com.goals.etrilhas.facade.MapaFacade;
import br.com.goals.etrilhas.modelo.Mapa;

public class BaseServlet extends HttpServlet {
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
}
