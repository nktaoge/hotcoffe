package br.com.goals.etrilhas.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.template.RsItemCustomizado;
import br.com.goals.template.Template;

public class CamadaListar extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    public CamadaListar() {
        super();
    }
    private Template getTemplate() throws IOException{
    	Template template = new Template();
		template.setTemplateFile("camadaListar.html");
		template.setRsItemCustomizado(new RsItemCustomizado(){
			public String tratar(Object o, String item) {
				if(o instanceof Camada){
					Camada camada = (Camada) o;
					Template temp = new Template();
					temp.setTemplate(item);
					temp.setLink("editar", "camadaEditar?id=" + camada.getId());
					temp.setLink("apagar", "camadaApagar?id=" + camada.getId());
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
		Template template = getTemplate();
		try{
			Mapa mapa = getMapa(request);
			template.encaixaResultSet(mapa.getCamadas());
			template.setArea("mensagem","");
		}catch(Exception e){
			e.printStackTrace();
			try{
				template.setArea("mensagem",e.getMessage());
			}catch(Exception e2){}
		}
		response.getWriter().write(template.toString());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Montar resposta para o usuario
		Template template = getTemplate();
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
			template.setArea("mensagem","Ordem alterada");
		}catch(Exception e){
			e.printStackTrace();
			try{
				template.setArea("mensagem",e.getMessage());
			}catch(Exception e2){}
		}
		response.getWriter().write(template.toString());
	}
}
