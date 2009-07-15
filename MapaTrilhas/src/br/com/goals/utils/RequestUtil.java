package br.com.goals.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
	@SuppressWarnings("unchecked")
	public static void request(HttpServletRequest request, Object obj) throws Exception{
		Set<String> keys = request.getParameterMap().keySet();
		
		Class cls = obj.getClass();
		Method metodos[] = cls.getMethods();
		for(String key:keys){
			//System.out.println(key + " = '" + URLDecoder.decode(request.getParameter(key), "ISO-8859-1")+"'");
			for (int i = 0; i < metodos.length; i++) {
				try {
					if(metodos[i].getName().equals("set" + Character.toUpperCase(key.charAt(0))+key.substring(1))){
						String keyVal = request.getParameter(key);
						System.out.println("RequestUtil.request(): " + metodos[i].getName() + " = " + URLDecoder.decode(keyVal, "ISO-8859-1"));
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
}
