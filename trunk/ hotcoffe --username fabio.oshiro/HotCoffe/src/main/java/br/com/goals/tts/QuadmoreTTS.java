package br.com.goals.tts;

public class QuadmoreTTS
{
    static
    {
        System.loadLibrary("QuadTTS");
    }
    public native boolean SpeakDarling(String strInput);
    public native boolean setVoiceToken(String strVoiceToken);
    public native String getVoiceToken();
}