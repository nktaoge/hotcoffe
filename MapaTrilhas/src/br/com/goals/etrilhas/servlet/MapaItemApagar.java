package br.com.goals.etrilhas.servlet;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;

/**
 * Servlet implementation class MapaItemApagar
 */
public class MapaItemApagar extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapaItemApagar() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		Mapa mapa = getMapa(request);
		String camadaId=request.getParameter("camada.id");
		
		try {
			mapaItemFacade.apagar(mapa,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(camadaId==null){
			request.getRequestDispatcher("CamadaListar")
				.forward(request, response);
		}else{
			response.sendRedirect("MapaItemListar?camada.id="+camadaId);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
