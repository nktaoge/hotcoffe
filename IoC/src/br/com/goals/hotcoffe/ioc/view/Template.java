package br.com.goals.hotcoffe.ioc.view;

import java.lang.reflect.Method;
import java.util.List;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.Controlador;
import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
import br.com.goals.hotcoffe.ioc.casosdeuso.util.Opcoes;
/**
 * um key gerado interessante "utzuoudami"
 * @author fabio
 *
 */

public class Template {	
	private String defaultCss="<link rel=\"stylesheet\" href=\"../css/template.css\" type=\"text/css\" />";	
	private String defaultHead = "";
	private String upCode = "";
	private String downCode = "</body></html>";
	private UmCasoDeUso umCasoDeUso = null;
	private MenuPrincipal menuPrincipal = null;
	private String mensagem = "";
	private static Logger logger = Logger.getLogger(Template.class);
	private String html = "";
	private String form = null;
	private Controlador controlador;
	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
		if(menuPrincipal!=null){
			menuPrincipal.setControlador(controlador);
		}
	}
	public Template(Controlador controle){
		menuPrincipal = new MenuPrincipal(controle);
		defaultHead = "<head>" + defaultCss + 
			"<script type=\"text/javascript\" src=\"../js/menu.js\"></script></head>\n";
		upCode = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + 
			"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"pt-br\" lang=\"pt-br\" >\n" + defaultHead + "<body>";
	}
	public Template(UmCasoDeUso umCasoDeUso) {
		this.umCasoDeUso = umCasoDeUso;
		menuPrincipal = new MenuPrincipal(umCasoDeUso);
		defaultHead = "<head>" + defaultCss + 
			"<script type=\"text/javascript\" src=\"../js/menu.js\"></script></head>\n";
		upCode = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + 
			"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"pt-br\" lang=\"pt-br\" >\n" + defaultHead + "<body>";
	}
	public void setHtml(String html) {
		this.html = html;
	}
	/**
	 * 
	 * @return toda a pagina html
	 */
	public String getHtml() {
		if(form==null || form.equals("")){
			html = upCode + menuPrincipal + getMensagem() + downCode;
		}else{
			//tem o form
			html = upCode + menuPrincipal + getForm() +
				"<div class=\"frmIoC_btn\"><input type=\"submit\" value=\"Enviar\"/></div></form>" + downCode;
		}
		logger.debug("html return;");
		return html;
	}
	/**
	 * 
	 * @return a mensagem em um ul
	 */
	public String getMensagem() {
		if(mensagem!=null && !mensagem.equals("")){
			return "<ul class=\"alert\">" + mensagem + "</ul>";
		}
		return mensagem;
	}
	/**
	 * 
	 * @return retorna o formulario 
	 */
	public String getForm() {
		if(form==null) return "";
		return form;
	}
	/**
	 * Cria um formulario HTML
	 * @param obj todos os set serao impressos
	 * @param umCasoDeUso
	 * @return html do formulario
	 */
	public String criarFormulario(Object obj,UmCasoDeUso umCasoDeUso) {
		if(form==null){
			form = "<form class=\"frmIoC\" action=\"?"+ Controlador.IOC_KEY +"="+umCasoDeUso.getKey()+"\" method=\"post\">";
		}
		form+="<fieldset><legend>" + getLabel(obj) + "</legend>";
		form+=getMensagem();
		if(obj instanceof Opcoes){
			form+=criarCampos((Opcoes)obj);
		}else{
			form+=criarCampos(obj);
		}
		form+="</fieldset>";
		return form;
	}
	private String criarCampos(Opcoes opcoes){
		logger.debug("criando campos de opcoes");
		String retorno="";
		List<String> list = opcoes.getList();
		int i;
		for (i = 0; i < list.size(); i++) {
			retorno+="<div class=\"radio\">" +
					"<label for=\"idField"+i+"\">" + list.get(i) + "</label>" +
					"<input type=\"radio\" id=\"idField"+i+"\" name=\"IoC_opcoes\" value=\""+i+"\" />" +
					"</div>";
		}
		retorno+="<div><label for=\"idField"+i+"\">Outro: </label><input id=\"idField"+i+"\" name=\"IoC_outro\" /></div>";
		return retorno;
	}
	private String criarCampos(Object obj){
		logger.debug("criando campos de um objeto ");
		String retorno="";
		Method[] metodos = obj.getClass().getMethods();
		for (int i = 0; i < metodos.length; i++) {
			String nome = metodos[i].getName();
			if(nome.startsWith("setTxt")){
				retorno+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + ": </label><textarea id=\"idField"+i+"\" name=\""+metodos[i].getName()+"\" cols=\"40\" rows=\"10\"></textarea></div>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}else if(nome.startsWith("setList")){ //$NON-NLS-1$
				retorno+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + ": </label><select id=\"idField"+i+"\" name=\""+metodos[i].getName()+"\" /><option>Selecione</option></select></div>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}else if(nome.startsWith("set")){ //$NON-NLS-1$
				retorno+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + ": </label><input id=\"idField"+i+"\" name=\""+metodos[i].getName()+"\" /></div>";
			}
		}
		return retorno;
	}
	
	public void adicionarMensagem(String mensagem) {
		logger.debug("adicionando mensagem '" + mensagem + "'");
		this.mensagem+="<li>" + mensagem + "</li>";
	}

	private static String separarNome(String nome){
		String retorno=nome.charAt(0)+"";
		for (int i = 1; i < nome.length(); i++) {
			String ch = nome.substring(i, i+1);
			if(ch.toUpperCase().equals(ch)){
				retorno+=' ' +ch;
			}else{
				retorno+=ch;
			}
		}
		return retorno;
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
				return separarNome(obj.substring(ini+1));
			}
			return separarNome(obj);
		}
	}
	public static String getLabel(Object obj){
		try{
			return Messages.getString("Template."+obj.getClass().getCanonicalName()); //$NON-NLS-1$
		}catch(MissingResourceException e){
			return separarNome(obj.getClass().getSimpleName());
		}
	}
	public static String getLabel(Object obj,String nome){
		try{
			return Messages.getString("Template."+obj.getClass().getCanonicalName()+"."+nome); //$NON-NLS-1$
		}catch(MissingResourceException e){
			if(nome.startsWith("setList")){
				return separarNome(nome.substring(7));
			}
			if(nome.startsWith("setTxt")){
				return separarNome(nome.substring(6));
			}
			return separarNome(nome.substring(3));
		}
	}
}
