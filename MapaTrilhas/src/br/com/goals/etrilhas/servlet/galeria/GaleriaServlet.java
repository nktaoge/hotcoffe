package br.com.goals.etrilhas.servlet.galeria;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.goals.etrilhas.facade.GaleriaFacade;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.etrilhas.servlet.BaseServlet;
import br.com.goals.template.Template;

/**
 * Servlet implementation class GaleriaServlet
 */
public class GaleriaServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private static Logger logger  = Logger.getLogger(GaleriaServlet.class);
	private static GaleriaFacade galeriaFacade = GaleriaFacade.getInstance();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GaleriaServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		try{
			Long galeriaId = Long.parseLong(request.getParameter("galeriaId"));
			Galeria galeria = galeriaFacade.selecionar(galeriaId);
			template.encaixaResultSet(galeria.getFotos());
		}catch(Exception e){
			logger.error("Erro inesperado ",e);
			template.setMensagem(e.getMessage());
		}
		response.getWriter().write(template.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
