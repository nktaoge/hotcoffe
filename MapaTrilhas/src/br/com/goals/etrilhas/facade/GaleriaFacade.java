package br.com.goals.etrilhas.facade;

import java.util.List;

import br.com.goals.etrilhas.dao.BaseDao;
import br.com.goals.etrilhas.modelo.Galeria;

public class GaleriaFacade extends BaseFacade<Galeria>{

	private BaseDao<Galeria> galeriaDao = new BaseDao<Galeria>();
	private static GaleriaFacade instance = new GaleriaFacade();

	private GaleriaFacade(){
		
	}
	
	@Override
	public void atualizar(Galeria obj) throws FacadeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void criar(Galeria obj) throws FacadeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List listar() throws FacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Galeria selecionar(long id) throws FacadeException {
		try{
			return galeriaDao.selecionar(id);
		}catch(Exception e){
			throw new FacadeException(e.getMessage());
		}
	}

	public static GaleriaFacade getInstance() {
		return instance ;
	}

}
