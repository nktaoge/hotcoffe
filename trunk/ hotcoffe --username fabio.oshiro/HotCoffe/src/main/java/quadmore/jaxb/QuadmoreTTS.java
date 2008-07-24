package quadmore.jaxb;

public class QuadmoreTTS
{
    static
    {
        System.loadLibrary("QuadTTS");
    }
    public static void main(String args[])
    { }
    public boolean speakDarling(String strInput){
    	return SpeakDarling(strInput);
    }
    private native boolean SpeakDarling(String strInput);
    public native boolean setVoiceToken(String strVoiceToken);
    public native String getVoiceToken();
}