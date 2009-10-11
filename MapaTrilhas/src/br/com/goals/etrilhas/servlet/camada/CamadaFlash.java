package br.com.goals.etrilhas.servlet.camada;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.servlet.BaseServlet;
import br.com.goals.template.RequestUtil;

public class CamadaFlash extends BaseServlet {
	private static final long serialVersionUID = 1L;
    public CamadaFlash() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * Cria uma camada se o mapa estiver vazio
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Camada camada = new Camada();
		String msg = "";
		try{
			RequestUtil.request(request, camada);
			Mapa mapa = getMapa(request);
			if(mapa.getCamadas()==null || mapa.getCamadas().size()==0){
				if(request.getParameter("id").equals("undefined")){
					camadaFacade.criar(camada,mapa);
					msg = "Camada criada com sucesso!";	
				}else{
					camada.setId(Long.valueOf(request.getParameter("id")));
					camadaFacade.atualizar(camada,getMapa(request));
					msg = "Camada atualizada com sucesso!";
				}
			}else{
				//informar a ultima camada criada
				camada = mapa.getCamadas().get(mapa.getCamadas().size()-1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(msg);
		PrintWriter pw = response.getWriter();
		pw.write("<ponto><pid>"+camada.getId()+"</pid><msg>"+msg+"</msg></ponto>");
		pw.close();
	}

}
