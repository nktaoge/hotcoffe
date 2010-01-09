package br.com.goals.etrilhas.modelo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;

import br.com.goals.cafeina.view.tmp.CafeinaHiddenField;

public abstract class Base implements Serializable{
	private static final long serialVersionUID = -4031640023366960605L;
	private Long id;
	public Base() {
		BeanInfo info;
		try {
			String transients[] = transients();
			if(transients!=null){
				info = Introspector.getBeanInfo(this.getClass());
				PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
				for (int i = 0; i < propertyDescriptors.length; ++i) {
				    PropertyDescriptor pd = propertyDescriptors[i];
				    
				    for (int j = 0; j < transients.length; j++) {
				    	if (pd.getName().equals(transients[j])) {
				    		pd.setValue("transient", Boolean.TRUE);
				    	}
				    }
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Informe os atributos transientes
	 * @return atributos que nao serao salvos no XML
	 */
	protected String[] transients(){
		return null;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Base))
			return false;
		Base other = (Base) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	private Long versao;
	public Long getVersao(){
		return versao;
	}
	
	@CafeinaHiddenField
	public void setVersao(Long l){
		versao = l;
	}
}
