package br.com.goals.quiz.vo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.appengine.api.users.User;
@Entity
public class Quiz {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private User author;
	
	private String nome;
	
	@ManyToOne
	private List<Pergunta> perguntas;
	
	private List<QuizTag> tags;

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
	 * @return the author
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(User author) {
		this.author = author;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the perguntas
	 */
	public List<Pergunta> getPerguntas() {
		return perguntas;
	}

	/**
	 * @param perguntas the perguntas to set
	 */
	public void setPerguntas(List<Pergunta> perguntas) {
		this.perguntas = perguntas;
	}

	/**
	 * @return the tags
	 */
	public List<QuizTag> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<QuizTag> tags) {
		this.tags = tags;
	} 
}
