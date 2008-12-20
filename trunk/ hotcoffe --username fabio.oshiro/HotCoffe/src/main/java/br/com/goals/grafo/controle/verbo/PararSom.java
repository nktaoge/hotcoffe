package br.com.goals.grafo.controle.verbo;

import java.util.List;

import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.tts.TTS;

public class PararSom extends SuperVerbo{

	@Override
	public List<Ponto> executar(Ponto sujeito, Ponto predicado) {
		TTS.setDontSpeak(true);
		return null;
	}

}
