package br.com.goals.hotcoffe.grafo;

import java.util.Scanner;

import br.com.goals.debug.Sysou;
import br.com.goals.grafo.CAL;

public class TestarCAL {
	public static void ligarOuts(){
		try {
			Sysou.setVerboseLevelBySimpleName("CAL", 1);
			Sysou.setVerboseLevelBySimpleName("Pensar", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		ligarOuts();
		CAL cal = new CAL();
		Scanner sc = new Scanner(System.in);
		while(true){
			String s = sc.nextLine();
			System.out.println(cal.processar(s));
		}
	}
}
