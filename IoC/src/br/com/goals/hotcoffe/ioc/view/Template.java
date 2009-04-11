package br.com.goals.hotcoffe.ioc.view;

import java.lang.reflect.Method;
import java.util.MissingResourceException;

import br.com.goals.hotcoffe.ioc.Controlador;
import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;


public class Template {	
	
	private String defaultCss="<style type=\"text/css\">.frmIoC{clear:both;}\n.frmIoC div{clear:both}\n.frmIoC div label{display: block; float: left; width:100px; text-align:right;}\n.frmIoC_btn{padding-left:100px;}</style>";
	
	private String defaultHead = "";
	private String upCode = "";
	private String downCode = "</body></html>";
	private UmCasoDeUso umCasoDeUso;
	private MenuPrincipal menuPrincipal = null;
	public Template(UmCasoDeUso umCasoDeUso) {
		this.umCasoDeUso = umCasoDeUso;
		menuPrincipal = new MenuPrincipal(this.umCasoDeUso);
		defaultHead = "<head><link rel=\"stylesheet\" href=\"../css/template.css\" type=\"text/css\" />" + defaultCss + 
			"<script type=\"text/javascript\" src=\"../js/menu.js\"></script></head>\n";
		upCode = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + 
			"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"pt-br\" lang=\"pt-br\" >\n" + defaultHead + "<body>";
	}
	
	
	/**
	 * Cria um formulario HTML
	 * @param obj todos os set serao impressos
	 * @param umCasoDeUso
	 * @return html do formulario
	 */
	public String criarFormulario(Object obj,UmCasoDeUso umCasoDeUso) {
		String form="<form class=\"frmIoC\" action=\"?"+ Controlador.IOC_KEY +"="+umCasoDeUso.getKey()+"\" method=\"post\">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
		return upCode + menuPrincipal + form + downCode;
	}
	public String criarMensagem(String mensagem) {
		return upCode + menuPrincipal + "<div class=\"alert\">" + mensagem + "</div>" + downCode;
	}
	@SuppressWarnings("unchecked")
	public static String getLabel(Class cls){
		try{
			return Messages.getString("Template."+cls.getCanonicalName()); //$NON-NLS-1$
		}catch(MissingResourceException e){
			return cls.getSimpleName();
		}
	}
	public static String getLabel(String obj){
		try{
			return Messages.getString("Template."+obj); //$NON-NLS-1$
		}catch(MissingResourceException e){
			int ini = obj.lastIndexOf('.');
			if(ini!=-1){
				return obj.substring(ini+1);
			}
			return obj;
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
