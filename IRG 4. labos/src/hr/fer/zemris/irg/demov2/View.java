package hr.fer.zemris.irg.demov2;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Class used for calculating transformation and projection matrices
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class View {

	public RealVector ociste;
	public RealVector glediste;
	public RealVector izvor;

	public View(RealVector ociste, RealVector glediste, RealVector izvor) {
		this.ociste = ociste;
		this.glediste = glediste;
		this.izvor = izvor;
	}

	/*
	 * Transformacija pogleda
	 */
	public RealVector calculateT(RealVector point) {

		// rotacija u xy-ravnini zbog poklapanja z-osi
		final RealMatrix m1 = T1();
		final RealMatrix m2 = m1.multiply(T2());

		// rotacija u xz ravnini radi poklapanja z-osi
		final RealMatrix m3 = m2.multiply(T3());

		// zakretanje za 90°, poklapanje y-osi
		final RealMatrix m4 = m3.multiply(T4());

		// okretanje x-osi
		final RealMatrix m5 = m4.multiply(T5());

		final RealMatrix pointM = new Array2DRowRealMatrix(point.toArray())
				.transpose();
		return pointM.multiply(m5).getRowVector(0);
	}

	public RealVector calculateP(RealVector point) {
		final double xo = ociste.getEntry(0);
		final double xg = glediste.getEntry(0);
		final double yo = ociste.getEntry(1);
		final double yg = glediste.getEntry(1);
		final double zo = ociste.getEntry(2);
		final double zg = glediste.getEntry(2);
		final double H = Math.sqrt(Math.pow(xo - xg, 2) + Math.pow(yo - yg, 2)
				+ Math.pow(zo - zg, 2));
		final RealMatrix pm = new Array2DRowRealMatrix(point.toArray())
				.transpose();
		final RealMatrix perspectiveMatrix = new Array2DRowRealMatrix(
				new double[][] { { 1, 0, 0, 0 }, { 0, 1, 0, 0 },
						{ 0, 0, 0, 1. / H }, { 0, 0, 0, 0 } });
		final RealMatrix result = pm.multiply(perspectiveMatrix);
		result.setEntry(0, 0, result.getEntry(0, 0) / point.getEntry(2));
		result.setEntry(0, 1, result.getEntry(0, 1) / point.getEntry(2));
		result.setEntry(0, 3, result.getEntry(0, 3) / result.getEntry(0, 3));
		return result.getRowVector(0);
	}

	private RealMatrix T1() {
		final double xo = ociste.getEntry(0);
		final double yo = ociste.getEntry(1);
		final double zo = ociste.getEntry(2);
		return new Array2DRowRealMatrix(new double[][] { { 1, 0, 0, 0 },
				{ 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { -xo, -yo, -zo, 1 } });
	}

	private RealMatrix T2() {
		final double xg1 = glediste.getEntry(0) - ociste.getEntry(0);
		final double yg1 = glediste.getEntry(1) - ociste.getEntry(1);

		final double cosA = xg1
				/ Math.sqrt(Math.pow(xg1, 2) + Math.pow(yg1, 2));
		final double sinA = yg1
				/ Math.sqrt(Math.pow(xg1, 2) + Math.pow(yg1, 2));
		return new Array2DRowRealMatrix(new double[][] { { cosA, -sinA, 0, 0 },
				{ sinA, cosA, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } });
	}

	private RealMatrix T3() {
		final double xg1 = glediste.getEntry(0) - ociste.getEntry(0);
		final double yg1 = glediste.getEntry(1) - ociste.getEntry(1);
		final double zg1 = glediste.getEntry(2) - ociste.getEntry(2);
		final double xg2 = Math.sqrt(Math.pow(xg1, 2) + Math.pow(yg1, 2));
		final double zg2 = zg1;
		final double sinB = xg2
				/ Math.sqrt(Math.pow(xg2, 2) + Math.pow(zg2, 2));
		final double cosB = zg2
				/ Math.sqrt(Math.pow(xg2, 2) + Math.pow(zg2, 2));
		return new Array2DRowRealMatrix(new double[][] { { cosB, 0, sinB, 0 },
				{ 0, 1, 0, 0 }, { -sinB, 0, cosB, 0 }, { 0, 0, 0, 1 } });
	}

	private RealMatrix T4() {
		return new Array2DRowRealMatrix(new double[][] { { 0, -1, 0, 0 },
				{ 1, 0, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } });
	}

	private RealMatrix T5() {
		return new Array2DRowRealMatrix(new double[][] { { -1, 0, 0, 0 },
				{ 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } });
	}

}
