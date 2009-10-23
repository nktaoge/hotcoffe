package br.com.goals.etrilhas.servlet.camada;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.servlet.BaseServlet;
import br.com.goals.template.RequestUtil;
import br.com.goals.template.Template;

public class CamadaEditar extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    public CamadaEditar() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		try{
			Long id = Long.parseLong(request.getParameter("id"));
			Camada camada = camadaFacade.selecionar(getMapa(request),id);
			template.setForm("camada",camada);
			template.setMensagem("");
		}catch(Exception e){
			template.setMensagem("Erro: " + e.getMessage());
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		Camada camada = null;
		try{
			Mapa mapa = getMapa(request);
			Long id = Long.parseLong(request.getParameter("Camada.id"));
			camada = camadaFacade.selecionar(mapa,id);
			RequestUtil.request(request, camada);
			
			response.getWriter().write(
					//*
					"UTF-8 = " + 
					URLEncoder.encode(camada.getNome(),"UTF-8") +
					"\n<br>ISO = " +
					URLEncoder.encode(camada.getNome(),"iso-8859-1")
					//*/
					);
			System.out.println(" camada.getNome() = " + camada.getNome());
			
			template.setForm("camada",camada);
			System.out.println("camada.nome = " + camada.getNome());
			mapaFacade.atualizar(mapa);
			
			template.setMensagem("Camada atualizada com sucesso.");
		}catch(Exception e){
			e.printStackTrace();
			template.setMensagem("Erro: " + e.getMessage());
		}
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}

}
