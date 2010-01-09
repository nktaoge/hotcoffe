package br.com.goals.etrilhas.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.goals.cafeina.view.tmp.AreaNaoEncontradaException;
import br.com.goals.cafeina.view.tmp.RequestUtil;
import br.com.goals.cafeina.view.tmp.Template;
import br.com.goals.etrilhas.dao.JdoFile;
import br.com.goals.etrilhas.dao.MapaItemDao;
import br.com.goals.etrilhas.facade.GaleriaFacade;
import br.com.goals.etrilhas.facade.MapaFacade;
import br.com.goals.etrilhas.facade.MapaItemFacade;
import br.com.goals.etrilhas.modelo.Camada;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.etrilhas.modelo.Mapa;
import br.com.goals.etrilhas.modelo.MapaItem;
import br.com.goals.jpa4google.PMF;

/**
 * Servlet implementation class MapaItemDefinirServlet
 */
public class MapaItemDefinir extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(MapaItemDefinir.class);
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapaItemDefinir() {
        super();
    }

    protected void carregarTemplate(Template template,MapaItem mapaItem,Mapa mapa) throws Exception{
    	template.setInput("nome",mapaItem.getNome());
    	template.setInput("x",mapaItem.getX());
    	template.setInput("y",mapaItem.getY());
    	
    	//File dirIcones = new File(template.getTemplatePath().getParent(),"media/icones");
		//template.setSelect("icone",mapaItem.getIcone(),dirIcones.list(fileNameFilter));
    	
    	template.setSelect("icone",mapaItem.getIcone(),listarImagensIcone());
    	
		template.setSelect("tipo", mapaItem.getTipo());
		if(mapaItem.getTipo()!=null && mapaItem.getTipo().equals("Galeria")){
			//se for galeria colocar antes o formulario
			Galeria galeria = GaleriaFacade.getInstance().selecionar(mapaItem.getGaleriaId());
			template.setForm("campos do tipo",galeria);
		}else{
			template.setForm("campos do tipo",mapaItem.getValor());
		}
		if(mapaItem.getCamada()==null){
			//selecionar a primeira
			if(mapa.getCamadas().size()>0){
				Camada selecionada = mapa.getCamadas().get(0);
				template.setRadio("camadas",mapa.getCamadas(),selecionada.getId().toString());
			}else{
				template.setRadio("camadas",mapa.getCamadas(),null);
			}
		}else{
			template.setRadio("camadas",mapa.getCamadas(),mapaItem.getCamada().getId().toString());
		}
		String linkEdicao = null;
		if(mapaItem.getTipo()!=null && mapaItem.getTipo().equals("Galeria")
			&& mapaItem.getGaleriaId()!=null){
			linkEdicao = "GaleriaServlet?galeriaId=" + mapaItem.getGaleriaId();
		}
		template.setLink("linkEdicao", linkEdicao);
    }
	private String[] listarImagensIcone() {
		PersistenceManager pm = PMF.getPersistenceManager();
		Query q = pm.newQuery("SELECT FROM " + JdoFile.class.getName() + " where fileName.startsWith(\"media/\")");
		List<JdoFile> files = (List<JdoFile>)q.execute();
		String retorno[] = new String[files.size()];
		int i=0;
		for(JdoFile f:files){
			retorno[i++]=f.getFileName();
		}
		return retorno;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long id = null;
		Template template = null;
		try{
			id = Long.parseLong(request.getParameter("id"));
			super.setCurrentMapaItemId(request, id);
		}catch(Exception e){
			e.printStackTrace();
		}
		template = getTemplate(request);
		/*
		 * Verificar se possui um tipo
		 */
		try{
			Mapa mapa = getMapa(request);
			MapaItemDao mapaItemDao = new MapaItemDao();
			MapaItem mapaItem = mapaItemDao.selecionar(mapa, id);
			template.set("id",id);
			carregarTemplate(template, mapaItem, mapa);
			template.setMensagem("");
		}catch(Exception e){
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
	}

	private static long callNumber = 0;
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = null;
		Mapa mapa = null;
		MapaItem mapaItem = null;
		logger.debug("MapaItemDefinir.doPost() callNumber="+ (callNumber++));
		try{
			String tipo = request.getParameter("tipo");
			Long id=null;
			try{
				id = Long.parseLong(request.getParameter("id"));
			}catch(NumberFormatException e){
				//ok
				logger.info("Criando mapaItem...");
			}
			mapa = getMapa(request);
			MapaItemDao mapaItemDao = new MapaItemDao();
			if(id==null){
				mapaItem = new MapaItem();
			}else{
				mapaItem = mapaItemDao.selecionar(mapa, id);
			}
			mapaItem.setNome(request.getParameter("nome"));
			mapaItem.setIcone(request.getParameter("icone"));
			try{
				mapaItem.setX(Double.parseDouble(request.getParameter("x")));
			}catch(Exception e){
				throw new Exception("Nao foi possivel atribuir a latitude.");
			}
			try{
				mapaItem.setY(Double.parseDouble(request.getParameter("y")));
			}catch(Exception e){
				throw new Exception("Nao foi possivel atribuir a longitude.");
			}
			//Criar um item do tipo
			Object obj = null;
			if(!tipo.equals(mapaItem.getTipo())){
				//O tipo era diferente do antigo
				obj = Class.forName("br.com.goals.etrilhas.modelo."+tipo).newInstance();
				mapaItem.setTipo(tipo);
				logger.debug("mapaItem.getTipo() = " + mapaItem.getTipo());
				if(tipo.equals("Galeria")){
					Galeria galeria;
					galeria = new Galeria();
					galeria.setTxtDescricao(request.getParameter("Galeria.TxtDescricao"));
					GaleriaFacade.getInstance().criar(galeria);
					mapaItem.setGaleriaId(galeria.getId());
					logger.debug("galeria.getId()="+galeria.getId());
				}else{
					mapaItem.setValor(obj);
				}
			
			}else{
				//O tipo permanesce o mesmo
				if(tipo.equals("Galeria")){
					Galeria galeria;
					if(request.getParameter("Galeria.id")==null
							|| request.getParameter("Galeria.id").equals("")){
						galeria = new Galeria();
						galeria.setTxtDescricao(request.getParameter("Galeria.TxtDescricao"));
						//TODO implementar o criar galeria
						GaleriaFacade.getInstance().criar(galeria);
						mapaItem.setGaleriaId(galeria.getId());
						logger.debug("galeria.getId()="+galeria.getId());
					}else{
						//TODO atualizar a galeria TESTAR
						Long galeriaId = Long.parseLong(request.getParameter("Galeria.id"));
						galeria = GaleriaFacade.getInstance().selecionar(galeriaId);
						galeria.setTxtDescricao(request.getParameter("Galeria.TxtDescricao"));
						GaleriaFacade.getInstance().atualizar(galeria);
						mapaItem.setGaleriaId(galeriaId);
					}
				}else{
					RequestUtil.request(request, mapaItem.getValor());
				}
			}
			
			//Verificar se colocou em outra camada
			Long camadaId = Long.parseLong(request.getParameter("Camada.id"));
			if(mapaItem.getCamada()==null){
				//o item nao tem camada
				Camada camada = camadaFacade.selecionar(mapa,camadaId);
				mapaItem.setCamada(camada);
				if(camada.getItems()==null){
					camada.setItems(new ArrayList<MapaItem>());
				}
				//camada.getItems().add(mapaItem);
				if(id==null){
					//o item nao esta salvo
					logger.debug("mapaItemFacade.criar()");
					mapaItemFacade.criar(mapaItem,mapa);
					id = mapaItem.getId();
				}
			}else if(!camadaId.equals(mapaItem.getCamada().getId())){
				//trocar de camada
				Camada camada = camadaFacade.selecionar(mapa,camadaId);
				//retira da camada antiga
				mapaItem.getCamada().getItems().remove(mapaItem);
				mapaItem.setCamada(camada);
				if(camada.getItems()==null){
					camada.setItems(new ArrayList<MapaItem>());
				}
				camada.getItems().add(mapaItem);
			}
			MapaFacade.getInstance().atualizar(mapa);
			MapaItemFacade.getInstance().criarHtml(mapaItem);
			//Montar resposta para o usuario
			template = getTemplate(request);
			template.set("id",id);
			carregarTemplate(template, mapaItem, mapa);
			template.setMensagem("Alterado com sucesso");
		}catch(Exception e){
			if(template==null){
				template = getTemplate(request);
			}
			try{
				carregarTemplate(template, mapaItem, mapa);
			}catch(Exception e2){
				e2.printStackTrace();
			}
			template.setMensagem("Erro: " + e.getMessage());
			e.printStackTrace();
		}
		response.getWriter().write(template.toString());
	}
}
