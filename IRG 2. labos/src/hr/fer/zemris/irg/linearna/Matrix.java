package hr.fer.zemris.irg.linearna;

/**
 * Klasa koja predstavlja jednu matricu proizvoljnih dimenzija 
 * i realnih vrijednosti. 
 * Nasljedjuje {@linkplain AbstractMatrix}.
 * 
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class Matrix extends AbstractMatrix {
	/**
	 * Polje elemenata matrice.
	 */
	private double[][] array;
	/**
	 * Boolean koji govori mogu li se elementi matrice mijenjati.
	 */
	public boolean notModifiable = false;
	/**
	 * Broj redaka matrice.
	 */
	private int rows;
	/**
	 * Broj strupaca matrice.
	 */
	private int cols;
	
	/**
	 * Konstruktor koji stvara praznu matricu zadanih dimenzija.
	 * 
	 * @param rows Broj redaka matrice.
	 * @param cols Broj stupaca matrice.
	 */
	public Matrix(int rows, int cols) {
		this(rows, cols, rows > 0 && cols > 0 ? new double[rows][cols] : null, false);
	}
	
	/**
	 * Konstruktor koji stvara novu matricu na temelju predanog polja i zadanih velicina.
	 * 
	 * @param rows Broj redaka matrice.
	 * @param cols Broj stupaca matrice.
	 * @param array Polje matrice.
	 * @param useGiven
	 *            Boolean koji govori moze li se koristiti predano polje ili
	 *            matrica mora stvoriti vlastitu kopiju predanog polja.
	 * @throws IllegalArgumentException
	 *             - ako su zadane velicine stupaca ili redaka manje od 0 ili je
	 *             kao polje predan null.
	 */
	public Matrix(int rows, int cols, double[][] array, boolean useGiven) throws IllegalArgumentException {
		if (rows < 0 || cols < 0 || array == null) {
			throw new IllegalArgumentException("Invalid paramaters provided to the matrix constructor.");
		}
		
		if (useGiven) {
			this.array = array;
		} else {
			this.array = new double[rows][cols];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					this.array[i][j] = array[i][j];
				}
			}
		}
		
		this.rows = rows;
		this.cols = cols;
	}
	
	@Override
	public IMatrix copy() {
		IMatrix matrix = newInstance(rows, cols);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix.set(i, j, array[i][j]);
			}
		}
		
		return matrix;
	}
	
	@Override
	public double get(int row, int col) {
		if (row < 0 || row >= this.rows || col < 0 || col >= this.cols) {
			throw new IllegalArgumentException("Invalid index, try again.");
		}
		return array[row][col];
	}
	
	@Override
	public int getColsCount() {
		return cols;
	}
	
	@Override
	public int getRowsCount() {
		return rows;
	}
	
	@Override
	public IMatrix newInstance(int rows, int cols) {
		if (rows < 0 || cols < 0) {
			throw new IllegalArgumentException("Ivalid arguments for newInstance method.");
		}
		return new Matrix(rows, cols);
	}
	
	/**
	 * Parsira predani String koji predstavlja matricu.
	 * Retci matrice moraju biti odvojeni sa znakom "|" (bez navodnika), a stupci razmakom.
	 * 
	 * @param text String koji predstavlja matricu.
	 * @return Nova matrica nastala parsiranjem predane definicije.
	 * @throws IllegalArgumentException - ako je predani String prazan.
	 */
	public static Matrix parseSimple (String text) throws IllegalArgumentException {
		if (text.length() == 0) {
			throw new IllegalArgumentException("Invalid string passed to the matrix parser.");
		}
		
		text = text.replaceAll("\\s++", " ");
		String[] rows = text.split("\\|");
		
		double[][] array = new double[rows.length][rows[0].split(" ").length];
		
		int i = 0;
		for (String red : rows) {
			red = red.trim();
			int j = 0;
			String[] temp = red.split(" ");
			
			for (String value : temp) {
				value = value.trim();
				array[i][j++] = Double.parseDouble(value);
			}
			i++;
		}
		
		return new Matrix(rows.length, rows[0].split(" ").length, array, false);
	}
	
	@Override
	public IMatrix set(int row, int col, double value) {
		if (notModifiable) {
			throw new UnmodifiableObjectException();
		} else if (row < 0 || col < 0 || row >= this.getRowsCount() || col >= this.getColsCount()){
			throw new IllegalArgumentException("Invalid index, try again.");
		}
		
		array[row][col] = value;
		return this;
	}
	
	
}
