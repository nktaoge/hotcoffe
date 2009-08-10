package br.com.goals.etrilhas.facade;

import java.util.Date;
import java.util.List;

import br.com.goals.etrilhas.dao.BaseDao;
import br.com.goals.etrilhas.dao.CamadaDao;
import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;

public class CamadaFacade {
	private static CamadaFacade instance = new CamadaFacade();
	private BaseDao<Mapa> mapaDao = new BaseDao<Mapa>();
	private CamadaDao camadaDao = new CamadaDao();
	private CamadaFacade(){}
	public static CamadaFacade getInstance() {
		return instance ;
	}
	
	public void criar(Camada camada, Mapa mapa) throws FacadeException{
		camada.setId(BaseDao.getNextId());
		mapa.getCamadas().add(camada);
		try{
			mapaDao.atualizar(mapa);
		}catch(Exception e){
			e.printStackTrace();
			throw new FacadeException(e.getMessage());
		}
	}
	/**
	 * 
	 * @param camada
	 * @param mapa
	 * @throws FacadeException
	 */
	public void atualizar(Camada camada, Mapa mapa) throws FacadeException{
		try{
			boolean achou = false;
			List<Camada> camadas = mapa.getCamadas();
			for(int i=0;i<camadas.size();i++){
				Camada cam = camadas.get(i);
				if(cam.getId().equals(camada.getId())){
					cam.setNome(camada.getNome());
					cam.setDescricao(camada.getDescricao());
					achou = true;
					break;
				}
			}
			if(!achou){
				throw new FacadeException("Camada com id " + camada.getId() + " nao foi encontrada.");
			}else{
				mapaDao.atualizar(mapa);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new FacadeException(e.getMessage());
		}
	}
	public Camada selecionar(Mapa mapa, Long id) throws FacadeException {
		try {
			return camadaDao.selecionar(mapa, id);
		} catch (Exception e) {
			throw new FacadeException(e.getMessage());
		}
	}
	public void apagar(Mapa mapa, Long id) throws FacadeException {
		try {
			camadaDao.apagar(mapa, id);
			MapaFacade.getInstance().atualizar(mapa);
		} catch (Exception e) {
			throw new FacadeException(e.getMessage());
		}
	}
	
}
