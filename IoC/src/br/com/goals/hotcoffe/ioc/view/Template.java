package br.com.goals.hotcoffe.ioc.view;

import java.lang.reflect.Method;
import java.util.List;
import java.util.MissingResourceException;

import br.com.goals.hotcoffe.ioc.Controlador;
import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;


public class Template {	
	
	private String defaultCss="<style type=\"text/css\">.frmIoC div{clear:both}\n.frmIoC div label{display: block; float: left; width:100px; text-align:right;}\n.frmIoC_btn{padding-left:100px;}</style>";
	private UmCasoDeUso umCasoDeUso;
	private MenuPrincipal menuPrincipal = null;
	public Template(UmCasoDeUso umCasoDeUso) {
		this.umCasoDeUso = umCasoDeUso;
		menuPrincipal = new MenuPrincipal(umCasoDeUso);
	}
	
	
	/**
	 * Cria um formulario HTML
	 * @param obj todos os set serao impressos
	 * @param umCasoDeUso
	 * @return html do formulario
	 */
	public String criarFormulario(Object obj,UmCasoDeUso umCasoDeUso) {
		String form=menuPrincipal.toString()+"<form class=\"frmIoC\" action=\"?"+ Controlador.IOC_KEY +"="+umCasoDeUso.getKey()+"\" method=\"post\">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		form+="<fieldset><legend>" + getLabel(obj) + "</legend>";
		Method[] metodos = obj.getClass().getMethods();
		for (int i = 0; i < metodos.length; i++) {
			String nome = metodos[i].getName();
			if(nome.startsWith("setTxt")){
				form+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + "</label><textarea id=\"idField"+i+"\" name=\""+metodos[i].getName()+"\" cols=\"40\" rows=\"10\"></textarea></div>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}else if(nome.startsWith("setList")){ //$NON-NLS-1$
				form+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + "</label><select id=\"idField"+i+"\" name=\""+metodos[i].getName()+"\" /><option>Selecione</option></select></div>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}else if(nome.startsWith("set")){ //$NON-NLS-1$
				form+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + "</label><input id=\"idField"+i+"\" name=\""+metodos[i].getName()+"\" /></div>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}
		}
		form+="<div class=\"frmIoC_btn\"><input type=\"submit\" value=\"Enviar\"/></div></fieldset></form>";		 //$NON-NLS-1$
		return defaultCss + form;
	}
	public static String getLabel(Class cls){
		try{
			return Messages.getString("Template."+cls.getCanonicalName()); //$NON-NLS-1$
		}catch(MissingResourceException e){
			return cls.getSimpleName();
		}
	}
	public static String getLabel(Object obj){
		try{
			return Messages.getString("Template."+obj.getClass().getCanonicalName()); //$NON-NLS-1$
		}catch(MissingResourceException e){
			return obj.getClass().getSimpleName();
		}
	}
	public static String getLabel(Object obj,String nome){
		try{
			return Messages.getString("Template."+obj.getClass().getCanonicalName()+"."+nome); //$NON-NLS-1$
		}catch(MissingResourceException e){
			if(nome.startsWith("setList")){
				return nome.substring(7);
			}
			if(nome.startsWith("setTxt")){
				return nome.substring(6);
			}
			return nome.substring(3);
		}
	}
}
