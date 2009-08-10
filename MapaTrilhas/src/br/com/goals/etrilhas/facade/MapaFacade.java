package br.com.goals.etrilhas.facade;

import java.util.List;

import br.com.goals.etrilhas.dao.BaseDao;
import br.com.goals.etrilhas.dao.MapaDao;
import br.com.goals.etrilhas.modelo.Mapa;

public class MapaFacade extends BaseFacade<Mapa>{
	private MapaDao mapaDao = new MapaDao();
	private static MapaFacade instance = new MapaFacade();
	private MapaFacade(){}
	public static MapaFacade getInstance() {
		return instance;
	}
	
	private void verificar(Mapa mapa) throws FacadeException{
		
	}
	

	@Override
	public void atualizar(Mapa mapa) throws FacadeException {
		try {
			verificar(mapa);
			mapaDao.atualizar(mapa);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FacadeException("Erro ao atualizar: " + e.getMessage());
		}
		
	}

	@Override
	public void criar(Mapa mapa) throws FacadeException {
		try {
			verificar(mapa);
			mapaDao.criar(mapa);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FacadeException("Erro ao salvar: " + e.getMessage());
		}
	}

	@Override
	public List<Mapa> listar() throws FacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Sempre retorna, se nao existe ele cria um
	 */
	@Override
	public Mapa selecionar(long id) throws FacadeException {
		try{
			return mapaDao.selecionar(id);
		}catch(Exception e){
			Mapa mapa = new Mapa();
			mapa.setId(id);
			try {
				mapaDao.atualizar(mapa);
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new FacadeException(e1.getMessage());
			}
			return mapa;
		}
	}
}
