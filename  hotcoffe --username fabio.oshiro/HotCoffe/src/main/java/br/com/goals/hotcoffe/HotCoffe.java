package br.com.goals.hotcoffe;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.ResultSetMetaData;
import java.util.HashMap;

public class HotCoffe{
	ModelDao modelDao;
	Config config;
	HashMap<String,Model> mapModel;
	public HotCoffe(Config config) {
		this.config = config;
		modelDao = new ModelDao(config);
		makeAllModel();
	}

	/**
	 * Faz o relacionamento de classes do modelo que esta no banco
	 */
	private void makeAllModel() {
		Connection con = null;
		try {
			mapModel = new HashMap<String,Model>();
			// Listar todas as tabelas do banco
			con = modelDao.getConnection();
			DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet rsTables = databaseMetaData.getTables(null, null, "%", null);
			while (rsTables.next()) {
				String tableName = rsTables.getString(3);
				System.out.print("\n Tabela = ");
				System.out.println(tableName);
				mapModel.put(tableName, makeNullModel(tableName));
			}
			modelDao.setMapModel(mapModel);
			/*
			 * relacionar as tabelas, segundo uma convenção idmodelo = campo
			 * auto increment, onde modelo é o nome do modelo
			 */
			// fieldConfig.isAutoIncrement = rsmd.isAutoIncrement(i);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				con.close();
			}catch(Exception e){
				
			}
		}
	}
	private Model makeNullModel(String name) {
		Model model = new Model(name);
		Connection con = modelDao.getConnection();
		try{
			PreparedStatement st = con.prepareStatement("select * from "+name+" where 1=0");
			ResultSet rs = st.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int tot = rsmd.getColumnCount();
			
			for (int i = 1; i <= tot; i++) {
				model.createAttribute(rsmd.getColumnName(i), rsmd.getColumnClassName(i),rsmd.getColumnDisplaySize(i),rsmd.isNullable(i) == ResultSetMetaData.columnNullable,rsmd.isAutoIncrement(i));
			}
			rs.close();
			st.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(Exception e){
				
			}
		}
		return model;
	}
	public ModelDao getModelDao() {
		return modelDao;
	}
	public Model getNewModel(String name){
		try{
			return (Model) mapModel.get(name).clone();
		}catch(Exception e){
			
		}
		return null;
	}
}
