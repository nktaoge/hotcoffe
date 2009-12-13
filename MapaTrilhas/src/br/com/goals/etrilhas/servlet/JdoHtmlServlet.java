package br.com.goals.etrilhas.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.dao.JdoHtml;
import br.com.goals.jpa4google.PMF;

/**
 * Servlet implementation class JdoHtmlServlet
 */
public class JdoHtmlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JdoHtmlServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(BaseServlet.CHARACTER_ENCODING);
		response.setContentType("text/html");
		String fName = request.getRequestURI();
		int l = fName.lastIndexOf('/');
		fName = fName.substring(l+1);
		//System.out.println("Find " + fName);
		PersistenceManager pm = PMF.getPersistenceManager();
		Query q = pm.newQuery("SELECT FROM " + JdoHtml.class.getName() + " WHERE fileName == fname PARAMETERS String fname");
		List results = (List)q.execute(fName);
		if(results.size()==0){
			System.out.println("None");
			q = pm.newQuery(JdoHtml.class);
			results = (List)q.execute();
			for(Object o:results){
				JdoHtml jdoHtml = (JdoHtml) o;
				//System.out.println("fName = " + jdoHtml.getFileName());
			}
		}else{
			for(Object o:results){
				JdoHtml jdoHtml = (JdoHtml) o;
				String html = URLDecoder.decode(jdoHtml.getTxtHtml().getValue(),"utf-8");
				//System.out.println("html = " + html);
				PrintWriter pw = response.getWriter(); 
				pw.write(html);
				pw.close();
				break;
			}
		}
		pm.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
