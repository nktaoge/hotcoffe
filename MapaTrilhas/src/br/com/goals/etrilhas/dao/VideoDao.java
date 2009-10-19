package br.com.goals.etrilhas.dao;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.io.FileUtils;

import com.google.appengine.api.datastore.Text;

import br.com.goals.etrilhas.modelo.Video;
import br.com.goals.jpa4google.PMF;
import br.com.goals.template.Template;

public class VideoDao extends BaseDao<Video> {
	@Override
	public void criar(Video obj) throws Exception {
		Template template = new Template();
		template.setTemplateFile("video.html");
		template.setArea("video", obj.getTxtCodYoutube());
		template.setArea("descricao",obj.getHtmlDescricao());
		if(google){
			String fName = "Video-"+obj.getId()+".html";
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
			File f = new File(getBasePath()+"Video-"+obj.getId()+".html");
			FileUtils.writeStringToFile(f, template.toString(),"ISO-8859-1");
		}
	}
}
