package br.com.goals.etrilhas.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.dao.MapaItemDao;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;

/**
 * Servlet implementation class MapaItemDefinirServlet
 */
public class MapaItemDefinirServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapaItemDefinirServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * Verificar se possui um tipo
		 */
		try{
			Long id = Long.parseLong(request.getParameter("id"));
			Mapa mapa = getMapa(request);
			MapaItemDao mapaItemDao = new MapaItemDao();
			MapaItem mapaItem = mapaItemDao.selecionar(mapa, id);
			request.setAttribute("mapaItem",mapaItem);
		}catch(Exception e){
			e.printStackTrace();
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("definirMapaItem.jsp");
		requestDispatcher.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String tipo = request.getParameter("tipo");
			Long id = Long.parseLong(request.getParameter("id"));
			Mapa mapa = getMapa(request);
			MapaItemDao mapaItemDao = new MapaItemDao();
			MapaItem mapaItem = mapaItemDao.selecionar(mapa, id);
			mapaItem.setNome(request.getParameter("nome"));
			mapaItem.setDescricao(request.getParameter("descricao"));
			mapaItem.setIcone(request.getParameter("icone"));
			request.setAttribute("mapaItem",mapaItem);
		}catch(Exception e){
			e.printStackTrace();
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("definirMapaItem.jsp");
		requestDispatcher.forward(request,response);	
	}
}
