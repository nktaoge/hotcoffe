package br.com.goals.etrilhas.facade;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import br.com.goals.etrilhas.dao.BaseDao;
import br.com.goals.etrilhas.modelo.Foto;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.template.Template;

public class GaleriaFacade extends BaseFacade<Galeria>{
	private static Logger logger = Logger.getLogger(GaleriaFacade.class);
	private BaseDao<Galeria> galeriaDao = new BaseDao<Galeria>();
	private static GaleriaFacade instance = new GaleriaFacade();

	private GaleriaFacade(){
		
	}
	
	@Override
	public void atualizar(Galeria obj) throws FacadeException {
		//TODO salvar a descricao usando um template
		try{
			galeriaDao.atualizar(obj);
		}catch(Exception e){
			throw new FacadeException(e.getMessage());
		}
	}

	@Override
	public void criar(Galeria obj) throws FacadeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Galeria> listar() throws FacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Galeria selecionar(long id) throws FacadeException {
		try{
			Galeria galeria =galeriaDao.selecionar(id); 
			return galeria;
		}catch(Exception e){
			throw new FacadeException(e.getMessage());
		}
	}

	public static GaleriaFacade getInstance() {
		return instance ;
	}

	/**
	 * Cria o arquivo html da galeria
	 * @param gal galeria
	 * @param itemId id mo mapaItem
	 * @throws Exception
	 */
	public void criarHtml(Galeria gal,Long itemId) throws Exception {
		logger.debug("Criando galeria "  + itemId);
		Galeria galeria = selecionar(gal.getId());
		Foto primeiraFoto = galeria.getFotos().get(0);
		String links = "";
		int i=1;
		for(Foto foto:galeria.getFotos()){
			links+=" <a href=\"javascript:openFoto('"+foto.getUrlRelativaJpg()+"')\">"+i+"</a> ";
			i++;
		}
		Template template = new Template();
		template.setTemplateFile("galeria.html");
		template.setArea("links", links);
		template.setArea("foto", "<img src=\""+primeiraFoto.getUrlRelativaJpg()+"\" height=\"300\" />");
		template.setArea("descricao",primeiraFoto.getTxtDescricao());
		File f = new File(galeriaDao.getBasePath()+"Galeria-"+itemId+".html");
		FileUtils.writeStringToFile(f, template.toString(),"ISO-8859-1");
	}

}
