package br.com.goals.utils;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;

 
/**
 * Classe responsável pela captura do conteúdo de uma página na Internet.
 * 
 */
public final class Http {
	private HttpClient httpClient;
	public HttpMethod metodo;
	/**
	 * Construtor de PegarPaginaWEB.
	 */
	public Http(int a) {
	}

	public Http() {
		// me parece que o construtor aqui apenas inicializa alguns parametros e
		// registra o protocolo

		httpClient = new HttpClient();
		//ps = new PainelSenha();
		//httpClient.getParams().setParameter(CredentialsProvider.PROVIDER, ps); 
		EasySSLProtocolSocketFactory sssl = new EasySSLProtocolSocketFactory();
		// StrictSSLProtocolSocketFactory sssl = new
		// StrictSSLProtocolSocketFactory();
		// sssl.setHostnameVerification(false);
		Protocol easyhttps = new Protocol("https", sssl, 443);
		Protocol.registerProtocol("https", easyhttps);
	}
	/**
	 * Independente de Relatorio
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws NotHTML
	 * @throws TempoExcedido
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public String getContent(String url) throws HttpException, IOException{
		HttpMethod metodo = new GetMethod(url);
		metodo.setRequestHeader("user-agent", "Mozilla/5.0");
		metodo.setFollowRedirects(true);
		int status = httpClient.executeMethod(metodo);
		String type = getContentType(metodo);
		String location = getLocation(metodo);
		//Verificar os possíveis erros
		if (status!=HttpStatus.SC_OK){
			//Não foi aceito, ocorreu um erro 500 404
			if(status==HttpStatus.SC_NOT_FOUND){
			}
			return "";
		}
		if ((status == HttpStatus.SC_OK) && (type.toUpperCase().indexOf("TEXT/HTML") == -1)) {
			//Não é do tipo texto/html
			return "";
		}
		//Verifica redirecionamento
		if (location!=""){
			//System.out.print(url+" to "+location+"\n");
		}
		String conteudoHTML=metodo.getResponseBodyAsString();
		
		return conteudoHTML;
	}
	/**
	 * Pega o código css
	 * @param url
	 * @return
	 * @throws Exception 
	 */
	public String getCssContent(String url) throws Exception{
		metodo = new GetMethod(url);
		metodo.setRequestHeader("user-agent", "Mozilla/5.0");
		metodo.setFollowRedirects(true);
		int status = httpClient.executeMethod(metodo);
		String location = getLocation(metodo);
		//Verificar os possíveis erros
		if (status!=HttpStatus.SC_OK){
			//Não foi aceito, ocorreu um erro 500 404
			if(status==HttpStatus.SC_NOT_FOUND){
			}
			throw new Exception("Erro de http " +status);
			//return "";
		}
		//Verifica redirecionamento
		if (location!=""){
			//System.out.print(url+" to "+location+"\n");
		}
		String conteudoHTML=metodo.getResponseBodyAsString();
		
		return conteudoHTML;
	}
	/**
	 * Método que retorna o Content-type de uma página web.
	 * 
	 * @param metodo
	 *            Uma instância de org.apache.commons.httpclient.HttpMethod
	 *            inicializada pela página.
	 * @return O Content-Type da página pesquisada.
	 */
	private static String getContentType(final HttpMethod metodo) {
		String type = "";
		Header header = metodo.getResponseHeader("Content-Type");
		if (header != null) {
			type = header.getValue();
		}
		return type;
	}
	
	
	public static String getContentLength(final HttpMethod metodo){
		String retorno = "";
		Header header = metodo.getResponseHeader("Content-Length");
		if (header != null) {
			retorno = header.getValue().toString();
		}
		return retorno;
	}

	private static String getLocation(final HttpMethod metodo){
		String retorno = "";
		Header header = metodo.getResponseHeader("Location");
		if (header != null) {
			retorno = header.getValue().toString();
		}
		return retorno;
	}
}