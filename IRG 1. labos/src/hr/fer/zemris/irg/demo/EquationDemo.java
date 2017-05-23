package hr.fer.zemris.irg.demo;

import hr.fer.zemris.irg.linearna.Matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class which calculates the solution of the 3 equations, 3 solutions system.
 * User is prompted to enter the equation in the following manner: (a1 a2 a3 ,
 * r1 , b1 b2 b3 , r2 , c1 c2 c3 , r3).
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class EquationDemo {

	/**
	 * Main method which calculates the equation solution.
	 *
	 * @param args
	 *            -none
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));
		System.out
				.print("Unesite jednadžbe u obliku (a1 a2 a3 , r1 , b1 b2 b3 , r2 , c1 c2 c3 , r3):\n");
		final String str = br.readLine();

		final String[] polje = str.split(",");

		final Matrix a1 = Matrix.parseSimple(polje[0] + " | " + polje[2] + "|"
				+ polje[4]);
		// System.out.println("a1 =\n" + a1.toString());
		final Matrix r1 = Matrix.parseSimple(polje[1].trim() + " | "
				+ polje[3].trim() + " | " + polje[5].trim());
		// System.out.println("r1 =\n" + r1.toString());

		final Matrix result = (Matrix) a1.nInvert().nMultiply(r1);

		System.out
				.println("\n[x y z] = " + result.nTranspose(false).toString());
	}
}
