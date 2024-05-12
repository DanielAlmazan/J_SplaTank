package sprites;

import graphics.Assets;
import math.Vector2D;
import states.Match;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Cell class
 * <p>
 * Class that represents a cell in the grid
 * <p>
 * Extends from Sprite
 */
public class Cell extends Sprite {
	/**
	 * Cell constructor
	 * <p>
	 * Constructor of the class Cell
	 *
	 * @param position Vector2D position of the cell
	 * @param texture  BufferedImage texture of the cell
	 */
	public Cell(Vector2D position, BufferedImage texture) {
		super(position, texture);
		this.width = 28;
		this.height = 28;
		this.hitBox = updateHitBox();
		this.texture = texture;
	}

	/**
	 * setColor method
	 * <p>
	 * Method that sets the color of the cell
	 * <p>
	 * If the player is player1, the cell will be pink.
	 * If the player is player2, the cell will be green.
	 *
	 * @param player the player that owns the cell
	 * @param enemy the enemy player
	 */
	public void setColor(Player player, Player enemy) {
		if (player.isPlayer1) {
			if (texture != Assets.cell1Texture) {
				if (Player.offsetCounter < 0) {
					Match.VSBarX++;
					Player.offsetCounter = 4;
				}
				if (texture == Assets.cell2Texture) {
					if (enemy.points > 0) enemy.points--;
				}
				player.points++;
			}
			texture = Assets.cell1Texture;
		} else {
			if (texture != Assets.cell2Texture) {
				if (Player.offsetCounter < 0) {
					Match.VSBarX--;
					Player.offsetCounter = 4;
				}
				if (texture == Assets.cell1Texture) {
					if (enemy.points > 0) enemy.points--;
				}
				player.points++;
			}
			texture = Assets.cell2Texture;
		}
		Player.offsetCounter--;
	}

	@Override
	public void update() { /* Nothing to do here */ }

	/**
	 * draw method, used for drawing the cell with g2d (Graphics2D)
	 * @param g transformed into g2d
	 */
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(this.texture, (int) this.position.getX(), (int) this.hitBox.getY(), null);
	}
}
