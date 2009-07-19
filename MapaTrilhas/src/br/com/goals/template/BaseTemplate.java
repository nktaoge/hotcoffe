package br.com.goals.template;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;

public class BaseTemplate{
	private static Logger logger = Logger.getLogger(BaseTemplate.class);
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
			logger.warn("Label not found: " + strKey);
			if(nome.startsWith("setList")){
				return separarNome(nome.substring(7));
			}
			if(nome.startsWith("setTxt")){
				return separarNome(nome.substring(6));
			}
			return separarNome(nome.substring(3));
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
	@SuppressWarnings("unchecked")
	protected String criarCampos(String prefixo,Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(obj==null) return "";
		if(prefixo==null) prefixo="";
		String retorno="";
		Method[] metodos = obj.getClass().getMethods();
		for (int i = 0; i < metodos.length; i++) {
			String nome = metodos[i].getName();
			logger.debug("metodo = " + nome);
			Class cls[] = metodos[i].getParameterTypes();
			if(cls.length!=1) continue;
			if(!cls[0].getName().startsWith("java.lang")) continue;
			if(!nome.startsWith("set") && !nome.startsWith("get")) continue;
			String inputName = metodos[i].getName().substring(3);
			
			//Obter o valor
			String inputValue = "";
			try{
				Object object = obj.getClass().getMethod("get"+inputName).invoke(obj,new Object[]{});
				if(object!=null){
					inputValue = object.toString();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//Colocar o prefixo
			inputName = prefixo + inputName;
			
			//Montar o campo
			if(nome.equals("getId")){
				Object val = metodos[i].invoke(obj);
				if(val!=null)
					retorno+="<input type=\"hidden\" id=\"idField"+i+"\" name=\""+prefixo+"id\" value=\""+val.toString()+"\" />";
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
					retorno+="<input type=\"hidden\" id=\"idField"+i+"\" name=\""+prefixo+"id\" value=\""+val.toString()+"\" />";
			}else if(nome.startsWith("setTxt")){
				retorno+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + ": </label><textarea id=\"idField"+i+"\" name=\""+inputName+"\" cols=\"40\" rows=\"10\">"+inputValue.replace("<", "&lt;")+"</textarea></div>";
			}else if(nome.startsWith("setList")){
				retorno+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + ": </label><select id=\"idField"+i+"\" name=\""+inputName+"\" /><option>Selecione</option></select></div>";
			}else if(nome.startsWith("set")){
				retorno+="<div><label for=\"idField"+i+"\">" + getLabel(obj,nome) + ": </label><input id=\"idField"+i+"\" name=\""+inputName+"\" value=\""+inputValue.replace("\"", "&quot;")+"\" /></div>";
			}
		}
		return retorno;
	}
}
