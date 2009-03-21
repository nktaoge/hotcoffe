package br.com.goals.hotcoffe.ioc.casosdeuso;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.spi.SyncResolver;

public abstract class UmCasoDeUso implements Runnable{
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	protected Ator ator = new Ator(this);
	public boolean aguardar;
	/**
	 * Request Forgery, fixed
	 * Casos de uso rodando
	 */
	private static HashMap<String,UmCasoDeUso> casosDeUso = new HashMap<String,UmCasoDeUso>();
	public static UmCasoDeUso getCasoDeUso(String id){
		return casosDeUso.get(id);
	}
	@Override
	public void run() {
		try{
			this.executar();			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	public final void executar(){
		aguardar=false;
		try{
			iniciar();
		}catch(Exception e){
			e.printStackTrace();
		}
		Template.finalizar(this);
		request.setAttribute("liberar","true");
	}
	abstract void iniciar() throws IOException;
	protected void mostrarAoUsuario(String string) {
		
	}
	public void setRequestResponse(HttpServletRequest request,HttpServletResponse response) {
		this.request = request;
		this.response = response;
		ator.setRequestResponse(request,response);
	}
	public static void acordar(UmCasoDeUso umCasoDeUso) {
		umCasoDeUso.aguardar=false;
		synchronized (umCasoDeUso.ator) {
			umCasoDeUso.ator.notify();
		}
	}
	static HashMap<String,UmCasoDeUso> getCasosDeUso() {
		return casosDeUso;
	}
}
