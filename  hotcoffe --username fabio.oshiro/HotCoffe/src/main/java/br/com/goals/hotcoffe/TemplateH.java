package br.com.goals.hotcoffe;
import java.io.File;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import br.com.goals.utils.StringUtils;

public class TemplateH {
	String html;
	public TemplateH(File file){
		
	}
	public TemplateH(String strTemplate){
		
	}
	public TemplateH(Class<? extends Action> class1) {
		System.out.println(class1.getSimpleName());
	}
	/**
	 * Recupera os valores de formulario postados
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public void mergeRequest(HttpServletRequest request){
		Enumeration<String> e = request.getParameterNames();
		if(e==null) return;
		while (e.hasMoreElements()) {
			String parm = e.nextElement();
			request.getParameter(parm);
			//procurar por input text
			//procurar por textarea
			//procurar por select
			//procurar por checkbox
			//procurar por radio
			
		}
	}
	public void setArea(String name, String value) {
		name = name.replace("(","\\(").replace(")","\\)").replace(".","\\.");
		Pattern patArea = Pattern.compile("<!-- ini "+name+" -->(.*?)<!-- end "+name+" -->",Pattern.DOTALL);
		Matcher mat = patArea.matcher(html);
		if(mat.find()){
			html = mat.replaceAll(StringUtils.tratarCaracteresEspeciaisRegex(value));
		}else{
			System.out.println("Warning: Area not found '"+name+"'"); 
		}
		
	}
}
