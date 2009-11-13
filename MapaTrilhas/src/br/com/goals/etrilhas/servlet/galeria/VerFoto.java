package br.com.goals.etrilhas.servlet.galeria;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.dao.JdoFile;
import br.com.goals.etrilhas.dao.JdoHtml;
import br.com.goals.jpa4google.PMF;

public class VerFoto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fName = request.getRequestURI();
		int l = fName.lastIndexOf('/');
		fName = fName.substring(l + 1);
		// System.out.println("Find " + fName);
		PersistenceManager pm = PMF.getPersistenceManager();
		Query q = pm.newQuery("SELECT FROM " + JdoFile.class.getName() + " WHERE fileName == fname PARAMETERS String fname");
		List results = (List) q.execute(fName);
		if (results.size() == 0) {
			System.out.println("None");
			q = pm.newQuery(JdoHtml.class);
			results = (List) q.execute();
			for (Object o : results) {
				JdoFile jdoHtml = (JdoFile) o;
				// System.out.println("fName = " + jdoHtml.getFileName());
			}
		} else {
			for (Object o : results) {
				JdoFile jdoFile = (JdoFile) o;
				OutputStream out = response.getOutputStream(); 
				out.write(jdoFile.getBytes().getBytes());
				out.close();
				break;
			}
		}
		pm.close();
	}
}
