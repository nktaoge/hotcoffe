package br.com.goals.etrilhas.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.cafeina.view.tmp.Template;

public class TrilhaEditar extends BaseServlet{
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		Mapa mapa = getMapa(request);
		template.setInput("nome",mapa.getNome());
		template.setTextArea("txtCoordenadasLatLng",mapa.getTxtCoordenadasLatLng());
		template.setMensagem("");
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Mapa mapa = getMapa(request);
		mapa.setNome(request.getParameter("nome"));
		mapa.setTxtCoordenadasLatLng(request.getParameter("txtCoordenadasLatLng"));
		Template template = getTemplate(request);
		template.setInput("nome",mapa.getNome());
		template.setTextArea("txtCoordenadasLatLng",mapa.getTxtCoordenadasLatLng());
		try{
			mapaFacade.atualizar(mapa);
		}catch (Exception e) {
			template.setMensagem("Erro: " + e.getMessage());
		}
		
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}
}
