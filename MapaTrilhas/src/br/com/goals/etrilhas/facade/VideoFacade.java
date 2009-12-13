package br.com.goals.etrilhas.facade;

import java.util.List;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.etrilhas.modelo.Video;

public class VideoFacade extends BaseFacade<Video>{
	private static VideoFacade instance;
	private VideoFacade(){
		
	}
	public static VideoFacade getInstance() {
		if(instance==null){
			instance = new VideoFacade();
		}
		return instance;
	}
	@Override
	public void atualizar(Video obj) throws FacadeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void criar(Video obj) throws FacadeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Video> listar() throws FacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Video selecionar(long id) throws FacadeException {
		// TODO Auto-generated method stub
		return null;
	}
	public Video selecionar(Mapa mapa, Long id) {
		for(Camada camada:mapa.getCamadas()){
			//atualizar o antigo
			for(MapaItem mItem:camada.getItems()){
				if(mItem.getId().equals(id)){
					return (Video)mItem.getValor();
				}
			}
		}
		return null;
	}
}
