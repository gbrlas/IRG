package hr.fer.zemris.irg.linearna;

/**
 * Apstraktna klasa koja predstavlja vektor realnih brojeva. Vektori
 * predstavljeni ovom klasom su vektori realnih brojeva i mogu biti proizvoljnih
 * dimenzija. Implementira osnovne metode za dobivanje informacija o vektoru te
 * za provođenje čestih operacija nad vektorima. Implementira
 * {@linkplain IVector}.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public abstract class AbstractVector implements IVector {

	@Override
	public abstract double get(int index);

	@Override
	public abstract IVector set(int index, double value);

	@Override
	public abstract int getDimension();

	@Override
	public abstract IVector copy();

	@Override
	public abstract IVector newInstance(int dimension);

	@Override
	public IVector add(IVector other) throws IncompatibleOperandException {
		if (this.getDimension() != other.getDimension()) {
			throw new IncompatibleOperandException();
		}

		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) + other.get(i));
		}

		return this;
	}

	@Override
	public IVector nAdd(IVector other) throws IncompatibleOperandException {
		return this.copy().add(other);
	}

	@Override
	public IVector sub(IVector other) throws IncompatibleOperandException {
		if (this.getDimension() != other.getDimension()) {
			throw new IncompatibleOperandException();
		}

		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) - other.get(i));
		}

		return this;
	}

	@Override
	public IVector nSub(IVector other) throws IncompatibleOperandException {
		return this.copy().sub(other);
	}

	@Override
	public IVector scalarMultiply(double byValue) {
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) * byValue);
		}

		return this;
	}

	@Override
	public IVector nScalarMultiply(double byValue) {
		return this.copy().scalarMultiply(byValue);
	}

	@Override
	public double norm() {
		double sum = 0;

		for (int i = this.getDimension() - 1; i >= 0; i--) {
			sum += Math.pow(this.get(i), 2);
		}

		return Math.sqrt(sum);
	}

	@Override
	public IVector normalize() {
		final double norm = this.norm();
		if (norm == 0) {
			return this;
		} else {
			for (int i = this.getDimension() - 1; i >= 0; i--) {
				this.set(i, this.get(i) / norm);
			}
		}

		return this;
	}

	@Override
	public IVector nNormalize() {
		return this.copy().normalize();
	}

	@Override
	public double cosine(IVector other) throws IncompatibleOperandException {
		if (this.getDimension() != other.getDimension()) {
			throw new IncompatibleOperandException();
		}

		double sum = 0;
		final double norm1 = this.norm();
		final double norm2 = other.norm();

		for (int i = this.getDimension() - 1; i >= 0; i--) {
			sum += this.get(i) * other.get(i);
		}

		return sum / (norm1 * norm2);
	}

	public IVector reverse() {
		return Vector.parseSimple("0 0 0").sub(this);
	}

	@Override
	public double scalarProduct(IVector other)
			throws IncompatibleOperandException {
		if (this.getDimension() != other.getDimension()) {
			throw new IncompatibleOperandException();
		}

		return this.norm() * other.norm() * this.cosine(other);
	}

	@Override
	public IVector nVectorProduct(IVector other)
			throws IncompatibleOperandException {
		if (this.getDimension() != 3 || other.getDimension() != 3) {
			throw new IncompatibleOperandException();
		}

		final IVector result = newInstance(3);
		final double i = this.get(1) * other.get(2) - this.get(2)
				* other.get(1);
		result.set(0, i);
		final double j = this.get(2) * other.get(0) - this.get(0)
				* other.get(2);
		result.set(1, j);
		final double k = this.get(0) * other.get(1) - this.get(1)
				* other.get(0);
		result.set(2, k);

		return result;
	}

	@Override
	public IVector nFromHomogeneus() {
		final IVector result = newInstance(this.getDimension() - 1);

		for (int i = this.getDimension() - 2; i >= 0; i--) {
			result.set(i, this.get(i) / this.get(getDimension() - 1));
		}

		return result;
	}

	@Override
	public double[] toArray() {
		final double[] array = new double[this.getDimension()];

		for (int i = 0; i < this.getDimension(); i++) {
			array[i] = this.get(i);
		}

		return array;
	}

	@Override
	public IVector copyPart(int n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"Invalid size provided to the copy method.");
		}
		final IVector result = newInstance(n);

		for (int i = 0; i < n; i++) {
			if (i < this.getDimension()) {
				result.set(i, this.get(i));
			} else {
				result.set(i, 0);
			}
		}

		return result;
	}

	@Override
	public IMatrix toColumnMatrix(boolean liveView) {

		if (liveView) {
			return new MatrixVectorView(this, false);
		}

		final double[][] array = new double[this.getDimension()][1];

		for (int i = 0; i < this.getDimension(); i++) {
			array[i][0] = this.get(i);
		}

		return new Matrix(this.getDimension(), 1, array, false);
	}

	@Override
	public IMatrix toRowMatrix(boolean liveView) {

		if (liveView) {
			return new MatrixVectorView(this, true);
		}

		final double[][] array = new double[1][this.getDimension()];

		for (int j = 0; j < this.getDimension(); j++) {
			array[0][j] = this.get(j);
		}

		return new Matrix(1, this.getDimension(), array, false);
	}

	@Override
	public String toString() {
		return toString(3);
	}

	/**
	 * Stvara string koji predstavlja ovaj vektor, sa zadanim decimalnim
	 * formatom.
	 *
	 * @param decimals
	 *            Broj znamenaka iza decimalne tocke.
	 * @return String koji predstavlja ovaj vektor.
	 */
	public String toString(int decimals) {
		final StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < this.getDimension(); i++) {
			builder.append(String.format("%.3f, ", this.get(i)));
		}

		builder.delete(builder.length() - 2, builder.length());
		builder.append("]");

		return builder.toString();
	}

}
