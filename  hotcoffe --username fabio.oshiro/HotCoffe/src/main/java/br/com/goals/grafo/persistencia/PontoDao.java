package br.com.goals.grafo.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.goals.grafo.modelo.Ligacao;
import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.hotcoffe.Config;

public class PontoDao {
	private Config config;
	private static PontoDao instance=new PontoDao();
	private PontoDao(){
		config = new Config(){
			public String getDbDriver() {
				return  "com.mysql.jdbc.Driver";
			}
			public String getDbPassword() {
				return "admin1234";
			}

			public String getDbUrl() {
				return "jdbc:mysql://localhost:3309/hotcoffe";
			}

			public String getDbUser() {
				return "root";
			}
			
		};
	}
	/**
	 * Algum connection pool
	 * 
	 * @return
	 */
	private Connection getConnection() {
		try {
			Class.forName(config.getDbDriver()).newInstance();
			Connection con = DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
			return con;
		} catch (Exception e) {
			System.out.println(e.getClass() + " " + e.getMessage());
		}
		return null;
	}
	public List<Ponto> acharPorNome(String nome){
		Connection con = null;
		ArrayList<Ponto> res=null;
		try{
			res = new ArrayList<Ponto>();
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("Select * From hot_ponto where nome=?");
			ps.setString(1, nome);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				res.add(parseResultSet(rs));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(con!=null){
					con.close();
				}
			}catch(Exception e){
				
			}
		}
		if(res==null || res.size()==0){
			Ponto ponto = new Ponto();
			ponto.setNome(nome);
			res = new ArrayList<Ponto>();
			res.add(ponto);
		}
		return res;
	}
	private Ponto parseResultSet(ResultSet rs) throws SQLException{
		Ponto ponto = new Ponto();
		ponto.setNome(rs.getString("nome"));
		ponto.setPontoId(rs.getLong("ponto_id"));
		ponto.setClasse(rs.getString("classe"));
		return ponto;
	}
	public static PontoDao getInstance() {
		return instance;
	}
	/**
	 * 
	 * @param classe
	 * @return pontos que tenham a classe x
	 */
	public List<Ponto> acharClasse(String classe) {
		Connection con = null;
		ArrayList<Ponto> res=null;
		try{
			res = new ArrayList<Ponto>();
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("Select * From hot_ponto where classe=?");
			ps.setString(1, classe);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				res.add(parseResultSet(rs));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(con!=null){
					con.close();
				}
			}catch(Exception e){
				
			}
		}
		return res;
	}
	/**
	 * 
	 * @param descricao nome do ponto
	 * @return Ponto 1 ponto
	 */
	private Ponto acharPorDescricaoExata(String descricao){
		Connection con = null;
		Ponto res=null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("Select * From hot_ponto where descricao=?");
			ps.setString(1, descricao);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				res = parseResultSet(rs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(con!=null){
					con.close();
				}
			}catch(Exception e){
				
			}
		}
		return res;
	}
	public Ponto criar(Ponto ponto){
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("insert into hot_ponto (nome,data_hora,classe,descricao) values (?,?,?,?)");
			ps.setString(1, ponto.getNome());
			ps.setObject(2, ponto.getDataHora());
			ps.setString(3, ponto.getClasse());
			ps.setString(4, ponto.getDescricao());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
				ponto.setPontoId(rs.getLong(1));
			}
			return ponto;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(con!=null){
					con.close();
				}
			}catch(Exception e){
				
			}
		}
		return null;
	}
	public synchronized Ponto acharOuCriarPorNome(String nome) {
		Ponto ponto = acharPorDescricaoExata(nome);
		if (ponto==null){
			ponto = new Ponto();
			ponto.setNome(nome);
			criar(ponto);
		}
		return ponto;
	}
	public synchronized Ponto acharOuCriarPorDescricao(String descricao) {
		Ponto ponto = acharPorDescricaoExata(descricao);
		if (ponto==null){
			ponto = new Ponto(descricao);
			criar(ponto);
		}
		return ponto;
	}
	public Ligacao ligar(Ponto importante,Ponto ponto) {
		Connection con = null;
		try{
			Ligacao ligacao = new Ligacao();
			Date dataHora = new Date();
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("insert into hot_ligacao (ponto_id_a,ponto_id_b,data_hora) values (?,?,?)");
			ps.setObject(1, importante.getPontoId());
			ps.setObject(2, ponto.getPontoId());
			ps.setObject(3, dataHora);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
				ligacao.setLigacaoId(rs.getLong(1));
				ligacao.setPontoIdA(ponto.getPontoId());
				ligacao.setPontoIdB(importante.getPontoId());
				ligacao.setDataHora(dataHora);
				return ligacao;
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(con!=null){
					con.close();
				}
			}catch(Exception e){
				
			}
		}
		return null;
	}
}
