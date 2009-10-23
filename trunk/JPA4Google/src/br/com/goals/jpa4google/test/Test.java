package br.com.goals.jpa4google.test;

import java.beans.BeanInfo;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.Introspector;
import java.beans.PersistenceDelegate;
import java.beans.PropertyDescriptor;
import java.beans.XMLEncoder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
public class Test {
	
	public static void main(String[] args) {
		try{
			Objeto obj = new Objeto();
			obj.setNome("Pai");
			ObjetoFilho of = new ObjetoFilho("teste");
			of.setId(12L);
			obj.getLista().add(of);
			XMLEncoder xenc = new XMLEncoder(System.out);
			
			
			Class clas = obj.getClass();
			BeanInfo info = Introspector.getBeanInfo(clas);
			PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; ++i) {
			    PropertyDescriptor pd = propertyDescriptors[i];
			    if (pd.getPropertyType().getCanonicalName().equals("java.util.List")) {
			    	System.out.println(pd.getName());
			    	List lista = (List)pd.getReadMethod().invoke(obj);
			    	System.out.println(lista.get(0).getClass());
			    	Class childClass = lista.get(0).getClass();
			    	BeanInfo infoChild = Introspector.getBeanInfo(childClass);
			    	PropertyDescriptor[] propertyDescriptorsChild = infoChild.getPropertyDescriptors();
			    	for (int j = 0; j < propertyDescriptorsChild.length; ++j) {
			    		PropertyDescriptor pdChild = propertyDescriptorsChild[j];
			    		System.out.println("pdChild = " + pdChild.getName());
			    		try{
			    			if(pdChild.getName().equals("class")) continue;
				    		Field f = childClass.getDeclaredField(pdChild.getName());
				    		Id o = f.getAnnotation(Id.class);
				    		if(o==null){
				    			pdChild.setValue("transient", Boolean.TRUE);
				    		}
			    		}catch (Exception e) {
			    			e.printStackTrace();
						}
			    	}
			    }
			    pd.setValue("transient", Boolean.FALSE);
			}
	
			
			xenc.writeObject(of);
			//xenc.writeObject(obj);
			xenc.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
