package br.com.goals.ui;

import java.util.Scanner;

import br.com.goals.grafo.CAL;
import br.com.goals.tts.TTS;
import br.com.goals.ui.programas.CommandLineClient;

public class CommandLine {
	CAL cal = new CAL();
	class FScanner implements KeyboardEventsProvider{
		Scanner scanner = new Scanner(System.in);
		public String nextLine(){
			return scanner.nextLine();
		}
	}
	KeyboardEventsProvider scanner = new FScanner();
	
	public void doWork(){
		
		while(true){
			TTS.speak("");
			String texto = scanner.nextLine();
			try{
				String classe = Interpretador.acharNomeClasse(texto);
				CommandLineClient cli=(CommandLineClient)Class.forName("br.com.goals.ui.programas."+classe).newInstance();
				//cli.setCommandLine(this);
				cli.start();
				while(cli.isBusy()){
					String cmd = getNextLine();
					//System.out.println("cmd="+cmd);
					cli.setLine(cmd);
				}
			}catch(ClassNotFoundException e){
				String resposta = cal.processar(texto);
				if(resposta==null){
					TTS.speak("Sem resposta");
				}else{
					TTS.speak(resposta);
				}
			}catch(Exception e){
				TTS.speak("erro: "+e.getMessage());
				e.printStackTrace();
			}
		}	
	}
	private String getNextLine(){
		return scanner.nextLine();
	}
	public static void main(String args[]){
		new CommandLine().doWork();		
	}
	public KeyboardEventsProvider getScanner() {
		return scanner;
	}
	public void setScanner(KeyboardEventsProvider scanner) {
		this.scanner = scanner;
	}
}
