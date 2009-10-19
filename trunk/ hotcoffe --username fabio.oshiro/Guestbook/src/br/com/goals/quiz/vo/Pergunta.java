package br.com.goals.quiz.vo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.appengine.api.users.User;
@Entity
public class Pergunta {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private User author;
	
	private String txtTexto;
	
	/*
	 * Many To Many dont work
	 */
	//@ ManyToMany(targetEntity=Opcao.class)
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<Opcao> opcoes = new ArrayList<Opcao>();

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
	 * @return the texto
	 */
	public String getTxtTexto() {
		return txtTexto;
	}

	/**
	 * @param texto the texto to set
	 */
	public void setTxtTexto(String texto) {
		this.txtTexto = texto;
	}

	/**
	 * @return the opcoes
	 */
	public List<Opcao> getOpcoes() {
		return opcoes;
	}

	/**
	 * @param opcoes the opcoes to set
	 */
	public void setOpcoes(List<Opcao> opcoes) {
		this.opcoes = opcoes;
	}
}
