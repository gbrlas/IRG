package hr.fer.zemris.irg.linearna;

public class VectorMatrixView extends AbstractVector {
	private IMatrix original;
	private int dimension;
	private boolean rowMatrix;
	
	
	public VectorMatrixView(IMatrix original) {
		this.original = original;
		
		if (original.getRowsCount() == 1) {
			rowMatrix = true;
			dimension = original.getColsCount();
		} else {
			rowMatrix = false;
			dimension = original.getRowsCount();
		}
	}
	
	@Override
	public int getDimension() {
		return dimension;
	}
	
	@Override
	public double get(int index) {
		if (index < 0 || index >= dimension) {
			throw new IllegalArgumentException("Invalid index, try again.");
		}
		
		if (rowMatrix) {
			return original.get(0, index);
		} else {
			return original.get(index, 0);
		}
	}
	
	@Override
	public IVector set(int index, double value) {
		if (index < 0 || index >= dimension) {
			throw new IllegalArgumentException("Invalid index, try again.");
		}
		
		if (rowMatrix) {
			original.set(0, index, value);
		} else {
			original.set(index, 0, value);
		}
		
		return this;
	}
	
	@Override
	public IVector copy() {
		IVector vector = newInstance(dimension);
		
		if (rowMatrix) {
			for (int i = 0; i < dimension; i++) {
				vector.set(i, original.get(0, i));
			}
		} else {
			for (int i = 0; i < dimension; i++) {
				vector.set(i, original.get(i, 0));
			}
		}
		
		return vector;
	}
	
	@Override
	public IVector newInstance(int dimension) {
		return new Vector(new double[dimension]);
	}
	
	
}
