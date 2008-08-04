package br.com.goals.ui;

import java.util.Scanner;

import br.com.goals.tts.TTS;
import br.com.goals.ui.programas.CommandLineClient;

public class CommandLine { 
	Scanner scanner = new Scanner(System.in);
	public CommandLine(){
		while(true){
			TTS.speak("o que você deseja?");
			String texto = scanner.nextLine();
			try{
				String classe = Interpretador.acharNomeClasse(texto);
				CommandLineClient cli=(CommandLineClient)Class.forName("br.com.goals.ui.programas."+classe).newInstance();
				//cli.setCommandLine(this);
				cli.start();
				while(cli.isBusy()){
					String cmd = getNextLine();
					System.out.println("cmd="+cmd);
					cli.setLine(cmd);
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
		new CommandLine();		
	}
}
