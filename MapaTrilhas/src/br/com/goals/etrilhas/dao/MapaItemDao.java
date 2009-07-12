package br.com.goals.etrilhas.dao;

import java.util.List;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;

public class MapaItemDao extends BaseDao<MapaItem> {
	@Override
	public void criar(MapaItem obj) throws Exception {
		System.out.println("obj.getY() = " + obj.getY());
		super.criar(obj);
	}

	public List<MapaItem> listar() throws Exception{
		return super.listar();
	}
	public MapaItem selecionar(Mapa mapa,Long id) throws Exception{
		for(Camada camada:mapa.getCamadas()){
			for(MapaItem mapaItem:camada.getItems()){
				if(mapaItem.getId().equals(id)){
					return mapaItem;
				}
			}
		}
		throw new Exception("Item com o id "+ id + " nao encontrado.");
	}
}
