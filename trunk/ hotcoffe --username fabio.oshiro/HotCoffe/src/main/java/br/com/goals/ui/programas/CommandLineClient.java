package br.com.goals.ui.programas;

import br.com.goals.tts.TTS;
import br.com.goals.ui.CommandLine;

public abstract class CommandLineClient {
	protected CommandLine commandLine;
	private String line = null;
	private boolean busy = false;

	public CommandLineClient() {
		busy = true;
	}

	public void onKeyPressEnter() {

	}

	protected String getNextLine() {
		try {
			while (line == null) {
				Thread.sleep(300);
			}
			onKeyPressEnter();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return line;
	}

	public void setCommandLine(CommandLine commandLine) {
		this.commandLine = commandLine;
	}

	public void start() {
		Thread t = new Thread() {
			public void run() {
				try {
					execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
				busy = false;
			}
		};
		t.start();
	}

	public abstract void execute() throws Exception;

	public void speak(final String text) {
		String txt = text.trim();
		if(!txt.equals("")){
			TTS.speak(txt);
			try{
				Thread.sleep(300);
			}catch(Exception e){
				
			}
		}
	}

	public synchronized void setLine(String line) {
		setNextLine(line);
		this.line = line;
	}

	public abstract void setNextLine(String nextLine);

	public boolean isBusy() {
		return busy;
	}
}
