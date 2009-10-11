package br.com.goals.template;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import java.util.logging.Logger;

public class RequestUtil {
	private static Logger logger = Logger.getLogger(RequestUtil.class.getName());
	private static boolean parseLong = false;
	
	public static void setParseLong(boolean parseLong) {
		RequestUtil.parseLong = parseLong;
	}
	
	/**
	 * Pega o request e atribui os parametros que casam com o objeto por atributo.<br>
	 * ou seja, não coloca coisas que são null<br>
	 * 
	 * Ex.: &lt;input name="Pessoa.nome">
	 * 
	 * @param request
	 * @param obj
	 * @throws Exception
	 */
	public static void request(HttpServletRequest request, Object obj) throws Exception{
		requestByPrefix(obj.getClass().getSimpleName()+".",request,obj);
	}
	@SuppressWarnings("unchecked")
	public static void requestByPrefix(String prefixo,HttpServletRequest request, Object obj)throws Exception{
		Set<String> keys = request.getParameterMap().keySet();
		
		Class cls = obj.getClass();
		String objName = cls.getSimpleName();
		Method metodos[] = cls.getMethods();
		for(String key:keys){
			String key2methodName=key;
			logger.fine("key="+key);
			if(prefixo!=null){
				//verifica se inicia com o prefixo
				if(!key.startsWith(prefixo)){
					continue;
				}
				key2methodName = key.substring(prefixo.length());
			}
			key2methodName = "set" + Character.toUpperCase(key2methodName.charAt(0))+key2methodName.substring(1);
			logger.fine("key2methodName = " +key2methodName);			
			for (int i = 0; i < metodos.length; i++) {
				try {
					if(metodos[i].getName().equals(key2methodName)){
						String keyVal = request.getParameter(key);
						logger.info(objName + "." + metodos[i].getName() + "(" + URLDecoder.decode(keyVal, "ISO-8859-1")+")");
						Class parametros[]=metodos[i].getParameterTypes();
						for (int j = 0; j < parametros.length; j++) {
							if(parametros[j].getSimpleName().equals("Integer")){
								try{
									metodos[i].invoke(obj, new Object[]{Integer.valueOf(keyVal)});
								}catch(NumberFormatException e){
									if(keyVal.indexOf(".")!=-1){
										metodos[i].invoke(obj, new Object[]{
											new Double(Math.round(Double.valueOf(keyVal))).intValue()
										});
									}
								}
							}else if(parametros[j].getSimpleName().equals("String")){
								metodos[i].invoke(obj, new Object[]{request.getParameter(key)});
							}else if(parseLong && parametros[j].getSimpleName().equals("Long")){
								try{
									metodos[i].invoke(obj, new Object[]{Long.valueOf(keyVal)});
								}catch(NumberFormatException e){
									if(keyVal.indexOf(".")!=-1){
										metodos[i].invoke(obj, new Object[]{
											new Double(Math.round(Double.valueOf(keyVal))).longValue()
										});
									}
								}
							}
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * Pega o request e atribui os parametros que casam com o objeto por atributo.<br>
	 * ou seja, não coloca coisas que são null
	 * @param request
	 * @param obj
	 * @throws Exception
	 */
	public static void requestByAttName(HttpServletRequest request, Object obj) throws Exception{
		requestByPrefix(null, request, obj);
	}
}
