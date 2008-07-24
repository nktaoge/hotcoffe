package br.com.goals.tts;

class TokensTTS
{
    static
    {
        System.loadLibrary("QuadTkns");
    }
    public static void main(String args[])
    { }
    public native String getVoiceToken();
}