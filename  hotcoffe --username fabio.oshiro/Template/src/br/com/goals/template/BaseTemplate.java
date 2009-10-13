package br.com.goals.template;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;


public class BaseTemplate{
	private static Logger logger = Logger.getLogger(BaseTemplate.class);
	/**
	 * Utilizado para criar o atributo id dos campos de formul&aacute;rio
	 */
	private long idField = 1;
	
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
		String strKey = "Template."+obj.getClass().getCanonicalName()+"."+nome;
		try{
			return Messages.getString(strKey);
		}catch(MissingResourceException e){
			String retorno = "";
			logger.warn("Label not found: " + strKey);
			if(nome.startsWith("setList")){
				retorno = separarNome(nome.substring(7));
			}else if(nome.startsWith("setTxt")){
				retorno = separarNome(nome.substring(6));
			}else if(nome.startsWith("setHtml")){
				retorno = separarNome(nome.substring(7));
			}else{
				retorno = separarNome(nome.substring(3));
			}
			//casos comuns
			if(retorno.equals("Descricao")){
				retorno = "Descri&ccedil;&atilde;o";
			}
			return retorno;
		}
	}
	
	/**
	 * 
	 * @param nome
	 * @return meuNome -> meu Nome
	 */
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
	
	/**
	 * Cria campos de formul&aacute;rio<br>
	 * Se o metodo come&ccedil;ar com:
	 * <ul>
	 * 	<li><b>setHtml</b> criar&aacute; um campo de textarea com a o atributo class="mceEditor"</li> 
	 *  <li><b>setTxt</b>  criar&aacute; um campo de textarea somente</li>
	 * </ul>
	 * 
	 * @param prefixo qualquer coisa para colocar antes do nome do campo
	 * @param obj Qualquer objeto com set e get
	 * @return String com HTML
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	protected String criarCampos(String prefixo,Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(obj==null) return "";
		if(prefixo==null) prefixo="";
		String retorno="";
		Method[] metodos = obj.getClass().getMethods();
		for (int i = 0; i < metodos.length; i++) {
			String nome = metodos[i].getName();
			String tipo = null;
			Method getMethod = null;
			logger.debug("metodo = " + nome);
			Class cls[] = metodos[i].getParameterTypes();
			if(cls.length!=1) continue;
			if(!cls[0].getName().startsWith("java.lang")) continue;
			if(!nome.startsWith("set") && !nome.startsWith("get")) continue;
			String inputName = metodos[i].getName().substring(3);
			
			//Obter o valor
			String inputValue = "";
			try{
				getMethod = obj.getClass().getMethod("get"+inputName);
				Object object = getMethod.invoke(obj,new Object[]{});
				if(object!=null){
					inputValue = object.toString();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			//obter o tipo
			try{
				tipo = getMethod.getReturnType().getCanonicalName();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//Colocar o prefixo
			inputName = prefixo + inputName;
			
			//Montar o campo
			if(nome.equals("getId")){
				Object val = metodos[i].invoke(obj);
				if(val!=null)
					retorno+="<input type=\"hidden\" id=\"idField"+idField+"\" name=\""+prefixo+"id\" value=\""+val.toString()+"\" />";
			}else if(nome.equals("setId")){
				Object val=null;
				try {
					val = obj.getClass().getMethod("getId").invoke(obj);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				if(val!=null)
					retorno+="<input type=\"hidden\" id=\"idField"+idField+"\" name=\""+prefixo+"id\" value=\""+val.toString()+"\" />";
			}else if(nome.startsWith("setHtml")){
				retorno+="<div><label for=\"idField"+idField+"\">" + getLabel(obj,nome) + ": </label><textarea id=\"idField"+idField+"\" name=\""+inputName+"\" class=\"mceEditor\" cols=\"40\" rows=\"10\">"+inputValue.replace("<", "&lt;")+"</textarea></div>";
			}else if(nome.startsWith("setTxt")){
				retorno+="<div><label for=\"idField"+idField+"\">" + getLabel(obj,nome) + ": </label><textarea id=\"idField"+idField+"\" name=\""+inputName+"\" cols=\"40\" rows=\"10\">"+inputValue.replace("<", "&lt;")+"</textarea></div>";
			}else if(nome.startsWith("setList")){
				retorno+="<div><label for=\"idField"+idField+"\">" + getLabel(obj,nome) + ": </label><select id=\"idField"+idField+"\" name=\""+inputName+"\" /><option>Selecione</option></select></div>";
			}else if(nome.startsWith("set")){
				//verificar se o retorno eh um boolean
				if("java.lang.Boolean".equals(tipo)){
					retorno+="<div><label for=\"idField"+idField+"\">" + getLabel(obj,nome) + ": </label>";
					if("true".equals(inputValue)){
						retorno+="<select id=\"idField"+idField+"\" name=\""+inputName+"\"><option value=\"on\" selected=\"selected\">Sim</option><option value=\"\">N&atilde;o</option></select></div>";
					}else{
						retorno+="<select id=\"idField"+idField+"\" name=\""+inputName+"\"><option value=\"on\">Sim</option><option value=\"\" selected=\"selected\">N&atilde;o</option></select></div>";
					}
				}else{
					logger.debug("tipo = '" +tipo+ "'");
					retorno+="<div><label for=\"idField"+idField+"\">" + getLabel(obj,nome) + ": </label><input id=\"idField"+idField+"\" name=\""+inputName+"\" value=\""+inputValue.replace("\"", "&quot;")+"\" /></div>";
				}
			}
			idField++;
		}
		return retorno;
	}
}
