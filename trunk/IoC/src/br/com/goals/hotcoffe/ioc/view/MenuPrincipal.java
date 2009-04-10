package br.com.goals.hotcoffe.ioc.view;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;

public class MenuPrincipal {
	private String menu=null;
	private UmCasoDeUso umCasoDeUso;
	public MenuPrincipal(UmCasoDeUso umCasoDeUso) {
		this.umCasoDeUso = umCasoDeUso;
	}
	public String toString(){
		if(menu==null){
			try {
				menu="<ul>";
				Class[] lista = umCasoDeUso.getControlador().getListCasosDeUso();
				for (int i = 0; i < lista.length; i++) {
					menu+="<li><a href=\"" + lista[i].getSimpleName() + "\">" + Template.getLabel(lista[i]) +"</a></li>";
				}
				menu+="</ul>";
			} catch (ClassNotFoundException e) {
				menu = "Sem menu!";
			}
		}
		return menu;
	}
}
