package br.com.goals.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.com.goals.tts.TTS;

public class DisplayMail {
	static boolean delete = false;
	static int deletar;
	private BufferedWriter out;
	private BufferedReader in;
	static Pattern patSubject = Pattern.compile("Subject: (.*)");
	static Pattern patFrom = Pattern.compile("From: (.*)");
	private String password;
	private String login;
	private String popServer;
	public static String getSubject(String msg){
		Matcher mat = patSubject.matcher(msg);
		if(mat.find()){
			return mat.group(1);
		}
		return "Sem título";
	}
	public static String getFrom(String msg){
		Matcher mat = patFrom.matcher(msg);
		if(mat.find()){
			return mat.group(1);
		}
		return "ninguem";
	}
	public static void main(String args[]) {
		DisplayMail mail = new DisplayMail();
		mail.doWork();
	}
	public void doWork(){
		boolean tryagain;
		int startIn =17;
		//deletar=14;
		do{
			tryagain=false;
			String arg[] = new String[3];
			arg[0] = popServer;
			arg[1] = login;
			arg[2] = password;
			//
			// usage :
			// DisplayMail [mailServer] [user] [password]
			// (will not delete mail on the server)
			//
			int j=0;
			try {
				// connect on port 110 (POP3)
				System.out.println("Connect to " + arg[0] + ":110");
				Socket s = new Socket(arg[0], 110);
				in = new BufferedReader(new InputStreamReader(s
						.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(s
						.getOutputStream()));
				
				login(in, out, arg[1], arg[2]);
				if(delete){
					//se algum ficou para apagar
					if(deletar!=0){
						for (j = 1; j <= deletar; j++) {
							delete(j);
							Thread.sleep(100);
						}
						send(out, "QUIT");
						tryagain = true;
						deletar = 0;
						continue;
					}
				}
				int i = check(in, out);
				TTS.speak("você possui "+i+" e-mails.");
				if (i == 0) {
					System.out.println("No mail waiting.");
				} else {
					for (j = startIn; j <= i; j++) {
						String msg = get(in, out, j);
						System.out.println("*****");
						System.out.println(msg);
						Thread.sleep(500);
						TTS.speak("mensagem");
						Thread.sleep(500);
						TTS.speak(j);
						Thread.sleep(500);
						TTS.speak(getFrom(msg));
						Thread.sleep(500);
						TTS.speak("Assunto");
						Thread.sleep(500);
						TTS.speak(getSubject(msg));
						System.out.println("\n\n\n\n\n\n\n");
					}
					//
					// If the mail was removed from the server
					// (see get()) then we must COMMIT with
					// the "QUIT" command :
					if (delete) {
						send(out, "QUIT");
					}
					//
				}
			} catch (Exception e) {
				e.printStackTrace();
				deletar = j;
				tryagain=true;
			}
		}while(tryagain);
	}
	public void delete(int i){
		if (delete) {
			try{
				send(out, "DELE " + i);
				receive(in);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public String get(BufferedReader in, BufferedWriter out, int i)
			throws IOException {
		String s = "";
		String t = "";
		send(out, "RETR " + i);
		//long count=0;
		while (((s = in.readLine()) != null) && (!(s.equals(".")))) {
			t += s + "\n";
			//System.out.println(i+" "+(count++));
		}
		//
		// To remove the mail on the server :
		delete(i);
		//
		return t;
	}

	private void send(BufferedWriter out, String s) throws IOException {
		System.out.println(s);
		out.write(s + "\n");
		out.flush();
	}

	private String receive(BufferedReader in) throws IOException {
		String s = in.readLine();
		System.out.println(s);
		return s;
	}

	private void login(BufferedReader in, BufferedWriter out, String user,
			String pass) throws IOException {
		receive(in);
		send(out, "HELO theWorld");
		receive(in);
		send(out, "USER " + user);
		receive(in);
		send(out, "PASS " + pass);
		receive(in);
	}

	private int check(BufferedReader in, BufferedWriter out) throws IOException {
		return getNumberOfMessages(in, out);
	}

	public int getNumberOfMessages(BufferedReader in, BufferedWriter out)
			throws IOException {
		int i = 0;
		String s;

		send(out, "LIST");
		receive(in);
		while ((s = receive(in)) != null) {
			if (!(s.equals("."))) {
				i++;
			} else {
				return i;
			}
		}
		return 0;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setPopServer(String popServer) {
		this.popServer = popServer;
	}
}
