package hr.fer.zemris.irg.linearna;

/**
 * Iznimka koja se baca ako se nad matricom ili vektorom zatraži provođenje
 * operacije s drugim objektom koji nije kompatibilan s obzirom na zatraženu
 * operaciju.
 *
 * @author marcupic
 *
 */
public class IncompatibleOperandException extends RuntimeException {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 */
	public IncompatibleOperandException() {
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
	public IncompatibleOperandException(String message) {
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
	public IncompatibleOperandException(Throwable cause) {
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
	public IncompatibleOperandException(String message, Throwable cause) {
		super(message, cause);
	}
}
