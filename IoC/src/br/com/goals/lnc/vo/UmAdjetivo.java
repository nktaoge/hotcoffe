package br.com.goals.lnc.vo;

public abstract class UmAdjetivo extends UmaPalavra{
	/**
	 * Valoriza uma caracteristica.<br>
	 * Ex.: a casa azul.<br>
	 * a cor da casa eh azul.<br>
	 * Na orientacao a objetos seria:<br>
	 * <code>casa.cor = azul;</code>
	 * <br>
	 * isto retornaria o substantivo "cor"
	 * para o adjetivo azul
	 */
	public abstract String caracteriza();
}
