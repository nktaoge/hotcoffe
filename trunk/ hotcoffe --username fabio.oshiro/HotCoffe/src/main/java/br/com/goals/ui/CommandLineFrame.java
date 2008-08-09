package br.com.goals.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class CommandLineFrame extends JFrame implements KeyboardEventsProvider{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5644490343148695548L;
	JTextArea commando = new JTextArea();
	private int specialKey=0;
	public CommandLineFrame(){
		add(commando);
		commando.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				//specialKey=e.getKeyCode();
				//System.out.println(specialKey);
			}
			public void keyReleased(KeyEvent e) {
				specialKey=e.getKeyCode();
				//System.out.println(specialKey);
			}
			public void keyTyped(KeyEvent e) {
				//specialKey=e.getKeyCode();
				//System.out.println(specialKey);
			}
		});
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		CommandLine commandLine = new CommandLine();
		commandLine.setScanner(this);
		commandLine.doWork();
	}
	public static void main(String args[]){
		new CommandLineFrame();
	}
	public String nextLine() {
		String retorno="";
		try{
			while(specialKey!=9 && specialKey!=13 && specialKey!=10){
				Thread.sleep(100);
			}
			retorno=commando.getText().replace("\r","\t");
			specialKey=0;
			commando.setText("");
		}catch(Exception e){
			
		}
		return retorno;
	}
}
