package br.com.goals.hotcoffe.ioc.casosdeuso;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.hotcoffe.ioc.Controlador;

public class Ator {
	private UmCasoDeUso umCasoDeUso;
	private Object obj;
	private HttpServletRequest request;
	private HttpServletResponse response;
	public Ator(UmCasoDeUso umCasoDeUso) {
		this.umCasoDeUso = umCasoDeUso;
		request = umCasoDeUso.request;
		response = umCasoDeUso.response;
	}
	public synchronized void preencher(Object obj){
		this.obj = obj;
		UmCasoDeUso.getCasosDeUso().put("asdf",umCasoDeUso);
		try {
			String form="<form action=\"?"+ Controlador.IOC_KEY +"=asdf\" method=\"post\">";
			PrintWriter printWriter = umCasoDeUso.response.getWriter();
			Method[] metodos = obj.getClass().getMethods();
			for (int i = 0; i < metodos.length; i++) {
				String nome = metodos[i].getName();
				if(nome.startsWith("set")){
					form+=metodos[i].getName() + "<input name=\""+metodos[i].getName()+"\" />";
				}
			}
			form+="<input type=\"submit\"/></form>";
			printWriter.write(form);
			umCasoDeUso.aguardar=true;
			System.out.println("Caso de uso aguardando");
			umCasoDeUso.request.setAttribute("liberar","true");
			wait();
			System.out.println("acordou...");
			//popular
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
