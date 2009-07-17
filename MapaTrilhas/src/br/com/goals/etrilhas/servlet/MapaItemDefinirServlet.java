package br.com.goals.etrilhas.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.dao.MapaItemDao;
import br.com.goals.etrilhas.facade.MapaFacade;
import br.com.goals.etrilhas.facade.MapaItemFacade;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.template.Template;
import br.com.goals.utils.RequestUtil;

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
			template.set("id",id);
			template.setInput("nome",mapaItem.getNome());
			template.setInput("icone",mapaItem.getIcone());
			template.setSelect("tipo", mapaItem.getTipo());
			template.setForm("campos do tipo",mapaItem.getValor());
		}catch(Exception e){
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String tipo = request.getParameter("tipo");
			Long id = Long.parseLong(request.getParameter("id"));
			Mapa mapa = getMapa(request);
			MapaItemDao mapaItemDao = new MapaItemDao();
			MapaItem mapaItem = mapaItemDao.selecionar(mapa, id);
			mapaItem.setNome(request.getParameter("nome"));
			mapaItem.setIcone(request.getParameter("icone"));
			
			//Criar um item do tipo
			Object obj = null;
			if(!tipo.equals(mapaItem.getTipo())){
				//O tipo era diferente do antigo
				obj = Class.forName("br.com.goals.etrilhas.modelo."+tipo).newInstance();
				mapaItem.setTipo(tipo);
				mapaItem.setValor(obj);
			}else{
				//O tipo permanesce o mesmo
				RequestUtil.request(request, mapaItem.getValor());
			}
			MapaItemFacade.getInstance().criarHtml(mapaItem);
			MapaFacade.getInstance().atualizar(mapa);
			
			//Montar resposta para o usuario
			Template template = new Template();
			template.setTemplateFile("definirMapaItem.html");
			template.set("id",id);
			template.setInput("nome",mapaItem.getNome());
			template.setInput("icone",mapaItem.getIcone());
			template.setSelect("tipo",mapaItem.getTipo());
			template.setForm("campos do tipo",mapaItem.getValor());
			
			response.getWriter().write(template.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
}
