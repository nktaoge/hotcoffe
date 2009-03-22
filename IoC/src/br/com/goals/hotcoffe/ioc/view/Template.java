package br.com.goals.hotcoffe.ioc.view;

import java.lang.reflect.Method;
import java.util.MissingResourceException;

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
		String form="<form action=\"?"+ Controlador.IOC_KEY +"="+umCasoDeUso.getKey()+"\" method=\"post\">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Method[] metodos = obj.getClass().getMethods();
		for (int i = 0; i < metodos.length; i++) {
			String nome = metodos[i].getName();
			if(nome.startsWith("set")){ //$NON-NLS-1$
				form+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + "</label><input id=\"idField"+i+"\" name=\""+metodos[i].getName()+"\" /></div>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}
		}
		form+="<input type=\"submit\"/></form>";		 //$NON-NLS-1$
		return form;
	}
	private static String getLabel(Object obj,String nome){
		try{
			return Messages.getString("Template."+obj.getClass().getCanonicalName()+"."+nome); //$NON-NLS-1$
		}catch(MissingResourceException e){
			return nome.substring(3);
		}
	}
}
