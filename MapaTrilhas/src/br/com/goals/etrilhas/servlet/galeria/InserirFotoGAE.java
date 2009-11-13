package br.com.goals.etrilhas.servlet.galeria;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import br.com.goals.etrilhas.dao.BaseDao;
import br.com.goals.etrilhas.dao.JdoFile;
import br.com.goals.etrilhas.facade.GaleriaFacade;
import br.com.goals.etrilhas.modelo.Foto;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.etrilhas.servlet.BaseServlet;
import br.com.goals.jpa4google.PMF;

import com.google.appengine.api.datastore.Blob;

/**
 * Servlet implementation class InserirFoto
 */
public class InserirFotoGAE extends BaseServlet {
	private static Logger logger = Logger.getLogger(InserirFotoGAE.class);
	private static final long serialVersionUID = 1L;
	private BaseDao<Foto> fotoDao = new BaseDao<Foto>();
	private File fotosDir = null;
	private static GaleriaFacade galeriaFacade = GaleriaFacade.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InserirFotoGAE() {
		super();

	}

	protected File getFotosDir() {
		if (fotosDir == null) {
			fotosDir = new File(fotoDao.getBasePath());
			fotosDir = fotosDir.getParentFile();
			fotosDir = new File(fotosDir, "media" + File.separatorChar + "fotos");
			if (!fotosDir.exists()) {
				fotosDir.mkdirs();
			}
			logger.debug("FotosDir = " + fotosDir.getAbsolutePath());
		}
		return fotosDir;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@SuppressWarnings("unchecked")
	protected void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			Long galeriaId = null;
			String descricao = "";
			String urlRelativa = "";
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
					String fieldName = item.getFieldName();
					String value;
					value = baos.toString();
					logger.debug(fieldName + " = " + value);
					if (fieldName.equals("descricao")) {
						descricao = value;
					} else if (fieldName.equals("galeriaId")) {
						galeriaId = Long.parseLong(value);
					}
				} else {
					// Salvando
					JdoFile jdoFile = new JdoFile();
					jdoFile.setFileName(item.getName());
					jdoFile.setBytes(new Blob(baos.toByteArray()));
					PersistenceManager pm = PMF.getPersistenceManager();
					try {
						pm.makePersistent(jdoFile);
					} finally {
						pm.close();
					}
					urlRelativa = "media/fotos/" + item.getName();
				}
			}

			Galeria galeria = galeriaFacade.selecionar(galeriaId);
			Foto foto = new Foto();
			foto.setTxtDescricao(descricao);
			foto.setUrlRelativaJpg(urlRelativa);
			if (galeria.getFotos() == null) {
				galeria.setFotos(new ArrayList<Foto>());
			}
			galeria.getFotos().add(foto);
			// TODO salvar a descricao usando um template
			galeriaFacade.atualizar(galeria);
			response.sendRedirect("GaleriaServlet?galeriaId=" + galeriaId);
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doWork(request, response);
	}
}
