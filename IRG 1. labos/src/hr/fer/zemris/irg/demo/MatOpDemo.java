package hr.fer.zemris.irg.demo;

import hr.fer.zemris.irg.linearna.Matrix;

/**
 * Class which calculates certain mathematical operations with vectors, such as
 * simple vector addition, scalar multiplication, vector product, vector
 * normalizing, vector reversing. It also calculates operations with matrices,
 * such as addition, matrix transposing, matrix inverting and matrices
 * multiplication.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class MatOpDemo {

	/**
	 * Main method which calculates the mathematical operations.
	 *
	 * @param args
	 *            -none
	 */
	public static void main(String[] args) {
		// Vector v1 = (Vector) LinAlgDefaults.defaultVector(3);
		//
		// v1 = (Vector) Vector.parseSimple("2 3 -4").add(
		// Vector.parseSimple("-1 4 -1"));
		//
		// System.out.println("v1 = (2i + 3j - 4k) + (-i + 4j - k)");
		// System.out.println("v1 = " + v1.toString() + "\n");
		//
		// final double s = v1.scalarProduct(Vector.parseSimple("-1 4 -1"));
		//
		// System.out.println("s = v1 * (-i + 4j -k)");
		// System.out.println("s = " + (int) s + "\n");
		//
		// final Vector v2 = (Vector) v1.nVectorProduct(Vector
		// .parseSimple("2 2 4"));
		//
		// System.out.println("v2 = v1 x (2i + 2j + 4k)");
		// System.out.println("v2 = " + v2.toString() + "\n");
		//
		// final Vector v3 = (Vector) v2.normalize();
		// System.out.println("v3 = |v2|");
		// System.out.println("v3 = " + v3.toString() + "\n");
		//
		// final Vector v4 = (Vector) v2.reverse();
		// System.out.println("v4 = -v2");
		// System.out.println("v4 = " + v4.toString() + "\n");
		//
		// final Matrix m1 = (Matrix)
		// Matrix.parseSimple("1 2 3 | 2 1 3 | 4 5 1")
		// .add(Matrix.parseSimple("-1 2 -3 | 5 -2 7 | -4 -1 3"));
		// System.out
		// .println("m1 = [1 2 3 | 2 1 3 | 4 5 1] + [-1 2 -3 | 5 -2 7 | -4 -1 3]");
		// System.out.println("m1 = " + "\n" + m1.toString());

		// final Matrix m2 = (Matrix)
		// Matrix.parseSimple("1 2 3 | 2 1 3 | 4 5 1")
		// .nMultiply(
		// Matrix.parseSimple("-1 2 -3 | 5 -2 7 | -4 -1 3")
		// .nTranspose(false));
		// System.out
		// .println("m2 = [1 2 3 | 2 1 3 | 4 5 1] * [-1 2 -3 | 5 -2 7 | -4 -1 3]T");
		// System.out.println("m2 = " + "\n" + m2.toString());
		//
		// final Matrix m3 = (Matrix)
		// Matrix.parseSimple("1 2 3 | 2 1 3 | 4 5 1")
		// .nMultiply(
		// Matrix.parseSimple("-1 2 -3 | 5 -2 7 | -4 -1 3")
		// .nInvert());
		// System.out
		// .println("m3 = [1 2 3 | 2 1 3 | 4 5 1] * [-1 2 -3 | 5 -2 7 | -4 -1 3]-1");
		// System.out.println("m3 = " + "\n" + m3.toString());

		final Matrix matrix = (Matrix) Matrix.parseSimple(
				"1 2 3 | 4 5 6| 2 1 2").nMultiply(
						Matrix.parseSimple("1 2 2 | 2 1 3 | 5 1 0"));
		System.out.println("matrix = " + "\n" + matrix.toString());

	}
}
