package br.com.goals.etrilhas.servlet.camada;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.servlet.BaseServlet;
import br.com.goals.template.Template;
import br.com.goals.utils.RequestUtil;

public class CamadaCriar extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    public CamadaCriar() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		try{
			Camada camada = new Camada();
			template.setForm("camada",camada);
			template.setMensagem("");
		}catch(Exception e){
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		try{
			Camada camada = new Camada();
			RequestUtil.request(request, camada);
			camadaFacade.criar(camada,getMapa(request));
			template.setForm("camada",camada);
			template.setMensagem("Camada criada com sucesso!");
		}catch(Exception e){
			template.setMensagem("Erro: " + e.getMessage());
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}

}
