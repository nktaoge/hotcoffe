package br.com.goals.etrilhas.servlet.camada;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.servlet.BaseServlet;
import br.com.goals.template.Template;
import br.com.goals.utils.RequestUtil;

public class CamadaEditar extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    public CamadaEditar() {
        super();
    }

    private Template getTemplate() throws IOException{
    	Template template = new Template();
    	template.setTemplateFile("camadaEditar.html");
    	return template;
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate();
		try{
			Long id = Long.parseLong(request.getParameter("id"));
			Camada camada = camadaFacade.selecionar(getMapa(request),id);
			template.setForm("camada",camada);
			template.setArea("mensagem", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate();
		try{
			Mapa mapa = getMapa(request);
			Long id = Long.parseLong(request.getParameter("Camada.id"));
			Camada camada = camadaFacade.selecionar(mapa,id);
			RequestUtil.request(request, camada);
			template.setForm("camada",camada);
			
			mapaFacade.atualizar(mapa);
			
			template.setArea("mensagem", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}

}
