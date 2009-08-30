package br.com.goals.etrilhas.servlet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Servlet Filter implementation class IpFlutuante
 */
public class IpFlutuante implements Filter {

	private static Boolean rodando = true;
	private static Thread atualizadorDeIp;
    /**
     * Default constructor. 
     */
    public IpFlutuante() {
    	atualizadorDeIp = new Thread(){
			public void run() {
				while(rodando){
					//atualizar o Ip na goals
					System.out.println("Atualizando o ip na goals");
					try {
						URL url = new URL("http://www.goals.com.br/remote_hosts/?nome=fabio&senha=fabio123");
						url.getContent();
						Thread.sleep(60000);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		try{
			atualizadorDeIp.interrupt();
			rodando = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		atualizadorDeIp.start();
	}

}
