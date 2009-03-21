package br.com.goals.hotcoffe.ioc.casosdeuso;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.hotcoffe.ioc.Controlador;
import br.com.goals.hotcoffe.ioc.view.Template;

public class Ator {
	private UmCasoDeUso umCasoDeUso;
	private HttpServletRequest request;
	private HttpServletResponse response;
	public Ator(UmCasoDeUso umCasoDeUso) {
		this.umCasoDeUso = umCasoDeUso;
		request = umCasoDeUso.request;
		response = umCasoDeUso.response;
	}
	/**
	 * Super abstracao, invoca o Template para cuidar disso
	 * @param obj
	 */
	public synchronized void preencher(Object obj){
		UmCasoDeUso.getCasosDeUso().put(umCasoDeUso.getKey(),umCasoDeUso);
		try {
			PrintWriter printWriter = umCasoDeUso.getResponse().getWriter();
			String form = Template.criarFormulario(obj, umCasoDeUso);
			printWriter.write(form);
			umCasoDeUso.aguardar=true;
			System.out.println("Caso de uso aguardando");
			Controlador controlador = (Controlador)request.getAttribute(Controlador.IOC_KEY);
			synchronized (controlador) {
				controlador.notify();
			}
			wait();
			System.out.println("acordou...");
			//popular
			Method[] metodos = obj.getClass().getMethods();
			for (int i = 0; i < metodos.length; i++) {
				String nome = metodos[i].getName();
				if(nome.startsWith("set")){
					String res = request.getParameter(metodos[i].getName());
					metodos[i].invoke(obj, res);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setRequestResponse(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
}
