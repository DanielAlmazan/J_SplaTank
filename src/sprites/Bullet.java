package sprites;

import graphics.Assets;
import math.Vector2D;
import states.Match;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Bullet class
 * <p>
 * This class is used to create a bullet object.
 * It extends the MovingSprite class.
 * <p>
 * It has the following properties:
 * <p>
 * - originPosition: the position where the bullet was fired from
 * - player: the player that fired the bullet
 * - MAX_DISTANCE: the maximum distance the bullet can travel
 * - active: a boolean that indicates if the bullet is active or not
 * <p>
 * It has the following methods:
 * <p>
 * - Bullet(Player player): constructor method
 * - isColliding(): checks if the bullet is colliding with any object
 * - update(): updates the bullet's position
 * - draw(Graphics g): draws the bullet
 */
public class Bullet extends MovingSprite {
	/**
	 * The position where the bullet was fired from
	 */
	public Vector2D originPosition;
	/**
	 * The player that fired the bullet
	 */
	public Player player;
	/**
	 * The maximum distance the bullet can travel
	 */
	public final int MAX_DISTANCE = 200;
	/**
	 * A boolean that indicates if the bullet is active or not
	 */
	public boolean active;

	/**
	 * Bullet constructor method
	 * <p>
	 * This method is used to create a bullet object.
	 * It takes a player as a parameter.
	 * <p>
	 * It has the following properties:
	 * <p>
	 * - player: the player that fired the bullet
	 * - active: a boolean that indicates if the bullet is active or not
	 * <p>
	 * It has the following methods:
	 * <p>
	 * - Bullet(Player player): constructor method
	 * - isColliding(): checks if the bullet is colliding with any object
	 * - update(): updates the bullet's position
	 * - draw(Graphics g): draws the bullet
	 *
	 * @param player the player that fired the bullet
	 */
	public Bullet(Player player) {
		super(player.tank.getFrontPosition());
		this.player = player;
		active = true;

		this.velocity = player.tank.velocity + 4;

		this.rotation = player.tank.rotation;
		this.originPosition = this.position;
		this.texture = player.isPlayer1 ? Assets.bullet1Texture : Assets.bullet2Texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		this.hitBox = updateHitBox();
	}

	/**
	 * Checks if the bullet is colliding with any object
	 * <p>
	 * This method is used to check if the bullet is colliding with any object.
	 * <p>
	 * It has the following properties:
	 * <p>
	 * - enemyTank: the enemy tank
	 * @return true if the bullet is colliding with any object, false otherwise
	 */
	@Override
	public boolean isColliding() {
		Tank enemyTank = player.enemy.tank;
		if (this.hitBox.intersects(enemyTank.hitBox)) {
			if (!enemyTank.isInvincible()) {
				enemyTank.health -= 15;
			}
			return true;
		}
		for (Sprite object : Match.colliders) {
			boolean collidesWithBlock = object instanceof Block && this.hitBox.intersects(object.hitBox);
			boolean exitsPlayingArea = !this.hitBox.intersects(Match.playingArea.hitBox);
			if (collidesWithBlock || exitsPlayingArea) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the bullet's position
	 */
	@Override
	public void update() {
		double distance = this.position.distance(this.originPosition, this.position);
		if (distance < MAX_DISTANCE && !isColliding()) {
			this.position = move();
			this.hitBox = updateHitBox();
		}
		else {
			//this.position = moveBackwards(3);
			this.active = false;
			this.player.paintCells(this.position);
		}
	}

	/**
	 * Draws the bullet
	 * @param g a Graphics object
	 */
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		this.at = AffineTransform.getTranslateInstance(
				this.position.getX() - (double) this.texture.getWidth() / 2,
				this.position.getY() - (double) this.texture.getHeight() / 2
		);

		this.at.rotate(this.rotation, (double) this.width /2, 0);

		g2d.drawImage(texture, at, null);
	}
}
