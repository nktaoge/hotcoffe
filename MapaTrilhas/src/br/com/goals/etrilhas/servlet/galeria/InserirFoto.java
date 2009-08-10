package br.com.goals.etrilhas.servlet.galeria;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.log4j.Logger;

import br.com.goals.etrilhas.dao.BaseDao;
import br.com.goals.etrilhas.modelo.Foto;
import br.com.goals.etrilhas.servlet.BaseServlet;

/**
 * Servlet implementation class InserirFoto
 */
public class InserirFoto extends BaseServlet {
	private static Logger logger = Logger.getLogger(InserirFoto.class);
	private static final long serialVersionUID = 1L;
	private BaseDao<Foto> fotoDao = new BaseDao<Foto>();
	private File fotosDir = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InserirFoto() {
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TODO verificar se podemos sobreescrever
		if (FileUpload.isMultipartContent(request)) {
			try {
				DiskFileUpload diskFileUpload = new DiskFileUpload();
				diskFileUpload.setSizeMax(1000000);
				diskFileUpload.setSizeThreshold(4096);
				List<FileItem> fileItens = diskFileUpload.parseRequest(request);
				String descricao = "";
				for(FileItem fileItem:fileItens){
					if (fileItem.isFormField()) {
						logger.debug(fileItem.getFieldName()+" = " +fileItem.getString());
						descricao = fileItem.getString();
					}else{
						File subindo = new File(fileItem.getName());
						File arquivo = new File(getFotosDir(),subindo.getName());
						fileItem.write(arquivo);
					}
				}
				//TODO salvar a descricao usando um template
			} catch (Exception e) {
				throw new ServletException(e.getMessage(), e);
			}
		}
	}

}
