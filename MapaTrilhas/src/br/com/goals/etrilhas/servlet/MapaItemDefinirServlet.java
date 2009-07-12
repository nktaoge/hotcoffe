package br.com.goals.etrilhas.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.dao.MapaItemDao;
import br.com.goals.etrilhas.facade.MapaFacade;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.template.Template;

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
		Template template = new Template();
		template.setTemplateFile("definirMapaItem.html");
		/*
		 * Verificar se possui um tipo
		 */
		try{
			Long id = Long.parseLong(request.getParameter("id"));
			Mapa mapa = getMapa(request);
			MapaItemDao mapaItemDao = new MapaItemDao();
			MapaItem mapaItem = mapaItemDao.selecionar(mapa, id);
			//request.setAttribute("mapaItem",mapaItem);
			template.set("id",id);
			template.setInput("nome",mapaItem.getNome());
			template.setTextArea("descricao",mapaItem.getDescricao());
			template.setInput("icone",mapaItem.getIcone());
			template.setSelect("tipo", "Video");
		}catch(Exception e){
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
		//RequestDispatcher requestDispatcher = request.getRequestDispatcher("definirMapaItem.jsp");
		//requestDispatcher.forward(request,response);
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
			MapaFacade.getInstance().atualizar(mapa);
			Template template = new Template();
			template.setTemplateFile("definirMapaItem.html");
			template.set("id",id);
			template.setInput("nome",mapaItem.getNome());
			template.setTextArea("descricao",mapaItem.getDescricao());
			template.setInput("icone",mapaItem.getIcone());
			template.setSelect("tipo", "Video");
			response.getWriter().write(template.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
}
