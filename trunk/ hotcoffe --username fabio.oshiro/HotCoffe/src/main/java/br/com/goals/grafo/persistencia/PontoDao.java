package br.com.goals.grafo.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	public Ponto acharPorNome(String nome){
		Connection con = null;
		Ponto res=null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("Select * From hot_ponto where nome=?");
			ps.setString(1, nome);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				return parseResultSet(rs);
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
	private Ponto parseResultSet(ResultSet rs) throws SQLException{
		Ponto ponto = new Ponto();
		ponto.setNome(rs.getString("nome"));
		ponto.setPontoId(rs.getLong("ponto_id"));
		ponto.setClasse(rs.getString("classe"));
		ponto.setDescricao(rs.getString("descricao"));
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
	/**
	 * Cria o ponto no banco de dados
	 * @param ponto a ser criado
	 * @return ponto com o id
	 */
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
			System.err.println(ponto);
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
		Ponto ponto = acharPorNome(nome);
		if (ponto==null){
			ponto = new Ponto();
			ponto.setNome(nome);
			criar(ponto);
		}
		return ponto;
	}
	public Ponto acharOuCriarPorNome(Ponto ponto) {
		Ponto pontoDB = acharPorNome(ponto.getNome());
		if (pontoDB==null){
			criar(ponto);
			return ponto;
		}else{
			return pontoDB;
		}
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
	public synchronized Ponto acharOuCriarPorDescricao(Ponto ponto) {
		Ponto pontoDB = acharPorDescricaoExata(ponto.getDescricao());
		if (pontoDB==null){
			criar(ponto);
			return ponto;
		}else{
			return pontoDB;
		}
	}
	/**
	 * Liga A para B se nao estiver ligado  
	 * @param importante conceito mais importante ou abstrato
	 * @param ponto conceito mais concreto ou menos importante
	 * @return Ligacao
	 */
	public synchronized Ligacao ligarSeDesligado(Ponto importante, Ponto ponto) {
		Connection con = null;
		try{
			Ligacao ligacao = new Ligacao();
			Date dataHora = new Date();
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("select * from hot_ligacao where ponto_id_a=? and ponto_id_b=?");
			ps.setObject(1, importante.getPontoId());
			ps.setObject(2, ponto.getPontoId());
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				ligacao.setLigacaoId(rs.getLong(1));
				ligacao.setPontoIdA(ponto.getPontoId());
				ligacao.setPontoIdB(importante.getPontoId());
				ligacao.setDataHora(dataHora);
				return ligacao;
			}else{
				return ligar(importante,ponto);
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
	public List<Ponto> getLigacaoA(Ponto ponto) {
		Connection con = null;
		ArrayList<Ponto> res=new ArrayList<Ponto>();
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("Select * From hot_ponto p inner join hot_ligacao l" +
					" on l.ponto_id_a=p.ponto_id where l.ponto_id_b=? order by p.ponto_id");
			ps.setObject(1, ponto.getPontoId());
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
	public List<Ponto> getLigacaoB(Ponto ponto) {
		Connection con = null;
		ArrayList<Ponto> res=new ArrayList<Ponto>();
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("Select * From hot_ponto p inner join hot_ligacao l" +
					" on l.ponto_id_b=p.ponto_id where l.ponto_id_a=? order by p.ponto_id");
			ps.setObject(1, ponto.getPontoId());
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
	public List<Ponto> acharPontoAComum(Ponto ponto1,Ponto ponto2){
		//TODO fazer funcionar
		ArrayList<Ponto> res=new ArrayList<Ponto>();
		
		Connection con = null;
		String sql = "select ponto_id_a from hot_ligacao l where l.ponto_id_b=?" 
				+ " and l.ponto_id_a in (select ponto_id_a from hot_ligacao where ponto_id_b=?)";
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(
					sql);
			ps.setObject(1, ponto1.getPontoId());
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
	private Comparator<Ponto> comparatorByIdAsc = new Comparator<Ponto>(){
		public int compare(Ponto o1, Ponto o2) {
			System.out.println("?");
			return o1.getPontoId().compareTo(o2.getPontoId());
		}
	};
	/**
	 * Encontra o ponto que representa este grupo
	 * @param listGrupo
	 * @return ponto que representa este grupo
	 */
	public List<Ponto> acharGrupo(List<Ponto> listGrupo) {
		ArrayList<Ponto> res=new ArrayList<Ponto>();
		
		//acha todos os pontos em comum
		List<Ponto> resA = acharPontosAComum(listGrupo);
		//verificar para cada res se é exatamente o listGrupo
		Collections.sort(listGrupo,comparatorByIdAsc);
		for(Ponto pA:resA){
			if(pA.getLigacaoB().size()==0){
				pA.setLigacaoB(this.getLigacaoB(pA));
			}
			//ver
			if(pA.getLigacaoB().size()!=listGrupo.size()){
				//já não é igual
				continue;
			}
			
			//comparar todos os pontos
			for(Ponto pB:pA.getLigacaoB()){
				for(Ponto pG:listGrupo){
					if(!pB.equals(pG)){
						continue;
					}
				}
			}
			
			res.add(pA);
		}
		return res;
	}
	private List<Ponto> retornarQuery(String sql) throws Exception{
		Connection con = null;
		ArrayList<Ponto> res=new ArrayList<Ponto>();
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				res.add(parseResultSet(rs));
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
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
	 * Acha o ponto A comum a todos os pontos
	 * @param listGrupo
	 * @return pontos A que tem como B todos da lista
	 */
	public List<Ponto> acharPontosAComum(List<Ponto> listGrupo) {
		/*
		 * select * from proc p 
		 * inner join lig l1 on l1.idA = p.id
		 * inner join lig l2 on l2.idA = p.id
		 * inner join lig l3 on l3.idA = p.id
		 * where l1.idB = :idB1
		 * and l2.idB = :idB2
		 * and l3.idB = :idB3
		 */
		String sql = "Select p.ponto_id,p.nome,p.descricao,p.classe,p.data_hora from hot_ponto p ";
		String where = " where 1=1 ";
		int t = listGrupo.size();
		for(int i=0;i<t;i++){
			sql+=" inner join hot_ligacao l"+i+" on l"+i+".ponto_id_a=p.ponto_id ";
			where +=" and l"+i+".ponto_id_b="+listGrupo.get(i).getPontoId();
		}
		List<Ponto> res1 = null;
		try{
			res1 = retornarQuery(sql+where);
			return res1;
		}catch(Exception e){
			//erro, fazer do modo antigo
			System.out.println("Erro ao executar query, fazendo do modo antigo.");
		}
		//resposta
		ArrayList<Ponto> res=new ArrayList<Ponto>();
		
		Ponto pontoMenosA = null;
		//carregar a ligacao A
		for(Ponto ponto:listGrupo){
			if(ponto.getLigacaoA().size()==0){
				ponto.setLigacaoA(this.getLigacaoA(ponto));
				if(ponto.getLigacaoA().size()==0){
					//lista vazia
					return res;
				}
			}
			if(pontoMenosA==null || pontoMenosA.getLigacaoA().size()<pontoMenosA.getLigacaoA().size()){
				pontoMenosA = ponto;
			}
		}
		
		if(pontoMenosA==null){
			return res;
		}
		for(Ponto pontoA:pontoMenosA.getLigacaoA()){
			boolean contidoEmTodos = true;
			for(Ponto ponto:listGrupo){
				if(!ponto.getLigacaoA().contains(pontoA)){
					contidoEmTodos = false;
					break;
				}
			}
			if(contidoEmTodos){
				res.add(pontoA);
			}
		}
		
		//comparar respostas
		if(res1!=null){
			Collections.sort(res1,comparatorByIdAsc);
			Collections.sort(res,comparatorByIdAsc);
			boolean igual=true;
			if(res1.size()==res.size()){
				int tot=res1.size();
				for(int i=0;i<tot;i++){
					Ponto ponto1=res1.get(i);
					Ponto ponto = res.get(i);
					if(!ponto1.equals(ponto)){
						System.out.println(ponto1.getPontoId() + " é diferente de " + ponto.getPontoId());
						igual=false;
					}
				}
			}else{
				System.out.println("res 1 é diferente de res " + res.size() + " " + res1.size());
				igual = false;
			}
			if(igual){
				System.out.println("res 1 é igual a res");
			}
		}
		return res;
	}
	public Ponto criarGrupo(Ponto pontoA, ArrayList<Ponto> listGrupo) {
		//Persiste o ponto caso nao esteja
		if(pontoA.getPontoId()==null || pontoA.getPontoId().equals(0l)){
			pontoA = criar(pontoA);
		}
		for(Ponto pontoB:listGrupo){
			ligar(pontoA, pontoB);
		}
		return pontoA;
	}
}
