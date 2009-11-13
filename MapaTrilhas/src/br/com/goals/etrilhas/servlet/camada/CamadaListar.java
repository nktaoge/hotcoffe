package br.com.goals.etrilhas.servlet.camada;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.servlet.BaseServlet;
import br.com.goals.cafeina.view.tmp.AreaNaoEncontradaException;
import br.com.goals.cafeina.view.tmp.RsItemCustomizado;
import br.com.goals.cafeina.view.tmp.Template;

public class CamadaListar extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    public CamadaListar() {
        super();
    }
    public Template getTemplate(HttpServletRequest request) throws IOException{
    	Template template = super.getTemplate(request);
		template.setRsItemCustomizado(new RsItemCustomizado(){
			public String tratar(Object o, String item) {
				if(o instanceof Camada){
					Camada camada = (Camada) o;
					Template temp = new Template();
					temp.setTemplate(item);
					try {
						temp.setLink("editar", "camadaEditar?id=" + camada.getId());
						temp.setLink("apagar", "CamadaApagar?id=" + camada.getId());
						temp.setLink("pontos", "MapaItemListar?camada.id=" + camada.getId());
					} catch (AreaNaoEncontradaException e) {
						e.printStackTrace();
					}
					return temp.toString();
				}else{
					return item;
				}
			}		
		});
		return template;
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Montar resposta para o usuario
		Template template = getTemplate(request);
		response.setCharacterEncoding(CHARACTER_ENCODING);
		try{
			Mapa mapa = getMapa(request);
			template.encaixaResultSet(mapa.getCamadas());
			template.setMensagem("");
		}catch(Exception e){
			e.printStackTrace();
			template.setMensagem("Erro: "+ e.getMessage());
		}
		response.getWriter().write(template.toString());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Montar resposta para o usuario
		Template template = getTemplate(request);
		response.setCharacterEncoding(CHARACTER_ENCODING);
		try{
			Mapa mapa = getMapa(request);
			for(Camada camada: mapa.getCamadas()){
				try{
					int ordem = Integer.parseInt(request.getParameter("Camada_"+camada.getId()+".ordem"));
					camada.setOrdem(ordem);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			template.encaixaResultSet(mapa.getCamadas());

			mapaFacade.atualizar(mapa);
			template.setMensagem("Ordem alterada.");
		}catch(Exception e){
			e.printStackTrace();
			template.setMensagem("Erro: " + e.getMessage());
		}
		response.getWriter().write(template.toString());
	}
}
