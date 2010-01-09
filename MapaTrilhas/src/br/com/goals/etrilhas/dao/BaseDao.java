package br.com.goals.etrilhas.dao;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import br.com.goals.etrilhas.modelo.Base;
import br.com.goals.jpa4google.EMF;
import br.com.goals.jpa4google.Jpa4GoogleEntityManager;
import br.com.goals.jpa4google.PMF;

import com.google.appengine.api.datastore.Text;

public class BaseDao<T extends Base> extends Jpa4GoogleEntityManager {
	private static String basePath = null;
	private static Logger logger = Logger.getLogger(BaseDao.class);
	private static Long lastIdGenerated = null; 
	private static File lastIds;
	protected static boolean google = true;
	private static String CHARSET_DEC = "iso-8859-1";
	public BaseDao() {
		
	}
	
	public static long getNextId() throws Exception{
		if(google){
			return getNextIdGoogle();
		}else{
			return getNextIdArquivo();
		}
	}
	private static long getNextIdArquivo(){
		if(lastIdGenerated==null){
			lastIdGenerated = 3L;
			try {
				lastIds = new File(new BaseDao().getBasePath()+"lastIds.txt");
				if(!lastIds.exists()){
					FileUtils.writeStringToFile(lastIds, lastIdGenerated.toString(),"UTF-8");
				}else{
					String lastId;
					lastId = FileUtils.readFileToString(lastIds,"UTF-8");
					lastIdGenerated = Long.parseLong(lastId); 
				}
			} catch (IOException e) {
				logger.error("Erro ",e);
			}
		}else{
			try{
				FileUtils.writeStringToFile(lastIds, lastIdGenerated.toString(),"UTF-8");
			}catch(Exception e){
				logger.error("Erro " , e);
			}
		}
		//entao eh not null, e por isso incrementamos
		lastIdGenerated++;
		return lastIdGenerated;	
	}
	private static long getNextIdGoogle() throws Exception{
		
		PersistenceManager pm = PMF.getPersistenceManager();
		try{
			Long id = 1L;
			JdoLastId jdoLastId = null;
			if(lastIdGenerated==null){
				logger.info("Iniciando lastId...");
				try{
					jdoLastId = pm.getObjectById(JdoLastId.class,id);
					lastIdGenerated = jdoLastId.getLastId();
					logger.info("recuperado o lastIdGenerated = " + lastIdGenerated + " do JDO");
				}catch(Exception e){
					logger.warn("Last Id not found...",e);
				}
				if(jdoLastId==null){
					jdoLastId = new JdoLastId();
					jdoLastId.setId(id);
					lastIdGenerated = 3L;
					jdoLastId.setLastId(lastIdGenerated);
					logger.info("criando last id " + lastIdGenerated);
					pm.makePersistent(jdoLastId);
				}
			}else{
				//atualizar
				try{
					jdoLastId = pm.getObjectById(JdoLastId.class,id);
					jdoLastId.setLastId(lastIdGenerated);
					pm.flush();
				}catch(Exception e){
					logger.error("Erro ao atualizar o lastId",e);
					throw e;
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			pm.close();
		}
		//entao eh not null, e por isso incrementamos
		lastIdGenerated++;
		return lastIdGenerated;	
	}
	/**
	 * 
	 * @return a pasta data
	 */
	public String getBasePath() {
		if(basePath==null){
			String path = this.getClass().getClassLoader().getResource("Configuracao.properties").getPath();
			File file = new File(path);
			File data = new File(file.getParentFile().getParentFile().getParentFile(),"data");
			if(!google){
				if(!data.exists()){
					data.mkdirs();
				}
			}
			basePath = data.getAbsolutePath();
			if(!basePath.endsWith(File.separator)){
				basePath+=File.separatorChar;
			}
		}
		return basePath;
	}
	
	public static void setBasePath(String basePath){
		logger.info("BaseDao.setBasePath('" +basePath +"');");
		File verificar = new File(basePath);
		if(!verificar.exists()){
			logger.info("Criando o diretorio " + basePath + "...");
			verificar.mkdirs();
		}
		BaseDao.basePath = basePath;
	}
	
	public T selecionar(long id) throws Exception{
		if(google){
			T t = selecionarGoogle(id);
			if(t==null){
				throw new Exception("Not found id " + id);
			}
			return t;
		}else{
			return selecionarArquivo(id);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected T selecionarGoogle(PersistenceManager pm,Long id) throws Exception{
		logger.info("selecionarGoogle() Carregando id " + id);
		JdoXml jdoXml = pm.getObjectById(JdoXml.class, id);
		if(jdoXml!=null){
			if(jdoXml.getTxtXml()==null){
				throw new NullPointerException("jdoXml.getTxtXml()==null");
			}
			String value = jdoXml.getTxtXml().getValue();
			//logger.warn("DEC XML:\n" + value);
			InputStream is = new ByteArrayInputStream(value.getBytes());
			XMLDecoder xdec = new XMLDecoder(is);
			T t = (T)xdec.readObject();
			urlEncodeAllObjectString(t, null,false);
			
			if(t==null){
				throw new NullPointerException("Nao foi possivel transformar o xml em objeto.");
			}
			return t;
		}else{
			logger.info(id + " = Null");
			return null;
		}
	}
	
	protected T selecionarGoogle(Long id){
		PersistenceManager pm = PMF.getPersistenceManager();
		try{
			return selecionarGoogle(pm,id);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private T selecionarArquivo(long id) throws Exception{
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
		if(google){
			logger.debug("criando " + obj.getClass() + " id "+ obj.getId());
			if(obj.getId()==null){
				//nao tem id, gerar um
				obj.setId(getNextId());
			}
			gravarGoogle(obj,obj.getId());
		}else{
			//Cria o ID
			obj.setId(getNextId());
			String path = getBasePath()+obj.getClass().getSimpleName()+ "-" + obj.getId() + ".xml";
			logger.debug("criando " + path);
			gravar(obj, path);
		}
	}
	
	protected synchronized void gravarGoogle(T obj, Long id) throws Exception{
		
		//verificar a versao do objeto
		//T objPersistido = selecionarGoogle(id);
		//if(objPersistido.getVersao()!=null && !objPersistido.getVersao().equals(obj.getVersao())){
			//Exception e = new Exception("Alguém salvou uma versão mais recente e a sua está desatualizada.");
			//logger.error("Erro de sincronismo. " + objPersistido.getVersao() + "!=" + obj.getVersao(),e);
		//}
		//logger.debug(objPersistido.getVersao() + "==" + obj.getVersao());
		obj.setVersao(System.currentTimeMillis());
		urlEncodeAllObjectString(obj, null,true);
		try{
			// Create output stream.
			OutputStream outputStream = new ByteArrayOutputStream();	
			// Create XML encoder.
			XMLEncoder xenc = new XMLEncoder(outputStream);
			// Write object.		
			xenc.writeObject(obj);
			xenc.close();
			
			JdoXml jdoXml = new JdoXml();
			if(id!=null){
				jdoXml.setId(id);
			}
			
			jdoXml.setTxtXml(new Text(outputStream.toString()));
			
			PersistenceManager pm = PMF.getPersistenceManager();
			if(id!=null){
				pm.makePersistent(jdoXml);
			}else{
				pm.makePersistent(jdoXml);
			}
			pm.close();
			
			logger.debug("gravarGoogle(Classe = " + obj.getClass()+",id = "+id+")");
			//logger.debug("INI XML:\n" + outputStream.toString());
			logger.debug("jdoXml.getId() = " + jdoXml.getId());
			//testar
			pm = PMF.getPersistenceManager();
			jdoXml = pm.getObjectById(JdoXml.class,jdoXml.getId());
			if(jdoXml!=null){
				if(jdoXml.getTxtXml()==null){
					throw new NullPointerException("jdoXml.getTxtXml()==null");
				}
			}
			pm.close();
		}finally{
			urlEncodeAllObjectString(obj, null,false);
		}
	}
	
	/**
	 * Somente um grava...
	 * @param obj
	 * @param path
	 * @throws Exception
	 */
	protected synchronized void gravar(T obj, String path)throws Exception{
		/*
		// Create output stream.
		OutputStream outputStream = new FileOutputStream(path);
		// Create XML encoder.
		XMLEncoder xenc = new XMLEncoder(outputStream);
		// Write object.
		xenc.writeObject(obj);
		xenc.close();
		*/
	}

	public void atualizar(T obj) throws Exception{
		//verificar a versao
		
		String path = getBasePath()+obj.getClass().getSimpleName()+ "-" + obj.getId() + ".xml";
		logger.debug("atualizando... " + path);
		
		if(google){
			gravarGoogle(obj,obj.getId());
		}else{
			gravar(obj, path);
		}
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
