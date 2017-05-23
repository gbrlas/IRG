package hr.fer.zemris.irg.demo;

import hr.fer.zemris.irg.linearna.Matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class which calculates the barycentric coordinates of a point in relation to
 * the provided triangle and tells the user whether the point is inside the
 * triangle or outside the triangle. The point and triangle coordinates are
 * provided by the user through the console when prompted.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class BarycentricCoordinates {

	/**
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.print("Unesite koordinate vrha A: \n");
		final String astr = br.readLine();
		final String[] atemp = astr.split(" ");
		System.out.print("Unesite koordinate vrha B: \n");
		final String bstr = br.readLine();
		final String[] btemp = bstr.split(" ");
		System.out.print("Unesite koordinate vrha C: \n");
		final String cstr = br.readLine();
		final String[] ctemp = cstr.split(" ");
		System.out.print("Unesite koordinate točke T: \n");
		final String tstr = br.readLine();
		final String[] ttemp = tstr.split(" ");

		final Matrix a1 = Matrix.parseSimple(atemp[0] + " " + btemp[0] + " "
				+ ctemp[0] + " | " + " " + atemp[1] + " " + btemp[1] + " "
				+ ctemp[1] + "|" + " " + atemp[2] + " " + btemp[2] + " "
				+ ctemp[2]);

		final Matrix t = Matrix.parseSimple(ttemp[0] + " | " + ttemp[1] + " | "
				+ ttemp[2]);

		final Matrix result = (Matrix) a1.nInvert().nMultiply(t);

		System.out
				.println("\n[x y z] = " + result.nTranspose(false).toString());

		boolean test = true;
		for (int i = 0; i < 3; i++) {
			if (result.get(i, 0) > 1 || result.get(i, 0) < 0) {
				test = false;
				break;
			}
		}

		System.out.println(test ? "Točka pripada trokutu."
				: "Točka ne pripada trokutu.");
	}

}
