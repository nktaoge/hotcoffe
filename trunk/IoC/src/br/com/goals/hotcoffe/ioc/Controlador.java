package br.com.goals.hotcoffe.ioc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;

/**
 * Servlet implementation class OlaMundo
 */
public class Controlador extends HttpServlet {
	private static final long serialVersionUID = 123L;
	public static final String IOC_KEY = "IoC";
	int contador=0;
    /**
     * Default constructor. 
     */
    public Controlador() {
        // TODO Auto-generated constructor stub
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
		System.out.println("contador = " + ++contador);
		try{
			Thread t33 = new Thread(){
				@Override
				public void run() {
					UmCasoDeUso umCasoDeUso = null;
					if(request.getParameter(IOC_KEY)!=null){
						umCasoDeUso=UmCasoDeUso.getCasoDeUso(request.getParameter(IOC_KEY));
						if(umCasoDeUso==null){
							request.setAttribute("liberar","true");
							Controlador controlador = (Controlador)request.getAttribute(IOC_KEY);
							synchronized (controlador) {
								controlador.notify();
							}
						}else{
							umCasoDeUso.setRequestResponse(request, response);
							UmCasoDeUso.acordar(umCasoDeUso);
						}
					}else{
						try {
							umCasoDeUso = (UmCasoDeUso) Class.forName("br.com.goals.hotcoffe.ioc.casosdeuso.CadastrarUsuario").newInstance();
							umCasoDeUso.aguardar=false;
							umCasoDeUso.setRequestResponse(request,response);
							umCasoDeUso.executar();
						} catch (Exception e) {
							e.printStackTrace();
							request.setAttribute("liberar","true");
							Controlador controlador = (Controlador)request.getAttribute(IOC_KEY);
							synchronized (controlador) {
								controlador.notify();
							}
						}
					}
				}
			};
			request.setAttribute(IOC_KEY,this);
			synchronized (this) {
				t33.start();
				System.out.println("Aguardando chamar interface usuario");
				wait();	
			}
			while(request.getAttribute("liberar")==null){
				Thread.sleep(1000);
				System.out.print(". ");
			}
			System.out.println("fim = " + contador);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}