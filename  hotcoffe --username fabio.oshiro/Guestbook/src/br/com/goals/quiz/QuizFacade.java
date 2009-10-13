package br.com.goals.quiz;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
		EntityTransaction t = null;
		try{
			t = em.getTransaction();
			t.begin();
			if(pergunta.getId()!=null){
				em.merge(pergunta);
				for(Opcao o:pergunta.getOpcoes()){
					em.merge(o);
				}
			}else{
				em.persist(pergunta);
				for(int i=0;i<5;i++){
					Opcao opcao = new Opcao();
					opcao.setCorreta(false);
					opcao.setTexto("Digite a opção");
					opcao.setPergunta(pergunta);
					pergunta.getOpcoes().add(opcao);
					em.persist(opcao);
				}
			}
			em.flush();
			t.commit();
		}catch(Exception e){
			e.printStackTrace();
			t.rollback();
		}finally{
			em.close();
		}
	}

	private void salvar(Opcao opcao) {
		EntityManager em = EMF.createEntityManager();
		EntityTransaction t = null;
		try{
			t = em.getTransaction();
			t.begin();
			if(opcao.getId()!=null){
				em.merge(opcao);
			}else{
				em.persist(opcao);
			}
			System.out.println("call commit...");
			em.flush();
			t.commit();
			System.out.println("Commited");
		}catch(Exception e){
			e.printStackTrace();
			t.rollback();
		}finally{
			em.close();
		}
	}

	public Pergunta findPergunta(String parameter) {
		EntityManager em = EMF.createEntityManager();
		try{
			Pergunta p = em.find(Pergunta.class, Long.valueOf(parameter));
			//Inicializar
			for(Opcao o:p.getOpcoes()) o.getTexto();
			return p;
		}finally{
			em.close();
		}
	}

	public List<Pergunta> listarPerguntas() {
		EntityManager em = EMF.createEntityManager();
		try{
			List<Pergunta> list = em.createQuery("Select p From Pergunta p").getResultList();
			list.size();
			return list;
		}finally{
			em.close();
		}
	}

	/**
	 * Apaga a pergunta e as opcoes
	 * @param parameter Id
	 */
	public void apagarPergunta(String parameter) {
		EntityManager em = EMF.createEntityManager();
		try{
			em.getTransaction().begin();
			Pergunta p = em.find(Pergunta.class, Long.valueOf(parameter));
			for(Opcao o:p.getOpcoes()){
				em.remove(o);
			}
			em.remove(p);
			em.getTransaction().commit();
		}finally{
			em.close();
		}
	}
}
