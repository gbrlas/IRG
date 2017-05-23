package hr.fer.zemris.irg.linearna;

/**
 * Apstraktna klasa koja predstavlja matricu realnih brojeva. Matrice
 * predstavljene ovom klasom su matrice realnih brojeva i mogu biti proizvoljnih
 * dimenzija. Implementira osnovne metode za dobivanje informacija o matrici te
 * za provođenje čestih operacija nad matricama. Implementira
 * {@linkplain IMatrix}.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public abstract class AbstractMatrix implements IMatrix {

	@Override
	public abstract int getRowsCount();

	@Override
	public abstract int getColsCount();

	@Override
	public abstract double get(int row, int col);

	@Override
	public abstract IMatrix set(int row, int col, double value);

	@Override
	public abstract IMatrix copy();

	@Override
	public abstract IMatrix newInstance(int rows, int cols);

	@Override
	public IMatrix nTranspose(boolean liveView) {
		if (liveView) {
			return new MatrixTransposeView(this);
		} else {
			final double[][] array = new double[this.getColsCount()][this
			                                                         .getRowsCount()];

			for (int i = 0; i < this.getRowsCount(); i++) {
				for (int j = 0; j < this.getColsCount(); j++) {
					array[j][i] = this.get(i, j);
				}
			}

			return new Matrix(this.getColsCount(), this.getRowsCount(), array,
					false);
		}

	}

	@Override
	public IMatrix add(IMatrix other) {
		if (this.getRowsCount() != other.getRowsCount()
				|| this.getColsCount() != other.getColsCount()) {
			throw new IncompatibleOperandException(
					"Matrices are not compatible!");
		}

		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getColsCount(); j++) {
				this.set(i, j, this.get(i, j) + other.get(i, j));
			}
		}

		return this;
	}

	@Override
	public IMatrix nAdd(IMatrix other) {
		return this.copy().add(other);
	}

	@Override
	public IMatrix sub(IMatrix other) {
		if (this.getRowsCount() != other.getRowsCount()
				|| this.getColsCount() != other.getColsCount()) {
			throw new IncompatibleOperandException(
					"Matrices are not compatible for addition!");
		}

		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getColsCount(); j++) {
				this.set(i, j, this.get(i, j) - other.get(i, j));
			}
		}

		return this;
	}

	@Override
	public IMatrix nSub(IMatrix other) {
		return this.copy().sub(other);
	}

	@Override
	public IMatrix nMultiply(IMatrix other) {
		if (this.getColsCount() != other.getRowsCount()) {
			throw new IncompatibleOperandException(
					"Matrices are not compatible for multiplication!");
		}

		final IMatrix result = newInstance(this.getRowsCount(),
				other.getColsCount());

		double sum = 0;
		for (int i = 0; i < this.getRowsCount(); i++) { // a row
			for (int j = 0; j < other.getColsCount(); j++) { // b column
				for (int k = 0; k < other.getRowsCount(); k++) { // a column
					sum += this.get(i, k) * other.get(k, j);
				}
				result.set(i, j, sum);
				sum = 0;
			}
		}

		return result;
	}

	@Override
	public double determinant() throws IncompatibleOperandException {
		if (this.getRowsCount() != this.getColsCount()) {
			throw new IncompatibleOperandException(
					"In order to calculate determinant matrix must be square.");
		}

		final double[][] temp = this.toArray();

		final double det = determinantHelper(temp, this.getRowsCount());

		return det;
	}

	/**
	 * Pomocna metoda koja sluzi racunanju determinante matrice (rekurzivno).
	 *
	 * @param temp
	 *            Polje matrice.
	 * @param N
	 *            Veličina kvadratne matrice
	 * @return Determinanta predane matrice.
	 */
	private double determinantHelper(double[][] temp, int N) {
		double det = 0;

		if (N == 1) {
			det = temp[0][0];
		} else if (this.getRowsCount() == 2) {
			det = temp[0][0] * temp[1][1] - temp[1][0] * temp[0][1];
		} else {
			det = 0;
			for (int j1 = 0; j1 < N; j1++) {
				final double[][] temp2 = new double[N - 1][];
				for (int k = 0; k < (N - 1); k++) {
					temp2[k] = new double[N - 1];
				}
				for (int i = 1; i < N; i++) {
					int j2 = 0;
					for (int j = 0; j < N; j++) {
						if (j == j1)
							continue;
						temp2[i - 1][j2] = temp[i][j];
						j2++;
					}
				}
				det += Math.pow(-1.0, 1.0 + j1 + 1.0) * temp[0][j1]
						* determinantHelper(temp2, N - 1);
			}
		}
		return det;
	}

	@Override
	public IMatrix subMatrix(int row, int col, boolean liveView) {
		if (liveView) {
			final int[] rows = new int[this.getRowsCount() - 1];
			final int[] cols = new int[this.getColsCount() - 1];

			int j = 0;
			for (int i = 0; i < this.getRowsCount(); i++) {
				if (i == row) {
					continue;
				} else {
					rows[j++] = i;
				}
			}

			j = 0;
			for (int i = 0; i < this.getColsCount(); i++) {
				if (i == col) {
					continue;
				} else {
					cols[j++] = i;
				}
			}

			return new MatrixSubMatrixView(this, rows, cols);
		}

		else {
			final double[][] array = new double[this.getRowsCount() - 1][this
			                                                             .getColsCount() - 1];

			int k = 0;
			int h = 0;
			for (int i = 0; i < this.getRowsCount(); i++) {
				for (int j = 0; j < this.getRowsCount(); j++) {
					if (i == row || j == col) {
						continue;
					} else {
						array[k][h++] = this.get(i, j);
					}
				}
				if (i != row) {
					k++;
				}
				h = 0;
			}

			return new Matrix(this.getRowsCount() - 1, this.getColsCount() - 1,
					array, false);
		}
	}

	/**
	 * Inverz se racuna pomoću Cramerovog pravila, a algoritam je opisan na
	 * sljedecoj stranici:
	 * http://en.wikipedia.org/wiki/Minor_(linear_algebra)#Matrix_of_cofactors.
	 */
	@Override
	public IMatrix nInvert() {

		final double det = this.determinant();
		final double[][] array = new double[this.getRowsCount()][this
		                                                         .getColsCount()];

		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getColsCount(); j++) {
				array[i][j] = subMatrix(i, j, false).determinant()
						* Math.pow(-1, 2 + i + j);
			}
		}

		Matrix temp = new Matrix(this.getRowsCount(), this.getColsCount(),
				array, false);
		temp = (Matrix) temp.nTranspose(false);

		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getColsCount(); j++) {
				temp.set(i, j, temp.get(i, j) / det);
			}
		}

		return temp;

	}

	@Override
	public double[][] toArray() {
		final double[][] array = new double[this.getRowsCount()][this
		                                                         .getColsCount()];

		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getColsCount(); j++) {
				array[i][j] = this.get(i, j);
			}
		}

		return array;
	}

	@Override
	public IVector toVector(boolean liveView) {
		if (this.getColsCount() != 1 && this.getRowsCount() != 1) {
			throw new IncompatibleOperandException();
		}

		if (liveView) {
			return new VectorMatrixView(this);
		} else {
			double[] array;

			if (this.getColsCount() == 1) {
				array = new double[this.getRowsCount()];

				for (int i = 0; i < this.getRowsCount(); i++) {
					array[i] = this.get(i, 0);
				}
			} else {
				array = new double[this.getColsCount()];

				for (int i = 0; i < this.getColsCount(); i++) {
					array[i] = this.get(0, i);
				}
			}

			return new Vector(array);
		}
	}

	@Override
	public String toString() {
		return toString(3);
	}

	/**
	 * Stvara string koji predstavlja ovu matricu, sa zadanim decimalnim
	 * formatom.
	 *
	 * @param decimals
	 *            Broj znamenaka iza decimalne tocke.
	 * @return String koji predstavlja ovu matricu.
	 */
	public String toString(int decimals) {
		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < this.getRowsCount(); i++) {
			builder.append("[");
			for (int j = 0; j < this.getColsCount(); j++) {
				builder.append(this.get(i, j) + ", ");
			}
			try {
				builder.delete(builder.length() - 2, builder.length());
			} catch (final Exception exception) {
			}
			;
			builder.append("]");
			builder.append("\n");

		}

		return builder.toString();
	}

	@Override
	public IMatrix scalarMultiply(double value) {

		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getColsCount(); j++) {
				this.set(i, j, this.get(i, j) * value);
			}
		}

		return this;
	}

	@Override
	public IMatrix nScalarMultiply(double value) {
		return this.copy().scalarMultiply(value);
	}

	@Override
	public IMatrix makeIdentity() {

		if (this.getRowsCount() != this.getColsCount()) {
			throw new IncompatibleOperandException(
					"Matrix is not square, cannot make identity.");
		}

		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getColsCount(); j++) {
				if (i == j) {
					this.set(i, j, 1);
				} else {
					this.set(i, j, 0);
				}
			}
		}

		return this;
	}

}