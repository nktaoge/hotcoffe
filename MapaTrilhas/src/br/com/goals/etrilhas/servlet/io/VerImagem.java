package br.com.goals.etrilhas.servlet.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.dao.JdoFile;
import br.com.goals.jpa4google.PMF;

public class VerImagem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String basePath = "";
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		basePath = config.getInitParameter("basePath");
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fName = request.getRequestURI();
		int l = fName.lastIndexOf('/');
		fName = URLDecoder.decode(fName.substring(l + 1),"utf-8");
		PersistenceManager pm = PMF.getPersistenceManager();
		Query q = pm.newQuery("SELECT FROM " + JdoFile.class.getName() + " WHERE fileName == fname PARAMETERS String fname");
		List results = (List) q.execute(basePath + fName);
		if (results.size() == 0) {
			System.out.println("None for " + fName);
		} else {
			for (Object o : results) {
				JdoFile jdoFile = (JdoFile) o;
				response.setContentType("image/jpeg");
				SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
				response.setHeader("Last-Modified", format.format(new Date(1000000+jdoFile.getId()))+" GMT");
				response.setHeader("Etag", jdoFile.getId().toString());
				String etag = request.getHeader("If-None-Match");
				if(etag!=null && etag.equals(jdoFile.getId().toString())){
					response.setStatus(304);
				}else{
					OutputStream out = response.getOutputStream(); 
					out.write(jdoFile.getBytes().getBytes());
					out.close();
				}
				break;
			}
		}
		pm.close();
	}
}
