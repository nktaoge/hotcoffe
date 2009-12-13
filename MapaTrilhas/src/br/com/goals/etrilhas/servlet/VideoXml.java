package br.com.goals.etrilhas.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.etrilhas.modelo.Video;

/**
 * Servlet implementation class MapaServlet
 */
public class VideoXml extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VideoXml() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String retorno="<dados>";
		try {
			String fName = request.getRequestURI();
			int l = fName.lastIndexOf('-');
			int fim = fName.lastIndexOf(".");
			fName = fName.substring(l+1, fim);
			Long id = Long.parseLong(fName);
			Video video = videoFacade.selecionar(getMapa(request),id);
			retorno+="<vid yid=\"" +	getYoutubeId(video.getTxtCodYoutube()) + "\">";
			retorno+=Template.htmlEncode(Template.htmlDecode(video.getHtmlDescricao().replace("<","&lt;").replace(">","&gt;"))).replace("&#13;","").replace("&#10;","")+"</vid>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		retorno+="</dados>";
		System.out.println(retorno);
		response.getWriter().write(retorno);
		response.getWriter().close();
	}

	private String getYoutubeId(String codYouTube){
		String begin = "http://www.youtube.com/v/";
		int i = codYouTube.indexOf(begin);
		if(i!=-1){
			i+=begin.length();
			int f = codYouTube.indexOf("&",i);
			return codYouTube.substring(i, f);
		}
		return "w1GQIrl4QaE";
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
