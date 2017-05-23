package hr.fer.zemris.irg.linearna;


/**
 * "Zivi" pogled na matricu u kojoj se matrica gleda transponirano, 
 * odnosno retke zamijenjujemo stupcima i obrnuto.
 * Nasljedjuje {@linkplain AbstractMatrix}.
 * 
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class MatrixTransposeView extends AbstractMatrix {
	/**
	 * Referenca na originalnu matricu.
	 */
	private IMatrix original;
	
	/**
	 * Konstruktor koji stvara novi transponirani pogled na matricu koristeci predanu
	 * referencu.
	 * 
	 * @param matrix Originalna matrica na koju stvaramo pogled.
	 * @throws IllegalArgumentException - ako je predana matrica null.
	 */
	public MatrixTransposeView(IMatrix matrix) throws IllegalArgumentException {
		if (matrix == null) {
			throw new IllegalArgumentException("Invalid argument passed to the MatrixTransposeView constructor.");
		}
		original = matrix;
	}
	
	
	@Override
	public int getRowsCount() {
		return original.getColsCount();
	}
	
	@Override
	public int getColsCount() {
		return original.getRowsCount();
	}
	
	@Override
	public double get(int row, int col) {
		return original.get(col, row);
	}
	
	@Override
	public IMatrix copy() {
		IMatrix original = newInstance(this.getRowsCount(), this.getColsCount());
		
		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getColsCount(); j++) {
				original.set(i, j, original.get(this.getRowsCount(), this.getColsCount()));
			}
		}
		
		return original;
	}
	
	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new Matrix(rows, cols);
	}
	
	@Override
	public IMatrix set(int row, int col, double value) {
		return original.set(col, row, value);
	}
}
