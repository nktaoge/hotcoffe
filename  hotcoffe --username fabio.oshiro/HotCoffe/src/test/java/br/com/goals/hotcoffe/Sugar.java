package br.com.goals.hotcoffe;

import java.util.Date;

import br.com.goals.hotcoffe.FakeRequest;

public class Sugar extends Action implements Config{
	Model usuario;
	public static void main(String args[]){
		Sugar sugar = new Sugar();
		FakeRequest request = new FakeRequest();
		//request.setParameter("usuario.doubled", "3.2");
		request.setParameter("usuario.nome", "Fabio Issamu Oshiro");
		request.setParameter("usuario.doubled", "1");
		//request.setParameter("usuario.nasc", "29/05/1979");
		sugar.setRequest(request);
		HotCoffe hotCoffe = new HotCoffe(sugar);
		sugar.usuario = hotCoffe.getNewModel("usuario");
		ModelDao usuarioDao = hotCoffe.getModelDao();
		try {
			sugar.process();
			sugar.usuario.set("nome", "Fabio");
			sugar.usuario.set("inteiro", 1);
			sugar.usuario.set("longed",1L);
			sugar.usuario.set("datahora", new Date());
			//usuario.set("doubled",2.2);
			
			//usuario.save();
			usuarioDao.save(sugar.usuario);
			System.out.println("id = " + sugar.usuario.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	
	public String getDbDriver() {
		return "com.mysql.jdbc.Driver";
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

	
	public void execute() {
		
	}
}
