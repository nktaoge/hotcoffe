package br.com.goals.etrilhas.servlet.galeria;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.etrilhas.facade.GaleriaFacade;
import br.com.goals.etrilhas.facade.MapaFacade;
import br.com.goals.etrilhas.facade.MapaItemFacade;
import br.com.goals.etrilhas.modelo.Foto;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.etrilhas.servlet.BaseServlet;

public class GaleriaXml extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private static Logger logger  = Logger.getLogger(GaleriaXml.class);
	private static GaleriaFacade galeriaFacade = GaleriaFacade.getInstance();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String retorno="<dados>";
		try {
			String fName = request.getRequestURI();
			int l = fName.lastIndexOf('-');
			int fim = fName.lastIndexOf(".");
			fName = fName.substring(l+1, fim);
			Long id = Long.parseLong(fName);
			Mapa mapa = MapaFacade.getInstance().selecionar(1l);
			MapaItem mapaItem = MapaItemFacade.getInstance().selecionar(mapa, id);
			Galeria galeria = galeriaFacade.selecionar(mapaItem.getGaleriaId());
			logger.debug("galeria desc = "+galeria.getTxtDescricao());
			String descricaoPadrao = encodeData(galeria.getTxtDescricao());
			for(Foto foto:galeria.getFotos()){
				retorno+="<img href=\"" + foto.getUrlRelativaJpg() + "\">";
				if(foto.getTxtDescricao()==null || foto.getTxtDescricao().trim().equals("")){
					retorno+=descricaoPadrao+"</img>";
				}else{
					retorno+=encodeData(foto.getTxtDescricao())+"</img>";
				}
			}
		} catch (Exception e) {
			logger.error("Erro ao gerar GaleriaXml",e);
		}
		retorno+="</dados>";
		System.out.println(retorno);
		response.getWriter().write(retorno);
		response.getWriter().close();
	}
	private String encodeData(String desc) {
		return Template.htmlEncode(Template.htmlDecode(desc.replace("<","&lt;").replace(">","&gt;"))).replace("&#13;","").replace("&#10;","");
	}
}
