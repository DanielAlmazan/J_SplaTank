package sprites;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.*;
import java.util.List;

import com.google.gson.annotations.Expose;
import graphics.Assets;
import input.KeyBoard;
import main.Game1;
import math.Vector2D;
import states.Match;

/**
 * Player is a class that represents a player in the game.
 * It contains the tank, the bullets, the enemy, the keyBoard and the scores of the player.
 * <p>
 * The player can move the tank, shoot and restart the game.
 * <p>
 * It is serializable.
 */
public class Player implements Serializable {
	/**
	 * The name of the player.
	 */
	@Expose
	public String name;
	/**
	 * Boolean that indicates if the player is player 1 or not.
	 */
	public boolean isPlayer1;
	/**
	 * The points of the player during the match.
	 */
	@Expose
	public int points;
	/**
	 * The historical record of the player.
	 */
	@Expose
	public int record;
	/**
	 * The number of matches won by the player.
	 */
	@Expose
	public int matchesWon;
	/**
	 * The number of matches lost by the player.
	 */
	@Expose
	public int matchesLost;
	/**
	 * The enemy of the player.
	 */
	public Player enemy;
	/**
	 * The tank of the player.
	 */
	public Tank tank;
	/**
	 * The bullets of the player.
	 */
	public List<Bullet> bullets;
	/**
	 * The keyBoard of the player, with custom keys depending on whether it is player 1 or 2.
	 */
	public KeyBoard keyBoard;
	/**
	 * The counter that controls the offset of the VS Bar in the match instance.
	 */
	public static int offsetCounter;
	/**
	 * The counter that controls the shooting rate of the tank.
	 */
	private int shootingCoolDown;

	/**
	 * The constructor of the class.
	 * It initializes the name, the tank, the bullets and the keyBoard of the player.
	 *
	 * @param name      The name of the player.
	 * @param isPlayer1 Boolean that indicates if the player is player 1 or not.
	 */
	public Player(String name, boolean isPlayer1) {
		this.name = name;
		this.isPlayer1 = isPlayer1;
		this.tank = new Tank(this);
		this.bullets = new ArrayList<>();

		// We assign the keyBoard to the player depending on whether it is player 1 or 2
		this.keyBoard = isPlayer1 ?
				new KeyBoard(
						KeyEvent.VK_W,
						KeyEvent.VK_S,
						KeyEvent.VK_A,
						KeyEvent.VK_D,
						KeyEvent.VK_SPACE,
						KeyEvent.VK_SHIFT
				) :
				new KeyBoard(KeyEvent.VK_UP,
						KeyEvent.VK_DOWN,
						KeyEvent.VK_LEFT,
						KeyEvent.VK_RIGHT,
						KeyEvent.VK_ENTER,
						KeyEvent.VK_CONTROL
				);
	}

	/**
	 * moveTank method, used to move the tank of the player.
	 */
	public void moveTank() {
		// Directional keys
		final double DELTA_ANGLE = 0.055;
		if (KeyBoard.isKeyDown(keyBoard.leftKey)) { tank.rotation -= DELTA_ANGLE; }
		else if (KeyBoard.isKeyDown(keyBoard.rightKey)) { tank.rotation += DELTA_ANGLE; }

		// Boost key
		tank.maxVel = KeyBoard.isKeyDown(keyBoard.boostKey) ?
				tank.BOOST_VEL : tank.STD_VEL;

		// Movement keys
		if (KeyBoard.isKeyDown(keyBoard.upKey)) {
			tank.accelerate();
			tank.isMovingForward = true;
		} else if (KeyBoard.isKeyDown(keyBoard.downKey)) {
			tank.accelerate();
			tank.isMovingForward = false;
		} else {
			tank.decelerate();
		}

		// Collision
		if (tank.isColliding()) {
			tank.decelerate(1.5f);
			tank.maxVel = 0;
		}

		tank.position = tank.move();
		// Shoot key
		if (KeyBoard.isKeyDown(keyBoard.shootKey)) { shoot(); }

	}

	/**
	 * shoot method adds a bullet to the list of bullets of the player.
	 */
	public void shoot() {
		if (shootingCoolDown < 0) {
			Bullet bullet = new Bullet(this);
			bullets.add(bullet);
			shootingCoolDown = 8; // 8 frames between each bullet
		}
	}

