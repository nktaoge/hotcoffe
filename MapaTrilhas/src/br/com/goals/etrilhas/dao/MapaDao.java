package br.com.goals.etrilhas.dao;

import javax.jdo.PersistenceManager;

import org.apache.log4j.Logger;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.jpa4google.PMF;

public class MapaDao extends BaseDao<Mapa>{
	private static Logger logger = Logger.getLogger(MapaDao.class);
	private static GaleriaDao galeriaDao = new GaleriaDao();
	public MapaDao(){
		
	}
	protected Mapa selecionarGoogle(Long id){
		PersistenceManager pm = PMF.getPersistenceManager();
		try{
			Mapa mapa = selecionarGoogle(pm,id); 
			return mapa;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			pm.close();
		}
	}
	public void atualizar(Mapa mapa) throws Exception {
		logger.debug("Atualizando o mapa...");
		for(Camada camada:mapa.getCamadas()){
			for(MapaItem mapaItem:camada.getItems()){
				logger.debug("Verificando o valor do item " + mapaItem.getNome());
				if(mapaItem.getValor()!=null){
					logger.debug("Class " + mapaItem.getValor().getClass().getCanonicalName());
					if(mapaItem.getValor() instanceof Galeria){
						Galeria galeria = (Galeria) mapaItem.getValor();
						if(galeria.getId()==null){
							galeria.setId(getNextId());
							galeriaDao.criar(galeria);
							logger.debug("Criada a galeria id " + galeria.getId());
						}else{
							galeriaDao.atualizar(galeria);
							logger.debug("Atualizada a galeria id " + galeria.getId());
						}
					}
				}else{
					logger.warn("Item " + mapaItem.getNome() + " mapaItem.getValor() = null");
				}
			}
		}
		super.atualizar(mapa);
	}
}
