package br.com.goals.ui;

import java.util.Scanner;

import br.com.goals.tts.TTS;
import br.com.goals.utils.DisplayMail;

public class CommandLine {
	public static void main(String args[]){
		Scanner scanner = new Scanner(System.in);
		DisplayMail displayMail = new DisplayMail();
		TTS.speak("Digite o servidor pop:");
		displayMail.setPopServer(scanner.next());
		TTS.speak("Digite o seu login:");
		displayMail.setLogin(scanner.next());
		TTS.speak("Digite a sua senha:");
		displayMail.setPassword(scanner.next());
		displayMail.doWork();
	}
}
