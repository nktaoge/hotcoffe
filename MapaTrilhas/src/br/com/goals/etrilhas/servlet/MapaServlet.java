package br.com.goals.etrilhas.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;

/**
 * Servlet implementation class MapaServlet
 */
public class MapaServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapaServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("MapaServlet.doGet()");
		String msg="";
		Mapa mapa = new Mapa();
		try {
			mapa = mapaFacade.selecionar(1L);
			msg="Mapa carregado com sucesso!";
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		setMapa(request,mapa);
		String retorno = "<mapa>";
		for(Camada camada:mapa.getCamadas()){
			retorno +="<camada pid=\""+camada.getId()+"\" nome=\""+camada.getNome()+"\">";
			for(MapaItem mapaItem:camada.getItems()){
				if(mapaItem.getIcone()==null){
					mapaItem.setIcone("default.png");
				}
				retorno+="<mapaItem nome=\""+mapaItem.getNome()+"\" icone=\""+mapaItem.getIcone()+"\" tipo=\""+mapaItem.getTipo()+"\" pid=\""+mapaItem.getId()+"\" x=\""+mapaItem.getX()+"\" y=\"" + mapaItem.getY() + "\" />";
			}
			retorno +="</camada>";
		}
		retorno+="<msg>"+msg+"</msg>";
		retorno+="</mapa>";
		System.out.println(retorno);
		response.getWriter().write(retorno);
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
