package br.com.goals.tts;

import quadmore.jaxb.QuadmoreTTS;
import quadmore.jaxb.TTSVoiceGetter;
/**
 * Cuida do TTS
 * @author Fabio Issamu Oshiro
 *
 */
public class TTS {
	private static TTS instance;
	private QuadmoreTTS ttss;
	static{
		instance=new TTS();
	}
	private TTS(){
		String lastVoice="ScanSoft Raquel_Full_22kHz";
		TTSVoiceGetter.setVoice(lastVoice);
		TTSVoiceGetter.setItemSelected(true);
		ttss = new QuadmoreTTS();
		String strToken = TTSVoiceGetter.getVoice();
		ttss.setVoiceToken(strToken);
		
		//ttss.speakDarling("utilizando a voz "+lastVoice);
		//String conteudo="testando ,1 2 3, som! 1 2 3. som! alôoo som.";		
		//instance=this;
	}
	public static void speak(String texto){
		//System.out.println("Falando: '"+texto+"'");
		instance.ttss.speakDarling(texto);
	}
	public static void speak(int j) {
		speak(j+"");
	}
}
