package sprites;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import math.Vector2D;

/**
 * Sprite is an abstract class that represents a sprite.
 */
public abstract class Sprite {
	/**
	 * The position of the sprite.
	 */
	public Vector2D position;
	/**
	 * The hitbox of the sprite.
	 */
	public Rectangle2D hitBox;
	/**
	 * The texture of the sprite.
	 */
	protected BufferedImage texture;
	/**
	 * The width of the sprite.
	 */
	protected int width;
	/**
	 * The height of the sprite.
	 */
	protected int height;

	/**
	 * Sprite constructor.
	 * @param position the position of the sprite
	 */
	public Sprite(Vector2D position)
	{
		this.position = position;
		this.hitBox = new Rectangle(
				(int)position.getX(),
				(int)position.getY(),
				width,
				height
		);
	}

	/**
	 * Sprite constructor.
	 * @param position the position of the sprite
	 * @param texture the texture of the sprite
	 */
	public Sprite(Vector2D position, BufferedImage texture) {
		this.position = position;
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		this.hitBox = new Rectangle2D.Double(
				this.position.getX(),
				this.position.getY(),
				this.width,
				this.height
		);
	}

	/**
	 * updateHitBox updates the hitBox of the sprite.
	 */
	public abstract void update();

	/**
	 * updateHitBox updates the hitBox of the sprite.
	 * @return the updated hitBox
	 */
	public Rectangle2D updateHitBox() {
		return new Rectangle2D.Double(
				this.position.getX(),
				this.position.getY(),
				this.width = texture.getWidth(),
				this.height = texture.getHeight()
		);
	}

	/**
	 * draw draws the sprite.
	 * @param g the Graphics object
	 */
	public abstract void draw(Graphics g);
}
