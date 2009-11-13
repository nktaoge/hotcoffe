package br.com.goals.etrilhas.servlet.galeria;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.goals.etrilhas.facade.GaleriaFacade;
import br.com.goals.etrilhas.modelo.Foto;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.etrilhas.servlet.BaseServlet;
import br.com.goals.cafeina.view.tmp.AreaNaoEncontradaException;
import br.com.goals.cafeina.view.tmp.RsItemCustomizado;
import br.com.goals.cafeina.view.tmp.Template;

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

    @Override
    public Template getTemplate(HttpServletRequest request) throws IOException {
    	Template template = super.getTemplate(request);
    	try {
			template.setArea("id",request.getParameter("galeriaId"));
		} catch (AreaNaoEncontradaException e) {
			e.printStackTrace();
		}
    	return template;
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		try{
			final Long galeriaId = Long.parseLong(request.getParameter("galeriaId"));
			template.setInput("galeriaId", galeriaId);
			Galeria galeria = galeriaFacade.selecionar(galeriaId);
			logger.debug("galeria.getFotos().size() = " + galeria.getFotos().size());
			if(request.getParameter("delId")!=null){
				galeriaFacade.apagarFoto(galeria,request.getParameter("delId"));
				template.setMensagem("Foto apagada");
			}else{
				template.setMensagem("");
			}
			template.setRsItemCustomizado(new RsItemCustomizado(){
				@Override
				public String tratar(Object obj, String item) {
					if(obj instanceof Foto){
						Foto foto=(Foto)obj;
						item = item.replace("href=\"#del\"","href=\"?galeriaId="+galeriaId+"&delId="+foto.getId()+"\"");
					}
					return item;
				}				
			});
			template.encaixaResultSet(galeria.getFotos());
		}catch(Exception e){
			logger.error("Erro inesperado ",e);
			template.setMensagem("Erro: " + e.getMessage());
		}
		response.getWriter().write(template.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
