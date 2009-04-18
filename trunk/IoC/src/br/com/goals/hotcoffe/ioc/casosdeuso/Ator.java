package br.com.goals.hotcoffe.ioc.casosdeuso;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.Controlador;
import br.com.goals.hotcoffe.ioc.casosdeuso.util.Opcoes;

public class Ator {
	private static Logger logger = Logger.getLogger(Ator.class);
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
			umCasoDeUso.getTemplate().criarFormulario(obj, umCasoDeUso);
			logger.debug("Caso de uso " + umCasoDeUso.getKey() + " aguardando...");
			Controlador controlador = umCasoDeUso.getControlador();
			synchronized (controlador) {
				controlador.notify();
			}
			wait();
			logger.debug(umCasoDeUso.getKey() + " acordou...");
			if(obj instanceof Opcoes){
				Opcoes opcoes = (Opcoes) obj;
				try{
					int opt = Integer.valueOf(request.getParameter("IoC_opcoes"));
					opcoes.setEscolha(opcoes.getList().get(opt));
				}catch(Exception e){
					opcoes.setEscolha(request.getParameter("IoC_opcoes"));
				}
			}else{
				popular(obj);
			}
		} catch (Exception e) {
			logger.error("Erro ao mandar preencher " +obj,e);
		}
	}
	private void popular(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		//popular
		Method[] metodos = obj.getClass().getMethods();
		for (int i = 0; i < metodos.length; i++) {
			String nome = metodos[i].getName();
			if(nome.startsWith("set")){
				String res = request.getParameter(metodos[i].getName());
				metodos[i].invoke(obj, res);
			}
		}
	}
	public void setRequestResponse(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	public void responder(Opcoes opcoes) {
		preencher(opcoes);
	}
}
