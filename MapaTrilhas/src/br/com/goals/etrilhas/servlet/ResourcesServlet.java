package br.com.goals.etrilhas.servlet;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Captura os recursos do servidor
 */
public class ResourcesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResourcesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = this.getClass().getClassLoader().getResource("Configuracao.properties").getPath();
		File file = new File(path);
		File media = new File(file.getParentFile().getParentFile().getParentFile(),"media");
		if(!media.exists()){
			media.mkdirs();
		}
		File files[] = media.listFiles();
		String fileNodes = "<arquivos>";
		for (int i = 0; i < files.length; i++) {
			path = files[i].getAbsolutePath()
				.replace(media.getAbsolutePath(), "/media")
				.replace("\\","/");
			fileNodes+="<arquivo path=\"" + path + "\" type=\"";
			if(files[i].isDirectory()){
				fileNodes+="d";
			}else if(file.isFile()){
				fileNodes+="f";
			}
			fileNodes+="\" />";
		}
		fileNodes+="</arquivos>";
		response.getWriter().write(fileNodes);
		response.getWriter().close();
	}

}
