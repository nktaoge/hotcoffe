package br.com.goals.grafo;

import java.util.List;
import java.util.Scanner;

import br.com.goals.debug.Sysou;
import br.com.goals.grafo.controle.Conceitos;
import br.com.goals.grafo.controle.Entender;
import br.com.goals.grafo.controle.Escutar;
import br.com.goals.grafo.controle.Pensar;
import br.com.goals.grafo.controle.Perguntar;
import br.com.goals.grafo.controle.ArticularPalavras;
import br.com.goals.grafo.modelo.DuvidaException;
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
	private Entender entender = new Entender(this);
	private Pensar pensar;
	private ArticularPalavras responder = new ArticularPalavras();
	private Perguntar perguntar = new Perguntar();
	private List<Ponto> listPontosComA;
	private Sysou sysou = new Sysou(this,0);
	public CAL(){
		Conceitos.carregarConceitos();
		escutar = new Escutar(this);
		pensar = new Pensar(this);
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
		sysou.onEnterFunction(1,"processar");
		String retorno = "";
		if(texto!=null && !texto.trim().equals("")){
			sysou.println(1,"Processando \""+texto+"\"...");
			listPontosComA = escutar.escutar(texto,emissor);
			for(int i=0;i<listPontosComA.size();i++){
				sysou.println(2,"listPontosComA " + listPontosComA.get(i));
			}
			try{
				List<Ponto> listPontosComSentido = entender.entender(listPontosComA);
				
				List<Ponto> listPontosPensados = pensar.pensar(listPontosComSentido);
				
				//Articular palavras
				String res =responder.responder(listPontosPensados);
				retorno = res;
			}catch(DuvidaException e){
				retorno = perguntar.perguntar(e.getPontos());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		sysou.onExitFunction(1,"processar");
		return retorno;
	}
	public Emissor getEmissor() {
		return emissor;
	}
	public void setEmissor(Emissor emissor) {
		this.emissor = emissor;
	}
	public Pensar getPensar() {
		return pensar;
	}
	public void setPensar(Pensar pensar) {
		this.pensar = pensar;
	}
	public List<Ponto> getListPontosComA() {
		return listPontosComA;
	}
}
