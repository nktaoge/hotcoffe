package br.com.goals.hotcoffe.ioc.casosdeuso;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.hotcoffe.ioc.Controlador;

public abstract class UmCasoDeUso{
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	private String key = null;
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
	public final void executar(){
		//gerar key
		init();
		try{
			iniciar();
		}catch(Exception e){
			e.printStackTrace();
		}
		finalizar();
	}
	/**
	 * Inicio do caso de uso
	 * @throws IOException
	 */
	protected abstract void iniciar() throws IOException;
	
	private void init(){
		aguardar=false;
		//gerar um key
		key="";
		while(true){
			for(int i=0;i<8;i++){
				key+=(char) (Math.random()*26 + 'a');
			}
			if(!casosDeUso.containsKey(key)) break;
		}
		System.out.println("key" + key);
	}
	public String getKey(){
		return key;
	}
	/**
	 * Destroy da vida
	 */
	public final void finalizar(){
		casosDeUso.remove(key);
		Template.finalizar(this);
		Controlador controlador = (Controlador)request.getAttribute(Controlador.IOC_KEY);
		synchronized (controlador) {
			controlador.notify();
		}
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
