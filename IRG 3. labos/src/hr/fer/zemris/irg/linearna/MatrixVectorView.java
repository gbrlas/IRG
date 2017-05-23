package hr.fer.zemris.irg.linearna;

/**
 * "Zivi" pogled na vektor u kojem odredjeni vektor reprezentiramo kao
 * jednodimenzionalnu matricu.
 * Nasljedjuje {@linkplain AbstractMatrix}.
 * 
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class MatrixVectorView extends AbstractMatrix {
	/**
	 * Referenca na originalni vektor.
	 */
	private IVector original;
	/**
	 * Boolean koji predstavlja treba li matrica imati jedan redak ili jedan stupac.
	 */
	private boolean asRowMatrix;
	
	/**
	 * Konstruktor koji stvara novi pogled na zadani vektor.
	 * 
	 * @param original Originalni vektor na koji stvaramo pogled.
	 * @param asRowMatrix Predstavlja treba li matrica imati jedan redak ili jedan stupac.
	 * @throws IllegalArgumentException - ako je predani vektor null.
	 */
	public MatrixVectorView(IVector original, boolean asRowMatrix) throws IllegalArgumentException {
		if (original == null) {
			throw new IllegalArgumentException("Invalid arguments passed to the MatrixVectorView constructor.");
		}
		this.original = original;
		this.asRowMatrix = asRowMatrix;
	}
	
	@Override
	public int getRowsCount() {
		if (asRowMatrix) {
			return 1;
		} else {
			return original.getDimension();
		}
	}
	
	@Override
	public int getColsCount() {
		if (asRowMatrix) {
			return original.getDimension();
		} else {
			return 1;
		}
	}
	
	@Override
	public double get(int row, int col) {
		if (row < 0 || col < 0 || row >= getRowsCount()
				|| col >= getColsCount()) {
			throw new IllegalArgumentException("Invalid index provided, try again.");
		}
		
		if (asRowMatrix) {
			return original.get(col);
		} else {
			return original.get(row);
		}
	}
	
	@Override
	public IMatrix set(int row, int col, double value) {
		if (row < 0 || col < 0 || row >= getRowsCount()
				|| col >= getRowsCount()) {
			throw new IllegalArgumentException("Invalid index provided, try again.");
		}
		
		if (asRowMatrix) {
			original.set(col, value);
		} else {
			original.set(row, value);
		}
		
		return this;
	}
	
	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new Matrix(rows, cols);
	}
	
	@Override
	public IMatrix copy() {
		IMatrix newMatrix = newInstance(this.getRowsCount(), this.getColsCount());
		
		if (asRowMatrix) {
			for (int i = 0; i < this.getRowsCount(); i++) {
				for (int j = 0; j < this.getColsCount(); j++) {
					newMatrix.set(i, j, original.get(j));
				}
			}
		} else {
			for (int i = 0; i < this.getRowsCount(); i++) {
				for (int j = 0; j < this.getColsCount(); j++) {
					newMatrix.set(i, j, original.get(i));
				}
			}
		}
		
		return newMatrix;
	}	
	
}
