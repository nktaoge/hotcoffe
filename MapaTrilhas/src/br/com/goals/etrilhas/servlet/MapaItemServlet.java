package br.com.goals.etrilhas.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.goals.etrilhas.facade.FacadeException;
import br.com.goals.etrilhas.facade.MapaItemFacade;
import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.cafeina.view.tmp.RequestUtil;

/**
 * Servlet implementation class MapaItemServlet
 */
public class MapaItemServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private static MapaItemFacade mapaItemFacade = MapaItemFacade.getInstance();
    private static Logger logger = Logger.getLogger(MapaItemServlet.class);
    public MapaItemServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = "";
		MapaItem mapaItem = new MapaItem();
		logger.debug("doing Post");
		try {
			RequestUtil.requestByAttName(request, mapaItem);
			Camada camada = new Camada();
			camada.setId(Long.valueOf(request.getParameter("id_camada")));
			mapaItem.setCamada(camada);
			if(request.getParameter("id").equals("undefined")
					|| request.getParameter("id").equals("null")){
				mapaItemFacade.criar(mapaItem,getMapa(request));
				msg = "Criado com sucesso!";	
			}else{
				mapaItem.setId(Long.valueOf(request.getParameter("id")));
				mapaItemFacade.atualizar(mapaItem,getMapa(request));
				msg = "Atualizado com sucesso!";
			}
		} catch (FacadeException e) {
			msg = "Erro: " + e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Erro: " + e.getMessage();
		}
		PrintWriter pw = response.getWriter();
		pw.write("<ponto><pid>"+mapaItem.getId()+"</pid><msg>"+msg+"</msg></ponto>");
		pw.close();
	}

}
