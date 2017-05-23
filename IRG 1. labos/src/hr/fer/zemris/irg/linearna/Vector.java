package hr.fer.zemris.irg.linearna;

/**
 * Klasa koja predstavlja vektor realnih brojeva. Vektori predstavljeni ovom
 * klasom su vektori realnih brojeva i mogu biti proizvoljnih dimenzija.
 * Implementira osnovne metode za dobivanje informacija o vektoru te za
 * provođenje čestih operacija nad vektorima. Implementira
 * {@linkplain AbstractVector}.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class Vector extends AbstractVector {
	/**
	 * Polje elemenata vektora.
	 */
	private double[] array;
	/**
	 * Boolean koji govori mogu li se elementi vektora mijenjati.
	 */
	private boolean notModifiable = false;
	/**
	 * Dimenzija vektora.
	 */
	private final int dimension;

	/**
	 * Konstruktor koji stvara novi vektor na temelju predanog polja elemenata.
	 *
	 * @param args
	 *            Polje elemenata od kojeg treba stvoriti vektor.
	 */
	public Vector(double... args) {
		this(false, false, args);
	}

	/**
	 * Konstruktor koji stvara novi vektor na temelju predanog polja elemenata
	 * te predanih zastavica.
	 *
	 * @param modifiable
	 *            Boolean koji govori mogu li se vrijednosti vektora mijenjati.
	 * @param usable
	 *            Boolean koji govori moze li se koristiti predano polje ili
	 *            vektor mora stvoriti vlastitu kopiju predanog polja.
	 * @param args
	 *            Polje elemenata od kojeg treba stvoriti vektor.
	 * @throws IllegalArgumentException
	 *             - ako je predano polje null.
	 */
	public Vector(boolean modifiable, boolean usable, double... args)
			throws IllegalArgumentException {
		if (args == null) {
			throw new IllegalArgumentException();
		}

		if (modifiable) {
			this.notModifiable = modifiable;
		}

		if (usable) {
			array = args;
		} else {
			array = new double[args.length];

			for (int i = 0; i < args.length; i++) {
				array[i] = args[i];
			}
		}

		dimension = array.length;
	}

	@Override
	public IVector copy() {
		final IVector vector = newInstance(dimension);

		for (int i = 0; i < dimension; i++) {
			vector.set(i, array[i]);
		}

		return vector;
	}

	@Override
	public double get(int index) {
		if (index < 0 || index >= dimension) {
			throw new IllegalArgumentException(
					"Index is out of bounds, try again.");
		}

		return array[index];
	}

	@Override
	public int getDimension() {
		return dimension;
	}

	@Override
	public IVector newInstance(int dimension) {
		return new Vector(new double[dimension]);
	}

	@Override
	public IVector set(int index, double value)
			throws IllegalArgumentException, UnmodifiableObjectException {
		if (index < 0 || index >= dimension) {
			throw new IllegalArgumentException(
					"Index is out of bounds, try again.");
		} else if (notModifiable) {
			throw new UnmodifiableObjectException(
					"This vector is non-modifiable.");
		}

		array[index] = value;

		return this;
	}

	/**
	 * Parsira predani String koji predstavlja vektor. Stupci vektora moraju
	 * biti rastavljeni razmakom.
	 *
	 * @param text
	 *            String koji predstavlja vektor.
	 * @return Novi vektor nastala parsiranjem predane definicije.
	 * @throws IllegalArgumentException
	 *             - ako je predani String prazan.
	 */
	public static Vector parseSimple(String text)
			throws IllegalArgumentException {
		if (text.length() == 0) {
			throw new IllegalArgumentException(
					"Invalid string passed to the matrix parser.");
		}

		text = text.replaceAll("\\s++", " ");
		final String[] values = text.split(" ");

		int i = 0;
		final double[] args = new double[values.length];
		for (final String value : values) {
			args[i++] = Double.parseDouble(value);
		}

		return new Vector(args);
	}

}
