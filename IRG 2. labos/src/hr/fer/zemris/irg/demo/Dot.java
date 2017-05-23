package hr.fer.zemris.irg.demo;

/**
 * Class which represents a single dot. Contains dot x, y and z coordinate
 * value.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class Dot {
	/**
	 * Dot x coordinate value.
	 */
	private int x;
	/**
	 * Dot y value.
	 */
	private int y;

	/**
	 * Dot z value.
	 */
	private int z;

	/**
	 * Constructor which sets the dot x and y values to the provided ones.
	 *
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 */
	public Dot(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Getter method which returns the dot x coordinate value.
	 *
	 * @return x-coordinate value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter method which returns the dot y coordinate value.
	 *
	 * @return y-coordinate value
	 */
	public int getY() {
		return y;
	}

	/**
	 * Getter method which returns the dot z coordinate value.
	 *
	 * @return z-coordinate value
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Copies the current dot values and returns a new dot.
	 *
	 * @return new dot with same values
	 */
	public Dot copy() {
		return new Dot(x, y, z);
	}

	/**
	 * Setter method which sets the x coordinate value to the provided one.
	 *
	 * @param x
	 *            x-coordinate value
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Setter method which sets the y coordinate value to the provided one.
	 *
	 * @param y
	 *            y-coordinate value
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Setter method which sets the z coordinate value to the provided one.
	 *
	 * @param z
	 *            z-coordinate value
	 */
	public void setZ(int z) {
		this.z = z;
	}

}