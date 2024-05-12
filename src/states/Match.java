package states;

import input.KeyBoard;
import main.Game1;
import sprites.*;
import graphics.Assets;
import math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Match state class
 * <p>
 *     This class is responsible for the match state of the game.
 *     It implements the IGameState interface.
 * </p>
 */
public class Match implements IGameState, KeyListener {
	/**
	 * The game instance
	 */
	private final Game1 game;
	/**
	 * The first player
	 */
	private final Player player1;
	/**
	 * The second player
	 */
	private final Player player2;
	/**
	 * The blocks array
	 */
	private final Block[] blocks;
	/**
	 * The end time of the match
	 */
	private LocalTime endTime;
	/**
	 * The VS bar x position
	 */
	public static int VSBarX;
	/**
	 * The playing area block
	 */
	public static Block playingArea = new Block(new Vector2D(0, 0));
	/**
	 * The colliders array
	 */
	public static ArrayList<Sprite> colliders = new ArrayList<>();
	/**
	 * The cells grid
	 */
	public static Cell[][] cellsGrid = new Cell[57][29];

	/**
	 * Match constructor
	 * @param game The game instance
	 * @param p1 The first player
	 * @param p2 The second player
	 */
	public Match(Game1 game, Player p1, Player p2) {
		this.game = game;
		this.player1 = p1;
		this.player2 = p2;
		this.blocks = new Block[] {
				new Block(new Vector2D(458, 407)),
				new Block(new Vector2D(933, 189)),
				new Block(new Vector2D(879, 866)),
				new Block(new Vector2D(1392, 757))
		};
		colliders.add(player1.tank);
		colliders.add(player2.tank);
		colliders.addAll(Arrays.asList(blocks));
		playingArea.hitBox.setRect(200, 215, 1530, 746);
		for (int i = 0; i < cellsGrid.length; i++) {
			for (int j = 0; j < cellsGrid[i].length; j++) {
				cellsGrid[i][j] = new Cell(
						new Vector2D(193 + i * 27, 189 + j * 27),
						Assets.cell0Texture
				);
			}
		}
		initialize();
	}

	/**
	 * Match initializer
	 */
	@Override
	public void initialize() {
		VSBarX = -Assets.VSBarTexture.getWidth() / 4;
		endTime = LocalTime.now().plusSeconds(90);
	}

	/**
	 * Match updater
	 */
	public void update() {
		boolean timeOut = LocalTime.now().isAfter(endTime);
		if (KeyBoard.isKeyDown(KeyEvent.VK_ESCAPE) || timeOut) {
			game.playersManagement.updatePlayersData(player1, player2);
			colliders.clear();
			game.playersManagement.savePlayersToJson(Game1.playersMap);
			game.statusEnum = Game1.status.MENU;
		}

		this.player1.update();
		this.player2.update();
	}

	/**
	 * Method that returns the remaining time
	 */
	private int[] getRemainingTime() {
		long totalSeconds = Duration.between(LocalTime.now(), endTime).getSeconds();
		int[] remainingTime = new int[2];
		remainingTime[0] = (int) totalSeconds / 60;
		remainingTime[1] = (int) totalSeconds % 60;
		return remainingTime;
	}

	/**
	 * Timer drawer
	 * @param g The graphics instance
	 */
	private void drawTimer(Graphics g) {
		int[] remainingTime = getRemainingTime();
		String countdown = remainingTime[0] + ":";
		countdown += remainingTime[1] < 10 ? "0" + remainingTime[1] : remainingTime[1];

		g.setFont(Assets.font2);
		g.setColor(Color.WHITE);

		FontMetrics fm = g.getFontMetrics();
		int strWidth = fm.stringWidth(countdown) / 2;
		int strHeight = fm.getHeight();

		g.drawString(countdown, Game1.SCREEN_WIDTH / 2 - strWidth, strHeight + 10);
	}

	/**
	 * Match drawer
	 * @param g The graphics instance
	 */
	public void draw(Graphics g) {
		g.drawImage(Assets.backgroundTexture, 0, 0, null);
		for (Cell[] cells : cellsGrid) {
			for (Cell cell : cells) {
				cell.draw(g);
			}
		}
		for (Block b : blocks) {
			b.draw(g);
		}
		g.drawImage(Assets.VSBarTexture, VSBarX, 0, null);
		this.player1.draw(g);
		this.player2.draw(g);
		drawTimer(g);
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }
}
