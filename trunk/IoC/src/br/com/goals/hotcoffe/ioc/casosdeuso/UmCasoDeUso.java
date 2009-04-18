package br.com.goals.hotcoffe.ioc.casosdeuso;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.Controlador;
import br.com.goals.hotcoffe.ioc.view.Template;

public abstract class UmCasoDeUso implements Runnable{
	private static Logger logger =Logger.getLogger(UmCasoDeUso.class);
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	private String key = null;
	protected Ator ator = new Ator(this);
	protected Sistema sistema = new Sistema(this);
	private Controlador controlador;
	private Template template = new Template(this);
	public void setTemplate(Template template) {
		this.template = template;
	}
	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Template getTemplate() {
		return template;
	}
	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
		if(template!=null){
			template.setControlador(controlador);
		}
	}
	public void setAtor(Ator ator) {
		this.ator = ator;
	}
	public Controlador getControlador() {
		return controlador;
	}
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
	@Override
	public void run() {
		executar();
	}
	/**
	 * Inicio do caso de uso
	 * @throws IOException
	 */
	protected abstract void iniciar() throws Exception;
	
	private void init(){
		//gerar um key
		key="";
		while(true){
			for(int i=0;i<10;i++){
				key+=(char) (Math.random()*26 + 'a');
			}
			if(!casosDeUso.containsKey(key)) break;
		}
		logger.debug("Gerado o key " + key + ". " + casosDeUso.size() + " casos de uso acontecendo.");
	}
	public String getKey(){
		return key;
	}
	/**
	 * Destroy da vida
	 */
	public final void finalizar(){
		casosDeUso.remove(key);
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
		synchronized (umCasoDeUso.ator) {
			umCasoDeUso.ator.notify();
		}
	}
	static HashMap<String,UmCasoDeUso> getCasosDeUso() {
		return casosDeUso;
	}
	public HttpServletResponse getResponse() {
		 return response;
	}
	protected void usar(UmCasoDeUso outroCasoDeUso) throws Exception {
		outroCasoDeUso.setControlador(getControlador());
		outroCasoDeUso.setRequestResponse(request, response);
		outroCasoDeUso.setAtor(ator);
		outroCasoDeUso.setSistema(sistema);
		outroCasoDeUso.setTemplate(template);
		outroCasoDeUso.setKey(key);
		outroCasoDeUso.iniciar();
	}
}
