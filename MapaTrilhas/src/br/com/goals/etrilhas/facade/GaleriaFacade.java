package br.com.goals.etrilhas.facade;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.goals.etrilhas.dao.GaleriaDao;
import br.com.goals.etrilhas.modelo.Foto;
import br.com.goals.etrilhas.modelo.Galeria;

public class GaleriaFacade extends BaseFacade<Galeria>{
	private static Logger logger = Logger.getLogger(GaleriaFacade.class);
	private GaleriaDao galeriaDao = new GaleriaDao();
	private static GaleriaFacade instance;

	private GaleriaFacade(){
		
	}
	
	@Override
	public void atualizar(Galeria galeria) throws FacadeException {
		//TODO salvar a descricao usando um template
		try{
			galeriaDao.atualizar(galeria);
		}catch(Exception e){
			throw new FacadeException(e.getMessage());
		}
	}

	@Override
	public void criar(Galeria obj) throws FacadeException {
		try{
			galeriaDao.criar(obj);
		}catch(Exception e){
			throw new FacadeException(e.getMessage());
		}
	}

	@Override
	public List<Galeria> listar() throws FacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Galeria selecionar(long id) throws FacadeException {
		try{
			Galeria galeria = galeriaDao.selecionar(id); 
			return galeria;
		}catch(Exception e){
			throw new FacadeException(e.getMessage());
		}
	}

	public static GaleriaFacade getInstance() {
		if(instance==null){
			instance = new GaleriaFacade();
		}
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
		galeriaDao.criarHtml(galeria,itemId);
	}

	public void apagarFoto(Galeria galeria, String fotoId) throws Exception {
		if(fotoId!=null){
			Long id = Long.parseLong(fotoId);
			for(Foto foto:galeria.getFotos()){
				if(foto.getId().equals(id)){
					galeria.getFotos().remove(foto);
					break;
				}
			}
			galeriaDao.atualizar(galeria);
		}
	}

}
