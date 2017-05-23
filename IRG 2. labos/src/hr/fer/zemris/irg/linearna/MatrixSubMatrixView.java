package hr.fer.zemris.irg.linearna;

/**
 * "Zivi" pogled na matricu u kojoj se zanemaruje odredjeni redak te odredjeni stupac.
 * Nasljedjuje {@linkplain AbstractMatrix}.
 * 
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class MatrixSubMatrixView extends AbstractMatrix {
	/**
	 * Referenca na originalnu matricu.
	 */
	private IMatrix original;
	/**
	 * Retci koje mozemo koristiti.
	 */
	private int[] rowIndexes;
	/**
	 * Strupci koje mozemo koristiti.
	 */
	private int[] colIndexes;
	
	/**
	 * Konstruktor koji stvara novi pogled na predanu matricu koristeci 
	 * predane retke i stupce.
	 * 
	 * @param original Referenca na originalnu matricu.
	 * @param rowIndexes Indexi redaka koje mozemo koristiti.
	 * @param colIndexes Indexi stupaca koje mozemo koristiti.
	 * @throws IllegalArgumentException - ako je bilo koji od predanih argumenata null.
	 */
	public MatrixSubMatrixView(IMatrix original, int[] rowIndexes,
			int[] colIndexes) throws IllegalArgumentException {
		
		if (original == null || rowIndexes == null || colIndexes == null) {
			throw new IllegalArgumentException("Invalid arguments passed to the SubMatrixView constructor.");
		}
		
		this.original = original;
		this.rowIndexes = rowIndexes;
		this.colIndexes = colIndexes;
	}
	
	@Override
	public int getRowsCount() {
		return rowIndexes.length;
	}
	
	@Override
	public int getColsCount() {
		return colIndexes.length;
	}
	
	@Override
	public IMatrix copy() {
		IMatrix newMatrix = newInstance(this.getRowsCount(), this.getColsCount());
		
		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getRowsCount(); j++) {
				newMatrix.set(i, j, original.get(rowIndexes[i], colIndexes[j]));
			}
		}
		
		return newMatrix;
	}
	
	@Override
	public double get(int row, int col) {
		if (row < 0 || col < 0 || row >= this.getRowsCount()
				|| col >= this.getColsCount()) {
			throw new IllegalArgumentException("Invalid index provided, try again.");
		}
		return original.get(rowIndexes[row], colIndexes[col]);
	}
	
	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new Matrix(rows, cols);
	}
	
	@Override
	public IMatrix set(int row, int col, double value) {
		return original.set(rowIndexes[row], colIndexes[col], value);
	}
}
