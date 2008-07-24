
public class TestTts {
	public static void main(String args[]) {
		String strVoiceList = TTSVoiceGetter.getXML();
		String lastVoice=null;
		int intPosition = 0;

		intPosition = strVoiceList.indexOf("<voice>");

		while(intPosition > 0)
		{
			strVoiceList = strVoiceList.substring(intPosition + 7);
			intPosition = strVoiceList.indexOf("</voice>");

			lastVoice = strVoiceList.substring(0,intPosition);
			intPosition = strVoiceList.indexOf("<voice>");
		}
		TTSVoiceGetter.setVoice(lastVoice);
		TTSVoiceGetter.setItemSelected(true);
		QuadmoreTTS ttss = new QuadmoreTTS();
		String strToken = TTSVoiceGetter.getVoice();
		ttss.setVoiceToken(strToken);
		//new String()
		ttss.SpeakDarling("olá pontuação");
	}
}
