package br.com.goals.jpa4google;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

public class Jpa4GoogleEntityManager implements EntityManager{

	private static Logger logger = Logger.getLogger(Jpa4GoogleEntityManager.class);
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Query createNamedQuery(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query createNativeQuery(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query createNativeQuery(String arg0, Class arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query createNativeQuery(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query createQuery(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T find(Class<T> arg0, Object id) {
		EntityManager em = EMF.createEntityManager();
		XmlData xmlData = em.find(XmlData.class, id);
		InputSource inStream = new InputSource();
        inStream.setCharacterStream(new StringReader(xmlData.getXmlData()));
		XMLDecoder xdec = new XMLDecoder(inStream.getByteStream());
		return (T)xdec.readObject();
	}

	@Override
	public void flush() {
		
	}

	@Override
	public Object getDelegate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlushModeType getFlushMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getReference(Class<T> arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityTransaction getTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void joinTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lock(Object arg0, LockModeType arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T merge(T arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Transforma em url encoded todas as strings
	 * @param obj Objeto
	 * @param objectsEncoded passe null
	 * @param encode true codifica, false decodifica
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	protected static void urlEncodeAllObjectString(Object obj,Set<String> objectsEncoded,boolean encode) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnsupportedEncodingException{
		if(objectsEncoded == null){
			objectsEncoded = new TreeSet<String>();
		}
		
		Class clazz = obj.getClass();
		String key = clazz.getCanonicalName() +"@"+ obj.hashCode();
		if(objectsEncoded.contains(key)){
			//ja foi
			return;
		}else{
			objectsEncoded.add(key);
		}
		logger.debug("salvando " + clazz.getCanonicalName());
		BeanInfo info = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; ++i) {
		    PropertyDescriptor pd = propertyDescriptors[i];
		    logger.debug(" pd.getName() = " + pd.getName() + " " + pd.getPropertyType().getCanonicalName());
		    if (pd.getPropertyType().getCanonicalName().equals("java.lang.String")){
		    	String v = (String)pd.getReadMethod().invoke(obj);
		    	if(v!=null){
		    		logger.debug(" pd.getName() = " + pd.getName() + " = " + v + " old ");
		    		if(encode){
		    			v = URLEncoder.encode(v,"iso-8859-1");
		    		}else{
		    			v = URLDecoder.decode(v,"iso-8859-1");
		    		}
		    		logger.debug(" pd.getName() = " + pd.getName() + " = " + v);
		    		pd.getWriteMethod().invoke(obj,v);
		    	}
		    }else if (pd.getPropertyType().getCanonicalName().equals("java.util.List")) {
		    	logger.debug(" salvando lista " + pd.getName());
		    	List lista = (List)pd.getReadMethod().invoke(obj);
		    	for(Object o:lista){
		    		if(o!=null){
				    	urlEncodeAllObjectString(o,objectsEncoded,encode);
		    		}
		    	}
		    }else if (!pd.getPropertyType().getCanonicalName().startsWith("java.")) {
		    	Object o = pd.getReadMethod().invoke(obj);
		    	if(o!=null){
			    	urlEncodeAllObjectString(o,objectsEncoded,encode);
		    	}
		    }
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setNotIdTransients(Object obj) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class clas = obj.getClass();
		BeanInfo info = Introspector.getBeanInfo(clas);
		PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; ++i) {
		    PropertyDescriptor pd = propertyDescriptors[i];
		    if (pd.getPropertyType().getCanonicalName().equals("java.util.List")) {
		    	logger.debug(pd.getName());
		    	List lista = (List)pd.getReadMethod().invoke(obj);
		    	logger.debug(lista.get(0).getClass());
		    	Class childClass = lista.get(0).getClass();
		    	BeanInfo infoChild = Introspector.getBeanInfo(childClass);
		    	PropertyDescriptor[] propertyDescriptorsChild = infoChild.getPropertyDescriptors();
		    	for (int j = 0; j < propertyDescriptorsChild.length; ++j) {
		    		PropertyDescriptor pdChild = propertyDescriptorsChild[j];
		    		logger.debug("pdChild = " + pdChild.getName());
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
		}
	}
	
	@Override
	public void persist(Object obj) {
		OutputStream outputStream = new ByteArrayOutputStream();
		XMLEncoder xenc = new XMLEncoder(outputStream);
		
		try {
			/*
			 * colocar tudo o que nao eh id como transiente 
			 * nas listas
			 */
			setNotIdTransients(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		xenc.writeObject(obj);
		xenc.close();
		XmlData xmlData = new XmlData();
		xmlData.setXmlData(outputStream.toString());
	}

	@Override
	public void refresh(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFlushMode(FlushModeType arg0) {
		// TODO Auto-generated method stub
		
	}

}
