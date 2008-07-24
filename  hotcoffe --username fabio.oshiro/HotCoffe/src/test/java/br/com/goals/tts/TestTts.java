package br.com.goals.tts;
import quadmore.jaxb.QuadmoreTTS;
import quadmore.jaxb.TTSVoiceGetter;
public class TestTts {
	public static void main(String args[]) {
		String lastVoice="ScanSoft Raquel_Full_22kHz";
		/*
		String strVoiceList = TTSVoiceGetter.getXML();
		
		int intPosition = 0;

		intPosition = strVoiceList.indexOf("<voice>");

		while(intPosition > 0)
		{
			strVoiceList = strVoiceList.substring(intPosition + 7);
			intPosition = strVoiceList.indexOf("</voice>");

			lastVoice = strVoiceList.substring(0,intPosition);
			//System.out.println(lastVoice);
			intPosition = strVoiceList.indexOf("<voice>");
		}
		//*/
		TTSVoiceGetter.setVoice(lastVoice);
		TTSVoiceGetter.setItemSelected(true);
		QuadmoreTTS ttss = new QuadmoreTTS();
		String strToken = TTSVoiceGetter.getVoice();
		ttss.setVoiceToken(strToken);
		//ttss.speakDarling("utilizando a voz "+lastVoice);
		String conteudo="testando ,1 2 3, som! 1 2 3. som! alôoo som.";		
		ttss.speakDarling(conteudo);
	}
}
