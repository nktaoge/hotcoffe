package br.com.goals.etrilhas.servlet.camada;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.servlet.BaseServlet;

public class CamadaApagar extends BaseServlet{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		Mapa mapa = getMapa(request);
		try {
			camadaFacade.apagar(mapa,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("camadaListar")
			.forward(request, response);
	}
}
