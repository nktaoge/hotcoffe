package br.com.goals.grafo;

import java.util.List;
import java.util.Scanner;

import br.com.goals.grafo.controle.Conceitos;
import br.com.goals.grafo.controle.Entender;
import br.com.goals.grafo.controle.Escutar;
import br.com.goals.grafo.controle.Pensar;
import br.com.goals.grafo.controle.Perguntar;
import br.com.goals.grafo.controle.Responder;
import br.com.goals.grafo.modelo.Duvida;
import br.com.goals.grafo.modelo.Emissor;
import br.com.goals.grafo.modelo.Ponto;

/**
 * Baseado no:<br>
 * CAL series for hobbyist programming in limited environments
 * @author fabio
 *
 */
public class CAL {
	private Emissor emissor = new Emissor();
	private Escutar escutar;
	private Entender entender = new Entender();
	private Pensar pensar = new Pensar();
	private Responder responder = new Responder();
	private Perguntar perguntar = new Perguntar();
	public CAL(){
		Conceitos.carregarConceitos();
		escutar = new Escutar(this);
	}
	public static void main(String[] args) {
		CAL cal = new CAL();
		Scanner sc = new Scanner(System.in);
		while(true){
			String s = sc.nextLine();
			System.out.println(cal.processar(s));
		}
	}
	public String processar(String texto){
		System.out.println("Processando \""+texto+"\"...");
		List<Ponto> listListPontos = escutar.escutar(texto,emissor);
		try{
			List<Ponto> listPontosComSentido = entender.entender(listListPontos);
			List<Ponto> listPontosPensados = pensar.pensar(listPontosComSentido);
			return responder.responder(listPontosPensados);
		}catch(Duvida e){
			return perguntar.perguntar(e.getPontos());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	public Emissor getEmissor() {
		return emissor;
	}
	public void setEmissor(Emissor emissor) {
		this.emissor = emissor;
	}
}
