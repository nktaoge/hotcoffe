package br.com.goals.etrilhas.servlet.galeria;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.etrilhas.facade.GaleriaFacade;
import br.com.goals.etrilhas.modelo.Foto;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.etrilhas.servlet.BaseServlet;

public class GaleriaXml extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private static Logger logger  = Logger.getLogger(GaleriaServlet.class);
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
			Galeria galeria = galeriaFacade.selecionar(id);
			for(Foto foto:galeria.getFotos()){
				retorno+="<img href=\"" + foto.getUrlRelativaJpg() + "\">";
				retorno+=Template.htmlEncode(Template.htmlDecode(foto.getTxtDescricao().replace("<","&lt;").replace(">","&gt;"))).replace("&#13;","").replace("&#10;","")+"</img>";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		retorno+="</dados>";
		System.out.println(retorno);
		response.getWriter().write(retorno);
		response.getWriter().close();
	}
}
