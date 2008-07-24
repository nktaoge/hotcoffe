import br.com.goals.tts.TTSVoiceGetter;

public class QuadmoreTTS
{
    static
    {
        System.loadLibrary("QuadTTS");
        QuadmoreTTS q = new QuadmoreTTS();
        TTSVoiceGetter.setQuadmore(q);
    }
    public native boolean SpeakDarling(String strInput);
    public native boolean setVoiceToken(String strVoiceToken);
    public native String getVoiceToken();
}