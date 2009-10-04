package br.com.goals.etrilhas.dao;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;

public class MapaDao extends BaseDao<Mapa>{
	
	private static GaleriaDao galeriaDao = new GaleriaDao();
	public MapaDao(){
		
	}
	
	public void atualizar(Mapa mapa) throws Exception {
		for(Camada camada:mapa.getCamadas()){
			for(MapaItem mapaItem:camada.getItems()){
				if(mapaItem.getValor()!=null && mapaItem.getValor() instanceof Galeria){
					Galeria galeria = (Galeria) mapaItem.getValor();
					if(galeria.getId()==null){
						galeria.setId(getNextId());
						galeriaDao.criar(galeria);
					}else{
						galeriaDao.atualizar(galeria);
					}
				}
			}
		}
		super.atualizar(mapa);
	}
}