	/**
	 * moveBullets method moves the bullets of the player.
	 * <p>
	 * It also checks if the bullet is active or not, and if it is not active,
	 * it removes it from the list.
	 */
	public void moveBullets() {
		shootingCoolDown--;
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			if (bullet.active) {
				bullet.update();
			} else {
				bullets.remove(i);
				i--;
			}
		}
	}

	/**
	 * update method checks the health of the tank, and gives the half of the
	 * points to the enemy if the tank is dead, then it respawns the tank.
	 * <p>
	 * It also moves the tank and the bullets of the player.
	 */
	public void update() {
		if (this.tank.health <= 0) {
			enemy.points += this.points / 2;
			this.points /= 2;
			this.tank.restart();
		}
		moveTank();
		moveBullets();
	}

	/**
	 * paintCells method paints the cells of the grid where the bullet falls.
	 * @param dropPosition The position where the bullet falls.
	 */
	public void paintCells(Vector2D dropPosition) {
		Vector2D[] area = getPaintingArea(dropPosition);

		// We check where the bullet falls and paint the cells accordingly with a 1 cells radius
		for (int i = (int) area[0].getX(); i <= (int) area[1].getX(); i++) {
			for (int j = (int) area[0].getY(); j <= (int) area[1].getY(); j++) {
				Match.cellsGrid[i][j].setColor(this, this.enemy);
			}
		}
	}

	/**
	 * getEpicenter method returns the epicenter of the explosion.
	 * @param dropPosition The position where the bullet falls.
	 * @return The epicenter of the explosion.
	 */
	public Vector2D getEpicenter(Vector2D dropPosition) {
		// We check how far the bullet is from the edges
		int x = (int) dropPosition.getX();
		int y = (int) dropPosition.getY();

		if (x < Match.cellsGrid[0][0].position.getX()) { x = (int) Match.cellsGrid[0][0].position.getX(); }
		else if (x > Match.cellsGrid[Match.cellsGrid.length - 1][0].position.getX()) {
			x = (int) Match.cellsGrid[Match.cellsGrid.length - 1][0].position.getX() + 1;
		}

		// We go through the array of cells to store the index of the first and last cell
		for (int i = 0; i < Match.cellsGrid.length; i++) {
			for (int j = 0; j < Match.cellsGrid[i].length; j++) {
				if (Match.cellsGrid[i][j].hitBox.contains(x, y)) {
					return new Vector2D(i, j);
				}
			}
		}
		return new Vector2D(x, y);
	}

	/**
	 * getPaintingArea method returns the area where the bullet will paint.
	 * @param dropPosition The position where the bullet falls.
	 * @return The area where the bullet will paint.
	 */

	public Vector2D[] getPaintingArea(Vector2D dropPosition) {
		Vector2D[] area = new Vector2D[2];
		Vector2D epicenter = getEpicenter(dropPosition);
		int radius = 1;

		int firstX, firstY, lastX, lastY;

		firstX = Math.max((int)epicenter.getX() - radius, 0);
		lastX = Math.min((int)epicenter.getX() + radius, Match.cellsGrid.length - 1);

		firstY = Math.max((int)epicenter.getY() - radius, 0);
		lastY = Math.min((int)epicenter.getY() + radius, Match.cellsGrid[0].length - 1);

		area[0] = new Vector2D(firstX, firstY);
		area[1] = new Vector2D(lastX, lastY);

		return area;
	}

	/**
	 * draw method draws the tank and the bullets of the player.
	 * <p>
	 * It also draws the name, points and record of the player.
	 * @param g The graphics object.
	 */
	public void draw(Graphics g) {
		tank.draw(g);
		for (Bullet bullet : bullets) {
			bullet.draw(g);
		}

		FontMetrics fm = g.getFontMetrics(Assets.font2);
		String playerInfo = this.name + " | Points: " + this.points + " | Record:" + this.record;
		int x = isPlayer1 ? 10 : Game1.SCREEN_WIDTH - fm.stringWidth(playerInfo) - 30;
		int y = fm.getHeight() + 10;
		g.setFont(Assets.font2);
		g.setColor(Color.WHITE);

		g.drawString(playerInfo, x, y);
	}
}