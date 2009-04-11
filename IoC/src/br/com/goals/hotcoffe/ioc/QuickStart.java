package br.com.goals.hotcoffe.ioc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class QuickStart {
	private static Logger logger = Logger.getLogger(QuickStart.class);
	
	public static List<Class> getClasses(String pckgname) throws ClassNotFoundException {
		ArrayList<Class> classes = new ArrayList<Class>();
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
				}else{
					try{
						className = pckgname+'.'+files[i];
						classes.addAll(getClasses(className));
					}catch(Exception e){
						logger.info("Excluindo " + className);
					}
				}
			}
		} else {
			throw new ClassNotFoundException(pckgname
					+ " does not appear to be a valid package");
		}
		return classes;
	}
}
