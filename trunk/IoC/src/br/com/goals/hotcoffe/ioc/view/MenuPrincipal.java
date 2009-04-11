package br.com.goals.hotcoffe.ioc.view;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
@SuppressWarnings("unchecked")
public class MenuPrincipal {
	private static Logger logger = Logger.getLogger(MenuPrincipal.class);
	private String menu=null;
	private UmCasoDeUso umCasoDeUso;
	
	private static Comparator<Class> comparator = new Comparator<Class>(){
		public int compare(Class o1, Class o2) {
			return o1.getName().compareTo(o2.getName());
		}		
	};
	public MenuPrincipal(UmCasoDeUso umCasoDeUso) {
		this.umCasoDeUso = umCasoDeUso;
	}
	
	private String package2ul(String pckgname,String base) throws ClassNotFoundException{
		String retorno = "<ul>";
		ArrayList<Class> classes = new ArrayList<Class>();
		int len = base.length()+1;
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = '/' + pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " (" + directory
					+ ") does not appear to be a valid package");
		}
		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			String className="";
			for (int i = 0; i < files.length; i++) {
				if (!files[i].endsWith(".class")) {
					try{
						className = pckgname+'.'+files[i];
						retorno += "<li><a href=\"#\" onclick=\"return false;\">" + Template.getLabel(className)+"</a>";
						retorno += package2ul(className,base)+"</li>";
					}catch(Exception e){
						logger.info("Excluindo " + className);
					}
				}
			}
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					try{
						className = pckgname + '.'+ files[i].substring(0, files[i].length() - 6);
						Class classe = Class.forName(className);
						classes.add(classe);
					}catch(Exception e){
						//may isnt a UserCase?
						logger.info("Excluindo " + className);
					}
				}
			}
			Collections.sort(classes, comparator);
			for (int i = 0; i < classes.size(); i++) {
				retorno+= "<li><a href=\"" + classes.get(i).getName().substring(len) + "\">" + Template.getLabel(classes.get(i).getName()) + "</a></li>";
			}
		} else {
			throw new ClassNotFoundException(pckgname
					+ " does not appear to be a valid package");
		}
		return retorno + "</ul>";
	}
	
	public String toString(){
		if(menu==null){
			try {
				String pacoteCasosDeUso =umCasoDeUso.getControlador().getPacoteCasosDeUso();
				menu = "<div id=\"pillmenu\">" + package2ul(pacoteCasosDeUso,pacoteCasosDeUso)+ "</div>";
			} catch (ClassNotFoundException e) {
				menu = "Sem menu!";
			}
		}
		return menu;
	}
}
