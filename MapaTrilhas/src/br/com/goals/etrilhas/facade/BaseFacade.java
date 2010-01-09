package br.com.goals.etrilhas.facade;

import java.util.List;

public abstract class BaseFacade<T> {
	public abstract void criar(T obj) throws FacadeException;
	public abstract void atualizar(T obj) throws FacadeException;
	public abstract T selecionar(long id) throws FacadeException;
	public abstract List<T> listar() throws FacadeException;
}
