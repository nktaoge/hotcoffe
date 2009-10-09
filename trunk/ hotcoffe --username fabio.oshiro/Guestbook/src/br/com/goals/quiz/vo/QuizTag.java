package br.com.goals.quiz.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class QuizTag {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long idQuiz;
	
	private Long idTag;
	
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
	 * @return the idQuiz
	 */
	public Long getIdQuiz() {
		return idQuiz;
	}
	/**
	 * @param idQuiz the idQuiz to set
	 */
	public void setIdQuiz(Long idQuiz) {
		this.idQuiz = idQuiz;
	}
	/**
	 * @return the idTag
	 */
	public Long getIdTag() {
		return idTag;
	}
	/**
	 * @param idTag the idTag to set
	 */
	public void setIdTag(Long idTag) {
		this.idTag = idTag;
	}
}
