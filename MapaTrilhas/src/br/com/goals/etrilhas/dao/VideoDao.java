package br.com.goals.etrilhas.dao;

import java.io.File;

import org.apache.commons.io.FileUtils;

import br.com.goals.etrilhas.modelo.Video;
import br.com.goals.template.Template;

public class VideoDao extends BaseDao<Video> {
	@Override
	public void criar(Video obj) throws Exception {
		Template template = new Template();
		template.setTemplateFile("video.html");
		template.setArea("video", obj.getTxtCodYoutube());
		template.setArea("descricao",obj.getTxtDescricao());
		File f = new File(getBasePath()+"Video-"+obj.getId()+".html");
		FileUtils.writeStringToFile(f, template.toString(),"ISO-8859-1");
	}
}
