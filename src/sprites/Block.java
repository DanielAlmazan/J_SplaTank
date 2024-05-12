package sprites;

import graphics.Assets;
import math.Vector2D;

import java.awt.*;

/**
 * Represents a block in the game.
 * Also used for the playingArea.
 */
public class Block extends Sprite {
	/**
	 * Creates a new block.
	 * @param position The position of the block.
	 */
	public Block(Vector2D position) {
		super(position, Assets.blockTexture);
		this.texture = Assets.blockTexture;
		this.width = this.texture.getWidth();
		this.height = this.texture.getHeight();
		this.hitBox = updateHitBox();
	}

	@Override
	public void update() { /* Nothing to do here */ }

	/**
	 * Draws the block.
	 * @param g The graphics object.
	 */
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(this.texture, (int) this.position.getX(), (int) this.hitBox.getY(), null);
	}
}
