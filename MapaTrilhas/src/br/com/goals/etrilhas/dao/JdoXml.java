package br.com.goals.etrilhas.dao;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

/**
 * Entidade de XML do JODO para nao gravar no disco<br>
 * Com JPA nao da para salvar String maior que 500 chars 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class JdoXml {
	/**
	 * ID
	 */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private Long versao;
	
	@Persistent(defaultFetchGroup="true") 
	private Text txtXml;

	/**
	 * Construtor default
	 */
	public JdoXml() {
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
	 * @return the txtXml
	 */
	public Text getTxtXml() {
		return txtXml;
	}

	/**
	 * @param txtXml the txtXml to set
	 */
	public void setTxtXml(Text txtXml) {
		this.txtXml = txtXml;
	}
	
	public Long getVersao() {
		return versao;
	}
	public void setVersao(Long versao) {
		this.versao = versao;
	}
}
