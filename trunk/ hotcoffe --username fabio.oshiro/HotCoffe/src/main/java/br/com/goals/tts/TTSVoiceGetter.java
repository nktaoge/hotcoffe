package br.com.goals.tts;

public class TTSVoiceGetter
{
		private static String strVoice = "";
		private static boolean blnItemSelected = false;

		public static String getXML()
		{
			String strVoiceList = "";
			//TokensTTS tts = new TokensTTS();
        	//strVoiceList = tts.getVoiceToken();
			return strVoiceList;
		}

		//This stores the voice selected in JComboBox:
		public static void setVoice(String strValue)
		{
			strVoice = strValue;
		}

		public static String getVoice()
		{
			return strVoice;
		}

		public static boolean isItemSelected()
		{
			return blnItemSelected;
		}

		public static void setItemSelected(boolean blnValue)
		{
			blnItemSelected = blnValue;
		}

		public static void setQuadmore(Object q) {
			// TODO Auto-generated method stub
			
		}
}
