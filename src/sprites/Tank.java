package sprites;

import main.Game1;
import math.Vector2D;
import graphics.Assets;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.time.LocalTime;

/**
 * Tank class
 * <p>
 * This class is used to create a tank object.
 * It extends the MovingSprite class.
 * <p>
 * It has the following methods:
 * <p>
 * - Tank(Player player): constructor method
 * - restart(): restarts the tank
 * - update(): updates the tank's position
 * - draw(Graphics g): draws the tank
 */
public class Tank extends MovingSprite {
	/**
	 * Spawn X position for player 1
	 */
	private final int SPAWN_X1 = 280;
	/**
	 * Spawn X position for player 2
	 */
	private final int SPAWN_X2 = Game1.SCREEN_WIDTH - SPAWN_X1;
	/**
	 * Spawn Y position (same for both players)
	 */
	private final int SPAWN_Y = (Game1.SCREEN_HEIGHT / 2);
	/**
	 * Health of the tank
	 */
	public int health = 100;
	public LocalTime invincibleTimeEnd;
	/**
	 * Player that owns the tank
	 */
	public Player player;

	/**
	 * Constructor method
	 * @param player the player that owns the tank
	 */
	public Tank(Player player) {
		super(new Vector2D(0, 0));

		isMovingForward = true;
		this.maxVel = STD_VEL;
		this.acceleration = 0.2;
		this.player = player;
		restart();
	}

	/**
	 * Restarts the tank
	 */
	public void restart() {
		if (player.isPlayer1) {
			position.setX(SPAWN_X1);
			rotation = 0;
			texture = Assets.tank1Texture;
		} else {
			position.setX(SPAWN_X2);
			rotation = Math.toRadians(180);
			texture = Assets.tank2Texture;
		}
		position.setX(position.getX() - (double) texture.getWidth() / 2);
		position.setY(SPAWN_Y);
		health = 100;
		this.hitBox = updateHitBox();
		invincibleTimeEnd = LocalTime.now().plusSeconds(5);
	}

	public boolean isInvincible() {
		return invincibleTimeEnd.isAfter(LocalTime.now());
	}

	/**
	 * Yes. Completely yes.
	 */
	@Override
	public void update() { }

	/**
	 * Draws the tank
	 * @param g the graphics object
	 */
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		at = AffineTransform.getTranslateInstance(this.position.getX(), this.position.getY());
		at.rotate(rotation, (double) this.texture.getWidth() / 2, (double) this.texture.getHeight() / 2);

		// Save the current composite so we can restore it later
		Composite originalComposite = g2d.getComposite();

		// Make the tank semi-transparent if it is invincible
		if(isInvincible()){
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		}
		g2d.drawImage(this.texture, at, null);

		// Restore the original composite
		g2d.setComposite(originalComposite);
	}
}
