package br.com.goals.hotcoffe;

public class HotCoffeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2254490190215267818L;

	public HotCoffeException() {

	}

	public HotCoffeException(String message) {
		super(message);
	}

	public HotCoffeException(Throwable cause) {
		super(cause);
	}

	public HotCoffeException(String message, Throwable cause) {
		super(message, cause);
	}

}
