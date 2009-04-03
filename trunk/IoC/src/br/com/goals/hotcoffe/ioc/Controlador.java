package br.com.goals.hotcoffe.ioc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;

/**
 * Servlet implementation class OlaMundo
 */
public class Controlador extends HttpServlet {
	private static Logger logger = Logger.getLogger(Controlador.class);
	private static final long serialVersionUID = 123L;
	public static final String IOC_KEY = "IoC";
	int contador=0;
    public Controlador() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		chamarCasoDeUso(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		chamarCasoDeUso(request,response);
	}
	
	private void chamarCasoDeUso(final HttpServletRequest request,final HttpServletResponse response) throws ServletException, IOException {
		logger.debug("contador = " + ++contador);
		try{
			UmCasoDeUso umCasoDeUso = null;
			if(request.getParameter(IOC_KEY)!=null){
				umCasoDeUso=UmCasoDeUso.getCasoDeUso(request.getParameter(IOC_KEY));
			}
			if(umCasoDeUso!=null){
				umCasoDeUso.setControlador(this);
				umCasoDeUso.setRequestResponse(request, response);
				synchronized (this) {
					UmCasoDeUso.acordar(umCasoDeUso);
					logger.debug("Controlador aguardando chamar interface usuario");
					wait();
				}
			}else{
				umCasoDeUso = (UmCasoDeUso) Class.forName("br.com.goals.hotcoffe.ioc.casosdeuso.CadastrarUsuario").newInstance();
				umCasoDeUso.setControlador(this);
				umCasoDeUso.setRequestResponse(request,response);
				Thread t33 = new Thread(umCasoDeUso);
				synchronized (this) {
					t33.start();
					logger.debug("Controlador aguardando chamar interface usuario");
					wait();
				}
			}
			logger.debug("fim = " + contador);
		}catch(Exception e){
			logger.error("Erro ao tentar executar",e);
		}
	}
}