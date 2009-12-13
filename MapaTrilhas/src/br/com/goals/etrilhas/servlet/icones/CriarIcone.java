package br.com.goals.etrilhas.servlet.icones;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import br.com.goals.cafeina.view.tmp.StringUtils;
import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.etrilhas.dao.JdoFile;
import br.com.goals.etrilhas.servlet.BaseServletV2;
import br.com.goals.jpa4google.PMF;

import com.google.appengine.api.datastore.Blob;

public class CriarIcone extends BaseServletV2 {
	private static final long serialVersionUID = 1L;

	private String basePath="media/icones/";
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mensagem="";
		try {
			
			FileItemFactory fif = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(fif);
			response.setContentType("text/plain");
			FileItemIterator iterator = upload.getItemIterator(request);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len;
				byte[] buffer = new byte[8192];
				while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
					baos.write(buffer, 0, len);
				}
				if (item.isFormField()) {
				} else {
					// Salvando
					String fName = basePath + StringUtils.removePontuacao(item.getName()).replace(' ', '-');
					JdoFile jdoFile = new JdoFile();
					jdoFile.setFileName(fName);
					jdoFile.setBytes(new Blob(baos.toByteArray()));
					PersistenceManager pm = PMF.getPersistenceManager();
					try {
						// remover antigos?
						Query q = pm.newQuery("SELECT FROM " + JdoFile.class.getName() + " WHERE fileName == fname PARAMETERS String fname");
						List<JdoFile> results = (List) q.execute(fName);
						int apagados = 0;
					
						for (JdoFile i : results) {
							pm.deletePersistent(i);
							apagados++;
						}
						pm.makePersistent(jdoFile);
						if(apagados>0){
							mensagem+="Arquivo substituido '" + jdoFile.getFileName() + "'.\n";
						}else{
							mensagem+="Arquivo gravado '" + jdoFile.getFileName() + "'.\n";
						}
					} finally {
						pm.close();
					}
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
		Template template = getTemplate(request);
		template.setMensagem(mensagem);
		response.setContentType("text/html");
		response.getWriter().write(template.toString());
		response.getWriter().close();
	}
}
