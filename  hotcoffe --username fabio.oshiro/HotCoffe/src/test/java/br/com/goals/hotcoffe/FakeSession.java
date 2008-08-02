package br.com.goals.hotcoffe;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;


public class FakeSession implements HttpSession{
	private HashMap<String,Object> attibute= new HashMap<String,Object>();
	
	public Object getAttribute(String key) {
		return attibute.get(key);
	}

	@SuppressWarnings("unchecked")
	
	public Enumeration getAttributeNames() {
		return null;
	}

	
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	
	public javax.servlet.http.HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void removeValue(String arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void setAttribute(String key, Object value) {
		attibute.put(key, value);
	}

	
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
}