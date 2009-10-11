package br.com.goals.template;

public class AreaNaoEncontradaException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AreaNaoEncontradaException(String string) {
		super("Area nao encontrada '" + string + "'");
	}

}
