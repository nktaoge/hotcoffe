package br.com.goals.etrilhas.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import br.com.goals.etrilhas.dao.CamadaDao;
import br.com.goals.etrilhas.modelo.Mapa;

public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected CamadaDao camadaDao = new CamadaDao();
	
    public BaseServlet() {
        super();
    }
    public void setMapa(HttpServletRequest request,Mapa mapa) {
    	request.getSession().setAttribute("mapa",mapa);
	}
    public Mapa getMapa(HttpServletRequest request){
    	return (Mapa)request.getSession().getAttribute("mapa");
    }
}
