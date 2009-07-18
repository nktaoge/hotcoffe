package br.com.goals.etrilhas.dao;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.goals.etrilhas.modelo.Base;

public class BaseDao<T extends Base> {
	private static String basePath = null;
	private static Logger logger = Logger.getLogger(BaseDao.class);
	 
	/**
	 * 
	 * @return a pasta data
	 */
	public String getBasePath() {
		if(basePath==null){
			String path = this.getClass().getClassLoader().getResource("Configuracao.properties").getPath();
			File file = new File(path);
			File data = new File(file.getParentFile().getParentFile().getParentFile(),"data");
			if(!data.exists()){
				data.mkdirs();
			}
			basePath = data.getAbsolutePath();
			if(!basePath.endsWith(File.separator)){
				basePath+=File.separatorChar;
			}
		}
		return basePath;
	}
	@SuppressWarnings("unchecked")
	public T selecionar(long id) throws Exception{
		File dir = new File(getBasePath());
		File arqs[] = dir.listFiles();
		for (int i = 0; i < arqs.length; i++) {
			if(arqs[i].getName().endsWith(id+".xml")){
				FileInputStream fis = new FileInputStream(arqs[i]);
				XMLDecoder xdec = new XMLDecoder(fis);
				try{
					return (T)xdec.readObject();
				}catch(Exception e){}
			}
		}
		throw new Exception("Id " + id + " not found!");
	}
	public void criar(T obj) throws Exception {
		//Cria o ID
		obj.setId(new Date().getTime());
		String path = getBasePath()+obj.getClass().getSimpleName()+ "-" + obj.getId() + ".xml";
		System.out.println(path);
		gravar(obj, path);
	}
	/**
	 * Somente um grava...
	 * @param obj
	 * @param path
	 * @throws Exception
	 */
	protected synchronized void gravar(T obj, String path)throws Exception{
		// Create output stream.
		FileOutputStream fos = new FileOutputStream(path);
		// Create XML encoder.
		XMLEncoder xenc = new XMLEncoder(fos);
		// Write object.
		xenc.writeObject(obj);
		xenc.close();
	}
	public void atualizar(T obj) throws Exception{
		String path = getBasePath()+obj.getClass().getSimpleName()+ "-" + obj.getId() + ".xml";
		//logger.warn("atualizado " + path, new Exception());
		logger.debug("atualizado " + path);		
		gravar(obj, path);	
	}
	@SuppressWarnings("unchecked")
	public List<T> listar() throws Exception{
		List<T> lista = new ArrayList<T>();
		File dir = new File(getBasePath());
		File arqs[] = dir.listFiles();
		for (int i = 0; i < arqs.length; i++) {
			FileInputStream fis = new FileInputStream(arqs[i]);
			XMLDecoder xdec = new XMLDecoder(fis);
			try{
				T t= (T)xdec.readObject();
				lista.add(t);
			}catch(Exception e){}
		}
		return lista;
	}
}
