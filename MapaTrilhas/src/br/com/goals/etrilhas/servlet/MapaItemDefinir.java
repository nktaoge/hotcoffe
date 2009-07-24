package br.com.goals.etrilhas.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.dao.MapaItemDao;
import br.com.goals.etrilhas.facade.MapaFacade;
import br.com.goals.etrilhas.facade.MapaItemFacade;
import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.template.Template;
import br.com.goals.utils.RequestUtil;

/**
 * Servlet implementation class MapaItemDefinirServlet
 */
public class MapaItemDefinir extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapaItemDefinir() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long id = null;
		Template template = null;
		try{
			id = Long.parseLong(request.getParameter("id"));
			super.setCurrentMapaItemId(request, id);
		}catch(Exception e){
			e.printStackTrace();
		}
		template = getTemplate(request);
		/*
		 * Verificar se possui um tipo
		 */
		try{
			Mapa mapa = getMapa(request);
			MapaItemDao mapaItemDao = new MapaItemDao();
			MapaItem mapaItem = mapaItemDao.selecionar(mapa, id);
			template.set("id",id);
			template.setInput("nome",mapaItem.getNome());
			template.setInput("icone",mapaItem.getIcone());
			template.setSelect("tipo", mapaItem.getTipo());
			template.setForm("campos do tipo",mapaItem.getValor());
			template.setRadio("camadas",mapa.getCamadas(),mapaItem.getCamada().getId());
			template.setMensagem("");
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
			//Verificar se colocou em outra camada
			Long camadaId = Long.parseLong(request.getParameter("Camada.id"));
			if(!camadaId.equals(mapaItem.getCamada().getId())){
				//trocar de camada
				Camada camada = camadaFacade.selecionar(mapa,camadaId);
				//retira da camada antiga
				mapaItem.getCamada().getItems().remove(mapaItem);
				mapaItem.setCamada(camada);
				if(camada.getItems()==null){
					camada.setItems(new ArrayList<MapaItem>());
				}
				camada.getItems().add(mapaItem);
			}
			//Montar resposta para o usuario
			Template template = getTemplate(request);
			template.set("id",id);
			template.setInput("nome",mapaItem.getNome());
			template.setInput("icone",mapaItem.getIcone());
			template.setSelect("tipo",mapaItem.getTipo());
			template.setForm("campos do tipo",mapaItem.getValor());
			template.setRadio("camadas",mapa.getCamadas(),mapaItem.getCamada().getId());
			template.setMensagem("Alterado com sucesso");
			response.getWriter().write(template.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
}
