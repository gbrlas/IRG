package hr.fer.zemris.irg.linearna;

/**
 * Klasa koja sluzi za stvaranje defaultnih matrica i vektora.
 * 
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class LinAlgDefaults {
	
	/**
	 * Metoda koja stvara defaultnu praznu matricu zadane velicine.
	 * 
	 * @param rows Broj redaka matrice.
	 * @param cols Broj stupaca matrice.
	 * @return Novostvorena matrica.
	 * @throws IllegalArgumentException - ako je bilo koja od predanih velicina negativna.
	 */
	public static IMatrix defaultMatrix (int rows, int cols) throws IllegalArgumentException {
		if (rows < 0 || cols < 0) {
			throw new IllegalArgumentException("Invalid arguments passed to the defaultMatrix creator.");
		}
		return new Matrix(rows, cols);
	}
	
	/**
	 * Metoda koja stvara defaultni prazni vektor zadane dimenzije.
	 * 
	 * @param dimension Dimenzija vektora.
	 * @return Novostvoreni vektor.
	 * @throws IllegalArgumentException - ako je predana velicina vektora negativna.
	 */
	public static IVector defaultVector (int dimension) throws IllegalArgumentException {
		if (dimension < 0) {
			throw new IllegalArgumentException("Invalid argument passed to the defaultVector creator.");
		}
		return new Vector(new double[dimension]);
	}
}
