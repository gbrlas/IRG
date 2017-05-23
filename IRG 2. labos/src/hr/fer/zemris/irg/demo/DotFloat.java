package hr.fer.zemris.irg.demo;

/**
 * Class which represents a single dot. Contains dot x, y and z coordinate value
 * (float).
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class DotFloat {
	/**
	 * Dot x coordinate value.
	 */
	private double x;
	/**
	 * Dot y coordinate value.
	 */
	private double y;
	/**
	 * Dot z coordinate value.
	 */
	private double z;

	/**
	 * Constructor which sets the dot x, y and z values to the provided ones.
	 *
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @param z
	 *            z-coordinate
	 */
	public DotFloat(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Getter method which returns the dot x coordinate value.
	 *
	 * @return x-coordinate value
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setter method which sets the x coordinate value to the provided one.
	 *
	 * @param x
	 *            x-coordinate value
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Getter method which returns the dot y coordinate value.
	 *
	 * @return y-coordinate value
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter method which sets the y coordinate value to the provided one.
	 *
	 * @param y
	 *            y-coordinate value
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Getter method which returns the dot z coordinate value.
	 *
	 * @return z-coordinate value
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Setter method which sets the z coordinate value to the provided one.
	 *
	 * @param z
	 *            z-coordinate value
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Copies the current dot values and returns a new dot.
	 *
	 * @return new dot with same values
	 */
	public DotFloat copy() {
		return new DotFloat(x, y, z);
	}

}
