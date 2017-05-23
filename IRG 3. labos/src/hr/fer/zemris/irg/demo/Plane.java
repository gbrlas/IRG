package hr.fer.zemris.irg.demo;

/**
 * Class representing a single plane (in analytical form).
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class Plane {
	/**
	 * Plane A value.
	 */
	private double A;
	/**
	 * Plane B value.
	 */
	private double B;
	/**
	 * Plane C value.
	 */
	private double C;
	/**
	 * Plane D value.
	 */
	private double D;

	/**
	 * Constructor whihc sets the plane values to the provided ones.
	 *
	 * @param a
	 *            Plane A value.
	 * @param b
	 *            Plane B value.
	 * @param c
	 *            Plane C value.
	 * @param d
	 *            Plane D value.
	 */
	public Plane(double a, double b, double c, double d) {
		super();
		A = a;
		B = b;
		C = c;
		D = d;
	}

	/**
	 * Getter method which returns the plane A value.
	 *
	 * @return Plane A value.
	 */
	public double getA() {
		return A;
	}

	/**
	 * Setter method which sets the plane A value.
	 *
	 * @param a
	 *            Plane A value.
	 */
	public void setA(double a) {
		A = a;
	}

	/**
	 * Getter method which returns the plane B value.
	 *
	 * @return Plane B value.
	 */
	public double getB() {
		return B;
	}

	/**
	 * Setter method which sets the plane B value.
	 *
	 * @param a
	 *            Plane B value.
	 */
	public void setB(double b) {
		B = b;
	}

	/**
	 * Getter method which returns the plane C value.
	 *
	 * @return Plane C value.
	 */
	public double getC() {
		return C;
	}

	/**
	 * Setter method which sets the plane C value.
	 *
	 * @param a
	 *            Plane C value.
	 */
	public void setC(double c) {
		C = c;
	}

	/**
	 * Getter method which returns the plane D value.
	 *
	 * @return Plane D value.
	 */
	public double getD() {
		return D;
	}

	/**
	 * Setter method which sets the plane D value.
	 *
	 * @param a
	 *            Plane D value.
	 */
	public void setD(double d) {
		D = d;
	}

}
