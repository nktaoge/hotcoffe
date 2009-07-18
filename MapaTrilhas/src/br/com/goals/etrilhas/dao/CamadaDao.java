package br.com.goals.etrilhas.dao;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;

public class CamadaDao {
	public Camada selecionar(Mapa mapa,Long id) throws Exception{
		for(Camada tmp:mapa.getCamadas()){
			if(tmp.getId().equals(id)){
				return tmp;
			}
		}
		throw new Exception("Camada id " + id + " not found.");
	}
}
