package hr.fer.zemris.irg.linearna;

/**
 * Iznimka koja se baca ako se nad matricom ili vektorom koji je nepromjenjiv
 * (tj. read-only) zatraži provođenje operacije koja objekt pokušava na bilo
 * koji način promijeniti.
 *
 * @author marcupic
 *
 */
public class UnmodifiableObjectException extends RuntimeException {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 */
	public UnmodifiableObjectException() {
		super();
	}

	/**
	 * Konstruktor.
	 *
	 * @param message
	 *            poruka
	 *
	 * @see RuntimeException#RuntimeException(String)
	 */
	public UnmodifiableObjectException(String message) {
		super(message);
	}

	/**
	 * Konstruktor.
	 *
	 * @param cause
	 *            razlog zbog kojeg je iznimka izazvana
	 *
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public UnmodifiableObjectException(Throwable cause) {
		super(cause);
	}

	/**
	 * Konstruktor.
	 *
	 * @param message
	 *            poruka
	 * @param cause
	 *            razlog zbog kojeg je iznimka izazvana
	 *
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public UnmodifiableObjectException(String message, Throwable cause) {
		super(message, cause);
	}

}
