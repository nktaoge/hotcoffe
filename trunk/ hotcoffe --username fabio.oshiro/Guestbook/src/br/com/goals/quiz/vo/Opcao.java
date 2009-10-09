package br.com.goals.quiz.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.datanucleus.jpa.annotations.Extension;

import com.google.appengine.api.datastore.Blob;

@Entity
public class Opcao {
	/*
	 * Foi necessario colocar o 
	 * @ Extension
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk",value="true") 
    private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/**/
	
	private Pergunta pergunta;
	
	private Blob imagem;
	
	private String texto;
	
	private Boolean correta;

	

	/**
	 * @return the pergunta
	 */
	public Pergunta getPergunta() {
		return pergunta;
	}

	/**
	 * @param pergunta the pergunta to set
	 */
	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	/**
	 * @return the imagem
	 */
	public Blob getImagem() {
		return imagem;
	}

	/**
	 * @param imagem the imagem to set
	 */
	public void setImagem(Blob imagem) {
		this.imagem = imagem;
	}

	/**
	 * @return the texto
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * @param texto the texto to set
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}

	/**
	 * @return the correta
	 */
	public Boolean getCorreta() {
		return correta;
	}

	/**
	 * @param correta the correta to set
	 */
	public void setCorreta(Boolean correta) {
		this.correta = correta;
	}
}
