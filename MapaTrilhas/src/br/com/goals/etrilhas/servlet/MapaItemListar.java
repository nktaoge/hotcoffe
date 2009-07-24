package br.com.goals.etrilhas.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.template.RsItemCustomizado;
import br.com.goals.template.Template;

/**
 * Servlet implementation class MapaItemListar
 */
public class MapaItemListar extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
	public Template getTemplate(HttpServletRequest request) throws IOException{
    	Template template = super.getTemplate(request);
		template.setRsItemCustomizado(new RsItemCustomizado(){
			public String tratar(Object o, String item) {
				if(o instanceof MapaItem){
					MapaItem mapaItem = (MapaItem) o;
					Template temp = new Template();
					temp.setTemplate(item);
					temp.setLink("editar", "definirMapaItem?id=" + mapaItem.getId());
					temp.setLink("apagar", "MapaItemApagar?id=" + mapaItem.getId());
					return temp.toString();
				}else{
					return item;
				}
			}		
		});
		return template;
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		try{
			Long camadaId = Long.parseLong(request.getParameter("camada.id"));
			template.setInput("camada.id", camadaId);
			Mapa mapa = getMapa(request);
			List<MapaItem> listItem = mapaItemFacade.listar(mapa,camadaId);
			template.encaixaResultSet(listItem);
			template.setMensagem("");
		}catch(Exception e){
			template.setMensagem("Erro: " + e.getMessage());
		}
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		try{
			Long camadaId = Long.parseLong(request.getParameter("camada.id"));
			Mapa mapa = getMapa(request);
			List<MapaItem> listItem = mapaItemFacade.listar(mapa,camadaId);
			for(MapaItem mapaItem: listItem){
				try{
					int ordem = Integer.parseInt(request.getParameter("MapaItem_"+mapaItem.getId()+".ordem"));
					mapaItem.setOrdem(ordem);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			mapaFacade.atualizar(mapa);
			template.setInput("camada.id", camadaId);
			template.encaixaResultSet(listItem);
			template.setMensagem("Itens ordenados");
		}catch(Exception e){
			e.printStackTrace();
			template.setMensagem("Erro: " + e.getMessage());
		}
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}

}
