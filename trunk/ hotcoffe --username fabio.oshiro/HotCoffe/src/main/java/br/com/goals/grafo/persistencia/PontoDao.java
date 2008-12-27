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
	/**
	 * Comparador para ordenar por id em ordem asc
	 */
	private Comparator<Ponto> comparatorByIdAsc = new Comparator<Ponto>(){
		public int compare(Ponto o1, Ponto o2) {
			return o1.getPontoId().compareTo(o2.getPontoId());
		}
	};
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
	 * Single
	 * @return instancia
	 */
	public static PontoDao getInstance() {
		return instance;
	}
	private Ponto parseResultSet(ResultSet rs) throws SQLException{
		Ponto ponto = new Ponto();
		ponto.setNome(rs.getString("nome"));
		ponto.setPontoId(rs.getLong("ponto_id"));
		ponto.setClasse(rs.getString("classe"));
		ponto.setDescricao(rs.getString("descricao"));
		return ponto;
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
	/**
	 * 
	 * @param nome
	 * @return null caso nao encontre
	 */
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
	
	
	/**
	 * 
	 * @param classe nome da classe
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
	/**
	 * 
	 * @param nome do ponto
	 * @return ponto criado ou encontrado
	 */
	public synchronized Ponto acharOuCriarPorNome(String nome) {
		Ponto ponto = acharPorNome(nome);
		if (ponto==null){
			ponto = new Ponto();
			ponto.setNome(nome);
			criar(ponto);
		}
		return ponto;
	}
	/**
	 * 
	 * @param ponto
	 * @return ponto criado ou encontrado
	 */
	public Ponto acharOuCriarPorNome(Ponto ponto) {
		Ponto pontoDB = acharPorNome(ponto.getNome());
		if (pontoDB==null){
			criar(ponto);
			return ponto;
		}else{
			return pontoDB;
		}
	}
	/**
	 * 
	 * @param descricao
	 * @return ponto criado ou encontrado
	 */
	public synchronized Ponto acharOuCriarPorDescricao(String descricao) {
		Ponto ponto = acharPorDescricaoExata(descricao);
		if (ponto==null){
			ponto = new Ponto(descricao);
			criar(ponto);
		}
		return ponto;
	}
	/**
	 * 
	 * @param importante ponto A
	 * @param ponto ponto B
	 * @param pontoTipo tipo da ligacao
	 * @return null ou a ligacao
	 */
	public Ligacao ligar(Ponto importante,Ponto ponto,Ponto pontoTipo) {
		Connection con = null;
		try{
			Ligacao ligacao = new Ligacao();
			Date dataHora = new Date();
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("insert into hot_ligacao (ponto_id_a,ponto_id_b,ponto_id_tipo,data_hora) values (?,?,?,?)");
			ps.setObject(1, importante.getPontoId());
			ps.setObject(2, ponto.getPontoId());
			ps.setObject(3, pontoTipo.getPontoId());
			ps.setObject(4, dataHora);
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
	/**
	 * 
	 * @param ponto ponto a ser criado
	 * @return ponto achado ou criado
	 */
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
	 * @param pontoTipo tipo do ponto
	 * @return Ligacao
	 */
	public synchronized Ligacao ligarSeDesligado(Ponto importante, Ponto ponto,Ponto pontoTipo) {
		Connection con = null;
		try{
			Ligacao ligacao = new Ligacao();
			Date dataHora = new Date();
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("select * from hot_ligacao where ponto_id_a=? and ponto_id_b=? and ponto_id_tipo=?");
			ps.setObject(1, importante.getPontoId());
			ps.setObject(2, ponto.getPontoId());
			ps.setObject(3, pontoTipo.getPontoId());
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				ligacao.setLigacaoId(rs.getLong(1));
				ligacao.setPontoIdA(ponto.getPontoId());
				ligacao.setPontoIdB(importante.getPontoId());
				ligacao.setDataHora(dataHora);
				return ligacao;
			}else{
				return ligar(importante,ponto,pontoTipo);
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
	/**
	 * 
	 * @param ponto
	 * @param ligacaoTipo tipo de ligacao
	 * @return lista com os pontos, nunca será null
	 */
	public List<Ponto> getLigacaoA(Ponto ponto, Ponto ligacaoTipo) {
		Connection con = null;
		ArrayList<Ponto> res=new ArrayList<Ponto>();
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("Select * From hot_ponto p inner join hot_ligacao l" +
					" on l.ponto_id_a=p.ponto_id where l.ponto_id_b=? and l.ponto_id_tipo=? order by p.ponto_id");
			ps.setObject(1, ponto.getPontoId());
			ps.setObject(2, ligacaoTipo.getPontoId());
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
	 * @param ponto
	 * @param ligacaoTipo tipo de ligacao
	 * @return lista com os pontos, nunca será null
	 */
	public List<Ponto> getLigacaoB(Ponto ponto,Ponto ligacaoTipo) {
		Connection con = null;
		ArrayList<Ponto> res=new ArrayList<Ponto>();
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement("Select * From hot_ponto p inner join hot_ligacao l" +
					" on l.ponto_id_b=p.ponto_id where l.ponto_id_a=? and l.ponto_id_tipo=? order by p.ponto_id");
			ps.setObject(1, ponto.getPontoId());
			ps.setObject(2, ligacaoTipo.getPontoId());
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
	 * Encontra o ponto que representa este grupo
	 * @param listGrupo
	 * @param ligacaoTipo
	 * @return ponto que representa este grupo
	 */
	public List<Ponto> acharGrupo(List<Ponto> listGrupo,Ponto ligacaoTipo) {
		ArrayList<Ponto> res=new ArrayList<Ponto>();
		
		//acha todos os pontos em comum
		List<Ponto> resA = acharPontosAComum(listGrupo,ligacaoTipo);
		//verificar para cada res se é exatamente o listGrupo
		Collections.sort(listGrupo,comparatorByIdAsc);
		for(Ponto pA:resA){
			if(pA.getLigacaoB().size()==0){
				pA.setLigacaoB(this.getLigacaoB(pA,ligacaoTipo));
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
	 * @param ligacaoTipo 
	 * @return pontos A que tem como B todos da lista
	 */
	public List<Ponto> acharPontosAComum(List<Ponto> listGrupo, Ponto ligacaoTipo) {
		/*
		 * select * from proc p 
		 * inner join lig l1 on l1.idA = p.id
		 * inner join lig l2 on l2.idA = p.id
		 * inner join lig l3 on l3.idA = p.id
		 * where l1.idB = :idB1
		 * and l1.idTipo = :idTipo
		 * and l2.idB = :idB2
		 * and l2.idTipo = :idTipo
		 * and l3.idB = :idB3
		 * and l3.idTipo = :idTipo
		 */
		String sql = "Select p.ponto_id,p.nome,p.descricao,p.classe,p.data_hora from hot_ponto p ";
		String where = " where 1=1 ";
		int t = listGrupo.size();
		for(int i=0;i<t;i++){
			sql+=" inner join hot_ligacao l"+i+" on l"+i+".ponto_id_a=p.ponto_id ";
			where +=" and l"+i+".ponto_id_b="+listGrupo.get(i).getPontoId();
			where +=" and l"+i+".ponto_id_tipo="+ligacaoTipo.getPontoId();
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
				ponto.setLigacaoA(this.getLigacaoA(ponto,ligacaoTipo));
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
	public Ponto criarGrupo(Ponto pontoA, ArrayList<Ponto> listGrupo,Ponto ligacaoTipo) {
		//Persiste o ponto caso nao esteja
		if(pontoA.getPontoId()==null || pontoA.getPontoId().equals(0l)){
			pontoA = criar(pontoA);
		}
		for(Ponto pontoB:listGrupo){
			ligar(pontoA, pontoB,ligacaoTipo);
		}
		return pontoA;
	}
	/**
	 * 
	 * @param ponto ponto
	 * @param ligacaoTipo tipo de ligacao
	 * @return todos os pontos que possuem ligacao A ou B do tipo
	 */
	public List<Ponto> getLigacao(Ponto ponto,Ponto ligacaoTipo) {
		List<Ponto> res = getLigacaoA(ponto, ligacaoTipo);
		List<Ponto> resB = getLigacaoB(ponto, ligacaoTipo);
		res.addAll(resB);
		return res;
	}
}
