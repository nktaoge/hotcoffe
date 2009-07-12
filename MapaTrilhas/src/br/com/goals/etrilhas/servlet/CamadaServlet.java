package br.com.goals.etrilhas.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.facade.CamadaFacade;
import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.utils.RequestUtil;

/**
 * Servlet implementation class CamadaServlet
 */
public class CamadaServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private CamadaFacade camadaFacade = new CamadaFacade(); 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CamadaServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Camada camada = new Camada();
		String msg = "";
		try{
			RequestUtil.request(request, camada);
			if(request.getParameter("id").equals("undefined")){
				camadaFacade.criar(camada,getMapa(request));
				msg = "Camada criada com sucesso!";	
			}else{
				camada.setId(Long.valueOf(request.getParameter("id")));
				camadaFacade.atualizar(camada,getMapa(request));
				msg = "Camada atualizada com sucesso!";
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
