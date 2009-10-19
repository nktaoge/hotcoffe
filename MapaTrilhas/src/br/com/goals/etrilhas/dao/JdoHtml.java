package br.com.goals.etrilhas.dao;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

/**
 * Entidade de HTML do JODO para nao gravar no disco<br>
 * Com JPA nao da para salvar String maior que 500 chars 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class JdoHtml {
	/**
	 * ID
	 */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Persistent(defaultFetchGroup="true") 
	private Text txtHtml;

	/**
	 * Construtor default
	 */
	public JdoHtml() {
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the txtHtml
	 */
	public Text getTxtHtml() {
		return txtHtml;
	}

	/**
	 * @param txtXml the txtHtml to set
	 */
	public void setTxtHtml(Text txtHtml) {
		this.txtHtml = txtHtml;
	}
	
	@Override
	public String toString() {
		return "fileName="+fileName+"\nhtml="+txtHtml;
	}
}
