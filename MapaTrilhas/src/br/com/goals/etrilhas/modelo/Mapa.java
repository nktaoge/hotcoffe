package br.com.goals.etrilhas.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Mapa extends Base{
	private static final long serialVersionUID = -5227948201808476322L;
	private String imagemVetorial;
	private String imagemSatelite;
	private List<Camada> camadas = new ArrayList<Camada>();
	private Comparator<Camada> comparador = new Comparator<Camada>(){
		public int compare(Camada o1, Camada o2) {
			try{
				return o1.getOrdem()-o2.getOrdem();
			}catch(Exception e){
				return 0;
			}
		}		
	};
	public List<Camada> getCamadas() {
		Collections.sort(camadas,comparador );
		return camadas;
	}
	public void setCamadas(List<Camada> camadas) {
		this.camadas = camadas;
	}
	public String getImagemVetorial() {
		return imagemVetorial;
	}
	public void setImagemVetorial(String imagemVetorial) {
		this.imagemVetorial = imagemVetorial;
	}
	public String getImagemSatelite() {
		return imagemSatelite;
	}
	public void setImagemSatelite(String imagemSatelite) {
		this.imagemSatelite = imagemSatelite;
	}
	
}
