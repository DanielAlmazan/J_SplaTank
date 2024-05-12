package sprites;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import math.Vector2D;
import states.Match;

/**
 * MovingSprite is an abstract class that extends Sprite and adds the ability to move.
 * It also adds the ability to rotate the sprite and check for collisions.
 */
public abstract class MovingSprite extends Sprite {
	/**
	 * The velocity of the sprite.
	 */
	public double velocity;
	/**
	 * Used for rotating the sprite.
	 */
	protected AffineTransform at;
	/**
	 * The rotation of the sprite to apply to the AffineTransform.
	 */
	protected double rotation;
	/**
	 * The acceleration of the sprite.
	 */
	protected double acceleration;
	/**
	 * The maximum velocity of the sprite.
	 */
	protected final double STD_VEL = 2.5;
	protected final int BOOST_VEL = 4;
	protected  double maxVel;
	/**
	 * Whether the sprite is moving forward or not.
	 */
	protected boolean isMovingForward = true;

	/**
	 * MovingSprite constructor.
	 * @param position the position of the sprite
	 */
	public MovingSprite(Vector2D position) {
		super(position);

		this.acceleration = 0.5;
		this.maxVel = 5;
	}

	/**
	 * getFrontPosition returns the position of the front of the sprite.
	 * @return the position of the front of the sprite
	 */
	public Vector2D getFrontPosition() {
		return getFrontPosition(this.rotation);
	}

	/**
	 * getFrontPosition returns the position of the front of the sprite.
	 * @param angle the angle to rotate the sprite by
	 * @return the position of the front of the sprite
	 */
	public Vector2D getFrontPosition(double angle) {
		AffineTransform rotatedPos = AffineTransform.getTranslateInstance(
				this.position.getX() + (double)this.texture.getWidth() / 2,
				this.position.getY() + (double)this.texture.getHeight() / 2
		);

		Vector2D rotatedVector = new Vector2D(
				rotatedPos.getTranslateX(), rotatedPos.getTranslateY()
		);

		double x = ((double)this.texture.getWidth() / 2) * Math.cos(angle);
		double y = ((double)this.texture.getHeight() / 2) * Math.sin(angle);

		rotatedVector = (new Vector2D(
				rotatedVector.getX() + x, rotatedVector.getY() + y));

		return rotatedVector;
	}

	/**
	 * getFrontBounds returns the bounds of the front of the sprite.
	 * @return the bounds of the front of the sprite
	 */
	public Rectangle2D getFrontBounds() {
		// We assign the rotation
		double newRotation = this.rotation;

		// If it's moving backwards, we invert the front bounds
		if (!isMovingForward) {
			newRotation = this.rotation + Math.toRadians(180);
		}

		Vector2D rotatedVector = this.getFrontPosition(newRotation);

		return new Rectangle2D.Double(
				rotatedVector.getX(), rotatedVector.getY(), 10, 10);
	}

	/**
	 * isColliding checks if the sprite is colliding with any other sprites
	 * from the 'Match.colliders' static list.
	 * @return true at the first collision, false otherwise
	 */
	public boolean isColliding() {
		// We get the front bound of the tank
		Rectangle2D frontBound = this.getFrontBounds();

		// Then, we check every object of the colliders list from Match class
		// We also check that it IS colliding with the playing area (if it stops
		// colliding, it means it's getting out of it, so we must stop the tank
		for (Sprite g : Match.colliders) {
			if (frontBound.intersects(g.hitBox) && this != g ||
					!frontBound.intersects(Match.playingArea.hitBox)) {
				isMovingForward = !isMovingForward;
				return true;
			}
		}
		return false;
	}

	/**
	 * accelerate increases the velocity of the sprite, up to the maximum velocity.
	 */
	public void accelerate() {
		if (this.velocity < this.maxVel) {
			this.velocity += this.acceleration * 0.35f;
		}
		if (this.velocity > this.maxVel) {
			this.velocity = this.maxVel;
		}
	}

	/**
	 * decelerate decreases the velocity of the sprite, down to 0.
	 */
	public void decelerate() { decelerate(0.5f);}

	public void decelerate(float multiplier) {
		if (this.velocity > 0) {
			this.velocity -= this.acceleration * multiplier;
		}
		if (this.velocity < 0) {
			this.velocity = 0;
		}
	}

	/**
	 * move method moves the sprite forward.
	 * @return the new position of the sprite
	 */
	public Vector2D move() {
		int direction = isMovingForward ? 1 : -1;

		double x = this.position.getX() + direction * Math.cos(this.rotation) * this.velocity;
		double y = this.position.getY() + direction * Math.sin(this.rotation) * this.velocity;

		this.hitBox = updateHitBox();

		return new Vector2D(x, y);
	}
}
