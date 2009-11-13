package br.com.goals.etrilhas.dao;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.appengine.api.datastore.Text;

import br.com.goals.etrilhas.modelo.Foto;
import br.com.goals.etrilhas.modelo.Galeria;
import br.com.goals.jpa4google.PMF;
import br.com.goals.cafeina.view.tmp.Template;

public class GaleriaDao extends BaseDao<Galeria>{
	private static Logger logger = Logger.getLogger(GaleriaDao.class);
	@Override
	public void atualizar(Galeria galeria) throws Exception {
		try{
			for(Foto foto:galeria.getFotos()){
				if(foto.getId()==null){
					foto.setId(getNextId());
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		super.atualizar(galeria);
	}
	/**
	 * Cria o arquivo html da galeria
	 * @param gal galeria
	 * @param itemId id mo mapaItem
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void criarHtml(Galeria galeria,Long itemId) throws Exception {
		logger.debug("Criando galeria "  + itemId);
		if(galeria.getFotos()==null) return;
		if(galeria.getFotos().size()==0) return;
		
		Foto primeiraFoto = galeria.getFotos().get(0);
		String links = "<a href=\"javascript:prevFoto()\" id=\"fotoAnterior\">&lt;&lt;</a> ";
		String scriptArr = "<script>var arrFotosI=0; var arrFotos = new Array(";
		for(int i=0;i<galeria.getFotos().size();i++){
			Foto foto = galeria.getFotos().get(i);
			scriptArr+="\"" + foto.getUrlRelativaJpg() + "\",";
		}
		scriptArr =scriptArr.substring(0,scriptArr.length()-1); 
		scriptArr+=");</script>";
		for(int i=0;i<galeria.getFotos().size();i++){
			links+=" <a href=\"javascript:openFoto("+i+")\" id=\"pic"+i+"\">"+(i+1)+"</a> ";
		}
		links+="<a href=\"javascript:nextFoto()\" id=\"fotoPosterior\">&gt;&gt;</a>";
		Template template = new Template();
		template.setTemplateFile("galeria.html");
		template.setArea("links", scriptArr + links);
		template.setArea("foto", "<img src=\""+primeiraFoto.getUrlRelativaJpg()+"\" height=\"277\" />");
		template.setArea("descricao",primeiraFoto.getTxtDescricao());

		if(google){
			String fName = "Galeria-"+itemId+".html";
			PersistenceManager pm = PMF.getPersistenceManager();
			Query q = pm.newQuery("SELECT FROM " + JdoHtml.class.getName() + " WHERE fileName == fname PARAMETERS String fname");
			List results = (List)q.execute(fName);
			for(Object o:results){
				pm.deletePersistent(o);
				System.out.println("Deleting old " + fName);
			}
			JdoHtml jdoHtml = new JdoHtml();
			jdoHtml.setFileName(fName);
			jdoHtml.setTxtHtml(new Text(URLEncoder.encode(template.toString(),"utf-8")));
			try{
				pm.makePersistent(jdoHtml);
			}finally{
				pm.close();
			}
		}else{
			File f = new File(getBasePath()+"Galeria-"+itemId+".html");
			FileUtils.writeStringToFile(f, template.toString(),"ISO-8859-1");
		}
	}
}
