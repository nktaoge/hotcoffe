package br.com.goals.jpa4google;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Jpa4GoogleEntityManagerFactory implements EntityManagerFactory{

	@Override
	public void close() {
		
	}

	@Override
	public EntityManager createEntityManager() {
		return new Jpa4GoogleEntityManager();
	}

	@Override
	public EntityManager createEntityManager(Map arg0) {
		return null;
	}

	@Override
	public boolean isOpen() {
		return false;
	}

}
