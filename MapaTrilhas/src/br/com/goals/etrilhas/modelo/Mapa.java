package br.com.goals.etrilhas.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Mapa extends Base implements Serializable{
	private static final long serialVersionUID = -5227948201808476322L;
	private String imagemVetorial;
	private String imagemSatelite;
	private String txtCoordenadasLatLng;
	private List<Camada> camadas = new ArrayList<Camada>();
	private static Comparator<Camada> comparador = new Comparator<Camada>(){
		public int compare(Camada o1, Camada o2) {
			try{
				return o1.getOrdem()-o2.getOrdem();
			}catch(Exception e){
				return 0;
			}
		}		
	};
	
	public String getTxtCoordenadasLatLng() {
		return txtCoordenadasLatLng;
	}
	
	/**
	 * Padr&atilde;o:
	 * <pre>
	 * latitude longitude altitude
	 * 22.22222 33.333333 4.444444
	 * 22.22222 33.333333 4.444444
	 * </pre>
	 * @param txtCoordenadasLatLng String
	 */
	public void setTxtCoordenadasLatLng(String txtCoordenadasLatLng) {
		this.txtCoordenadasLatLng = txtCoordenadasLatLng;
	}
	
	public List<Camada> getCamadas() {
		Collections.sort(camadas,comparador);
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
