package math;

/**
 * Vector2D class is used to set position of objects in the game
 */
public class Vector2D {
	/**
	 * x and y are the coordinates of the object
	 */
	private double x, y;
	/**
	 * Constructor
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds two vectors
	 * @param v the vector to be added
	 * @return the sum of the two vectors
	 */
	public Vector2D add(Vector2D v) {
		return new Vector2D(x + v.getX(), y + v.getY());
	}

	/**
	 * Calculates the Euclidean distance between two 2D vectors.
	 *
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The Euclidean distance between the two vectors.
	 */
	public double distance(Vector2D v1, Vector2D v2) {
		return Math.sqrt(Math.pow(v2.x - v1.x, 2) + Math.pow(v2.y - v1.y, 2));
	}

	public double getX() { return x; }

	public void setX(double x) { this.x = x; }

	public double getY() { return y; }

	public void setY(double y) { this.y = y; }

	@Override
	public String toString() {
		return "Vector2D{" +
				"x=" + x +
				", y=" + y +
				'}' + this.getClass();
	}
}
