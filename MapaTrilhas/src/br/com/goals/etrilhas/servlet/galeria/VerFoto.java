package br.com.goals.etrilhas.servlet.galeria;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.etrilhas.dao.JdoFile;
import br.com.goals.jpa4google.PMF;

public class VerFoto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fName = request.getRequestURI();
		int l = fName.lastIndexOf('/');
		fName = URLDecoder.decode(fName.substring(l + 1),"utf-8");
		// System.out.println("Find " + fName);
		PersistenceManager pm = PMF.getPersistenceManager();
		Query q = pm.newQuery("SELECT FROM " + JdoFile.class.getName() + " WHERE fileName == fname PARAMETERS String fname");
		List results = (List) q.execute(fName);
		if (results.size() == 0) {
			System.out.println("None for " + fName);
			q = pm.newQuery(JdoFile.class);
			results = (List) q.execute();
			//for (Object o : results) {
				//JdoFile jdoHtml = (JdoFile) o;
				// System.out.println("fName = " + jdoHtml.getFileName());
			//}
		} else {
			for (Object o : results) {
				JdoFile jdoFile = (JdoFile) o;
				/*
				seconds, minutes, hours, days
				$expires = 60*60*24*14;
				header("Pragma: public");
				header("Cache-Control: maxage=".$expires);
				header('Expires: ' . gmdate('D, d M Y H:i:s', time()+$expires) . ' GMT');
				 */
				//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
				//format.setCalendar(cal);
				//java.util.Date date = format.parse("2003-01-25 00:15:30");
				//System.out.println(date); 
				//long expires = 60*60*24*2;//2 minutos?
				response.setContentType("image/jpeg");
				//response.setHeader("Pragma", "public");
				//response.setHeader("Cache-Control","maxage="+expires);
				SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
				//response.setHeader("Last-Modified", format.format(new Date(new Date().getTime()+expires))+" GMT");
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
