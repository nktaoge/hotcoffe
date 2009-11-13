package br.com.goals.etrilhas.servlet.galeria;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import br.com.goals.etrilhas.facade.GaleriaFacade;
import br.com.goals.etrilhas.modelo.Foto;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.etrilhas.servlet.BaseServlet;

/**
 * Servlet implementation class InserirFoto
 */
public class InserirFotoTomcat extends BaseServlet {
	private static Logger logger = Logger.getLogger(InserirFotoTomcat.class);
	private static final long serialVersionUID = 1L;
	private BaseDao<Foto> fotoDao = new BaseDao<Foto>();
	private File fotosDir = null;
	private static GaleriaFacade galeriaFacade = GaleriaFacade.getInstance();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InserirFotoTomcat() {
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
		
		
		//TODO verificar se podemos sobreescrever o arquivo
		if (FileUpload.isMultipartContent(request)) {
			try {
				Long galeriaId = null;
				
				
				DiskFileUpload diskFileUpload = new DiskFileUpload();
				diskFileUpload.setSizeMax(1000000);
				diskFileUpload.setSizeThreshold(4096);
				List<FileItem> fileItens = diskFileUpload.parseRequest(request);
				String descricao = "";
				String urlRelativa = "";
				for(FileItem fileItem:fileItens){
					if (fileItem.isFormField()) {
						if(fileItem.getFieldName().equals("descricao")){
							logger.debug(fileItem.getFieldName()+" = " +fileItem.getString());
							descricao = fileItem.getString();
						}else if(fileItem.getFieldName().equals("galeriaId")){
							galeriaId = Long.parseLong(fileItem.getString());
						}
					}else{
						File subindo = new File(fileItem.getName());
						File arquivo = new File(getFotosDir(),subindo.getName());
						fileItem.write(arquivo);
						urlRelativa = "media/fotos/" + subindo.getName();
					}
				}
				
				
				Galeria galeria = galeriaFacade.selecionar(galeriaId);
				Foto foto = new Foto();
				foto.setTxtDescricao(descricao);
				foto.setUrlRelativaJpg(urlRelativa);
				if(galeria.getFotos()==null){
					galeria.setFotos(new ArrayList<Foto>());
				}
				galeria.getFotos().add(foto);
				//TODO salvar a descricao usando um template
				galeriaFacade.atualizar(galeria);
				response.sendRedirect("GaleriaServlet?galeriaId="+galeriaId);
			} catch (Exception e) {
				logger.error("Erro inesperado",e);
				throw new ServletException(e.getMessage(), e);
			}
		}
	}
}
