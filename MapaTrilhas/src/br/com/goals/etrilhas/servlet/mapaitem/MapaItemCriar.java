package br.com.goals.etrilhas.servlet.mapaitem;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.etrilhas.servlet.MapaItemDefinir;
import br.com.goals.cafeina.view.tmp.Template;

public class MapaItemCriar extends MapaItemDefinir {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = null;
		template = getTemplate(request);
		/*
		 * Verificar se possui um tipo
		 */
		try{
			Mapa mapa = getMapa(request);
			MapaItem mapaItem = new MapaItem();
			template.set("id",null);
			carregarTemplate(template, mapaItem, mapa);
			template.setMensagem("");
		}catch(Exception e){
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
	}
}
