package br.com.goals.hotcoffe.ioc.view;

import java.lang.reflect.Method;

import br.com.goals.hotcoffe.ioc.Controlador;
import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;


public class Template {	
	/**
	 * 
	 * @param obj todos os set serao impressos
	 * @param umCasoDeUso
	 * @return html do formulario
	 */
	public static String criarFormulario(Object obj,UmCasoDeUso umCasoDeUso) {
		String form="<form action=\"?"+ Controlador.IOC_KEY +"="+umCasoDeUso.getKey()+"\" method=\"post\">";
		Method[] metodos = obj.getClass().getMethods();
		for (int i = 0; i < metodos.length; i++) {
			String nome = metodos[i].getName();
			if(nome.startsWith("set")){
				form+=metodos[i].getName() + "<input name=\""+metodos[i].getName()+"\" />";
			}
		}
		form+="<input type=\"submit\"/></form>";		
		return form;
	}
}
