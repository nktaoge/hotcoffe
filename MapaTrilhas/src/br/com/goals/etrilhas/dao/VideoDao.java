package br.com.goals.etrilhas.dao;

import java.io.File;

import org.apache.commons.io.FileUtils;

import br.com.goals.etrilhas.modelo.Video;

public class VideoDao extends BaseDao<Video> {
	@Override
	public void criar(Video obj) throws Exception {
		File f = new File(getBasePath()+"Video-"+obj.getId()+".html");
		FileUtils.writeStringToFile(f, obj.getTxtCodYoutube());
	}
}
