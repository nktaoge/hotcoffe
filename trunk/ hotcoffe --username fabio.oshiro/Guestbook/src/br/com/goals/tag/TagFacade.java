package br.com.goals.tag;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.goals.persistencia.EMF;
import br.com.goals.persistencia.PMF;
import br.com.goals.tag.vo.Tag;

/**
 * Sistemas de tags
 * 
 * @author fabio
 * 
 */
public class TagFacade {
	private static final Logger logger = Logger.getLogger(TagFacade.class.getName());

	private static TagFacade instance = null;
	private TagFacade(){
		
	}
	public static TagFacade getInstance(){
		if(instance==null){
			instance = new TagFacade();
		}
		return instance;
	}
	private Tag salvar(String nome) {
		EntityManager em = EMF.createEntityManager();
		Tag tag = new Tag();
		tag.setNome(nome);
		try {
			em.getTransaction().begin();
			em.persist(tag);
			em.flush();
			em.getTransaction().commit();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try{
				em.close();
			}catch(Exception e){}
		}
		logger.info("saved " + nome + " id " + tag.getId());
		return tag;
	}

	@SuppressWarnings("unchecked")
	public Long getTagId(String nome) {
		EntityManager em = EMF.createEntityManager();
		Query query = em.createQuery("Select t From Tag t where t.nome=:Nome").setParameter("Nome",nome);
		
		try {
			List<Tag> results = query.getResultList();
			if (results.size() > 0) {
				logger.info("found");
				return results.get(0).getId();
			} else {
				logger.info("not found");
				return salvar(nome).getId();
			}
		} finally {
			em.close();
		}
	}
}
