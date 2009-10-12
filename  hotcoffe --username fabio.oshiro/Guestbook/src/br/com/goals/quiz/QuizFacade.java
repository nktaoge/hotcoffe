package br.com.goals.quiz;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.goals.persistencia.EMF;
import br.com.goals.quiz.vo.Opcao;
import br.com.goals.quiz.vo.Pergunta;
import br.com.goals.quiz.vo.Quiz;
import br.com.goals.quiz.vo.QuizTag;
import br.com.goals.tag.TagFacade;

/**
 * Facade do Quiz
 */
public class QuizFacade {
	/**
	 * Inicia o quiz
	 * @param tagName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Quiz iniciar(String tagName){
		Quiz quiz = null;
		Long idTag = TagFacade.getInstance().getTagId(tagName);
		EntityManager em = EMF.createEntityManager();
		
		Query query = em.createQuery("Select qt From QuizTag qt where qt.idTag=:idTag");
		query.setParameter("idTag",idTag);
		try{
			List<QuizTag> quizTags = query.getResultList();
			for(QuizTag quizTag : quizTags){
				quiz = (Quiz)em.find(Quiz.class,quizTag.getIdQuiz());
				break;
			}
			return quiz;
		}finally{
			em.close();
		}
	}
	
	/**
	 * Finaliza o quiz e ve a nota
	 */
	public void finalizar(){
		
	}
	
	public void salvar(Pergunta pergunta) {
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		if(pergunta.getId()!=null){
			em.merge(pergunta);
		}else{
			em.persist(pergunta);
		}
		em.flush();
		em.getTransaction().commit();
		System.out.println("id = " + pergunta.getId());
	}

	public void salvar(Opcao opcao) {
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		if(opcao.getId()!=null){
			em.merge(opcao);
		}else{
			em.persist(opcao);
		}
		em.flush();
		em.getTransaction().commit();
		System.out.println("id = " + opcao.getId());
	}

	public Pergunta findPergunta(String parameter) {
		EntityManager em = EMF.createEntityManager();
		return em.find(Pergunta.class, Long.valueOf(parameter));
	}

	public List<Pergunta> listarPerguntas() {
		EntityManager em = EMF.createEntityManager();
		return em.createQuery("Select p From Pergunta p").getResultList();
	}

	public void apagarPergunta(String parameter) {
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		Pergunta p = em.find(Pergunta.class, Long.valueOf(parameter));
		em.remove(p);
		em.getTransaction().commit();
	}
}
