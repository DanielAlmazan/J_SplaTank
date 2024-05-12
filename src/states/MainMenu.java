package states;

import graphics.Assets;
import main.Game1;
import math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * MainMenu state class
 * <p>
 *     This class is responsible for the main menu state of the game.
 *     It implements the IGameState interface.
 * </p>
 */
public class MainMenu implements IGameState, KeyListener {
	//region Properties
	/**
	 * The coolDown limit of the key listener
	 */
	private final byte COOL_DOWN = 7;
	/**
	 * The coolDown state of the key listener
	 */
	private byte coolDown = COOL_DOWN;
	/**
	 * The game instance
	 */
	private final Game1 game;
	/**
	 * For requesting the players' names
	 */
	private boolean player1Ready = false;
	/**
	 * For requesting the players' names
	 */
	private boolean player2Ready = false;
	/**
	 * The selected option
	 */
	private byte selectedOption = 0;
	/**
	 * The new game state
	 */
	private boolean newGame;
	/**
	 * The show leader board state
	 */
	private boolean showLeaderBoard;
	/**
	 * The last typed key
	 */
	private KeyEvent lastTypedKey;
	/**
	 * The key map for the key listener
	 */
	private final Map<Integer, Character> keyMap = new HashMap<>();
	//endregion

	/**
	 * MainMenu constructor
	 * @param game The game instance
	 */
	public MainMenu(Game1 game) {
		this.game = game;
		game.addKeyListener(this);
		initialize();
	}

	/**
	 * Method used for setting the keyMap
	 */
	@Override
	public void initialize() {
		for (int i = 0; i < 10; i++) {
			keyMap.put(KeyEvent.VK_0 + i, Character.forDigit(i, 10));
		}
		for (char c = 'A'; c <= 'Z'; c++) {
			keyMap.put(KeyEvent.VK_A + (c - 'A'), c);
		}
	}

	/**
	 * Method used for updating the MainMenu state
	 */
	@Override
	public void update() {
		coolDown--;

		if (coolDown < 0 && lastTypedKey != null) {
			processKey(getLastTypedKey());
			coolDown = COOL_DOWN;
		}
	}

	/**
	 * Method for processing the navigation of the main menu
	 * @param e The key event
	 */
	private void processKey(KeyEvent e) {
		if (keyMap.containsKey(e.getKeyCode()) && newGame) {
			nameBuilder(e);
		}
		else {
			// Used suggested "Improved switch statement" from IntelliJ IDEA
			switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE -> {
					if (newGame || showLeaderBoard) {
						goBack();
					} else game.statusEnum = Game1.status.EXIT;
				}
				case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> changeSelectedOption(e.getKeyCode() == KeyEvent.VK_DOWN);
				case KeyEvent.VK_ENTER -> processEnterKey();
				case KeyEvent.VK_BACK_SPACE -> {
					goBack();
					if (newGame) {
						if (!player1Ready) {
							game.player1Name = "";
						} else if (!player2Ready) {
							game.player2Name = "";
						}
					}
				}
			}
		}
		lastTypedKey = null;
	}

	/**
	 * Method for setting the current selected option
	 * @param isDownKey If the down key is pressed, the selected option is
	 *                     incremented, otherwise it is decremented
	 */
	private void changeSelectedOption(boolean isDownKey) {
		if (!newGame && !showLeaderBoard) {
			selectedOption = isDownKey
					?(selectedOption < 2 ? ++selectedOption : 0)
					:(selectedOption > 0 ? --selectedOption : 2);
		}
	}

	/**
	 * Method for processing the enter key
	 * <p>
	 *     If the selected option is 0, the newGame state is set to true and
	 *     the namesSetter method is called.
	 *     If the selected option is 1, the showLeaderBoard state is set to true.
	 *     If the selected option is 2, the game is exited.
	 * </p>
	 */
	private void processEnterKey() {
		if (selectedOption == 0) {
			newGame = true;
			namesSetter();
		}
		else if (selectedOption == 1) { showLeaderBoard = true;}
		else if (selectedOption == 2) { game.statusEnum = Game1.status.EXIT; }
	}

	/**
	 * Method for going back to the previous state
	 */
	public void goBack() {
		if (game.player1Name == "" && (newGame || showLeaderBoard)) {
			newGame = false;
			showLeaderBoard = false;
		} else if (game.player2Name == "") {
			game.player1Name = "";
			game.player2Name = "";
			player1Ready = false;
		}
	}

	/**
	 * Method for setting players' names
	 * @param e The key event
	 */
	public void nameBuilder(KeyEvent e) {
		final int MAX_NAME_LENGTH = 10;
		char letter = keyMap.get(e.getKeyCode());
		if (!player1Ready && game.player1Name.length() < MAX_NAME_LENGTH) {
			game.player1Name += letter;
		} else if (!player2Ready && game.player2Name.length() < MAX_NAME_LENGTH) {
			game.player2Name += letter;
		}
	}

	/**
	 * Method for setting the player1Ready and player2Ready states and start
	 * a new match if both players are ready
	 */
	public void namesSetter() {
		if (!player1Ready) {
			player1Ready = game.player1Name.length() > 0;
		} else if (!player2Ready) {
			player2Ready = game.player2Name.length() > 0 &&
					!game.player2Name.equals(game.player1Name);
		}
		else game.statusEnum = Game1.status.MATCH;
	}

	/**
	 * Draws the main menu
	 * @param g Graphics object
	 */
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(Assets.mainMenuTexture, 0, 0, null);
		drawHUB(g2d);
	}

	/**
	 * Draws the main menu HUB
	 * @param g2d Graphics2D object
	 */
	public void drawHUB(Graphics2D g2d) {
		if (!newGame && !showLeaderBoard) {
			drawMenu(g2d);
		} else if (newGame) {
			drawBackErase(g2d);
			drawNamesRequest(g2d);
		} else {
			drawBackErase(g2d);
			drawLeaderBoard(g2d);
		}
	}

	/**
	 * Draws the menu options
	 * @param g2d Graphics2D object
	 */
	public void drawMenu(Graphics2D g2d) {
		BufferedImage menuOptTexture = Assets.menuOptionsTexture;
		Vector2D menuOptPos = new Vector2D(
				(double) Game1.SCREEN_WIDTH / 2 - (double) menuOptTexture.getWidth() / 2,
				(double) Game1.SCREEN_HEIGHT / 2 - 120
		);
		g2d.drawImage(
				menuOptTexture,
				(int) menuOptPos.getX(),
				(int) menuOptPos.getY(),
				null
		);
		drawArrow(g2d, menuOptPos);
	}

	/**
	 * Draws the proper arrow depending on the selected option
	 * @param g2d Graphics2D object
	 * @param menuOptPos The position of the menu options
	 */
	public void drawArrow(Graphics2D g2d, Vector2D menuOptPos) {
		double arrowX = menuOptPos.getX() - 100;
		BufferedImage[] arrowTextures = {
				Assets.selectionArrow0Texture,
				Assets.selectionArrow1Texture,
				Assets.selectionArrow2Texture
		};

		Vector2D arrowPos = new Vector2D(arrowX, menuOptPos.getY() + 130 * selectedOption);

		g2d.drawImage(
				arrowTextures[selectedOption],
				(int) arrowPos.getX(),
				(int) arrowPos.getY(),
				null
		);
	}

	/**
	 * Draws names request, the letters typed and when both players are ready,
	 * shows the tutorial and waits for the enter key to start the match
	 * @param g2d Graphics2D object
	 */
	public void drawNamesRequest(Graphics2D g2d) {
		double halfScreenWidth = (double) Game1.SCREEN_WIDTH / 2;
		double halfScreenHeight = (double) Game1.SCREEN_HEIGHT / 2;

		g2d.setFont(new Font(Assets.font1.getName(), Font.BOLD, 30));
		g2d.setColor(Color.WHITE);

		String text1;
		String text2;

		if (!player1Ready) {
			text1 = "Player 1, enter your name: ";
			text2 = game.player1Name;
		} else if (!player2Ready) {
			text1 = "Player 2, enter your name: ";
			text2 = game.player2Name;
		} else {
			text1 = "Press Enter to start the match";
			text2 = "";
			g2d.drawImage(Assets.tutorialTexture, 0, 0, null);
		}

		drawStringCentered(g2d, text1, (int) halfScreenWidth, (int) (halfScreenHeight - 140));
		drawStringCentered(g2d, text2, (int) halfScreenWidth, (int) (halfScreenHeight - 40));
	}

	/**
	 * Draws a centered string
	 * @param g2d Graphics2D object
	 * @param str The string to be drawn
	 * @param x The x coordinate of the center
	 * @param y The y coordinate of the center
	 */
	public void drawStringCentered(Graphics2D g2d, String str,int x, int y) {
		FontMetrics fm = g2d.getFontMetrics();
		int strWidth = fm.stringWidth(str);
		g2d.drawString(str, x - strWidth / 2, y);
	}

	/**
	 * Draws the leaderboard texture and the players data
	 * @param g2d Graphics2D object
	 */
	public void drawLeaderBoard(Graphics2D g2d) {
		BufferedImage boardTexture = Assets.leaderBoardTexture;
		double halfScreenWidth = (double) Game1.SCREEN_WIDTH / 2;
		double halfScreenHeight = (double) Game1.SCREEN_HEIGHT / 2;

		Vector2D boardPos = new Vector2D(
				halfScreenWidth - (double) boardTexture.getWidth() / 2,
				halfScreenHeight - (double) boardTexture.getHeight() / 2
		);

		g2d.drawImage(
				boardTexture,
				(int) boardPos.getX(),
				(int) boardPos.getY(),
				null);

		String[][] playersData = game.playersManagement.getPlayersDataColumns(Game1.playersMap);
		int baseY = (int) boardPos.getY() + 100;
		int lineHeight = 30;  // Define the line height as needed.

		g2d.setFont(Assets.font2);
		g2d.setColor(Color.WHITE);

		// The x-positions for each column.
		int[] xs = {
				(int)boardPos.getX() + 60,
				(int)boardPos.getX() + 110,
				(int)boardPos.getX() + 295,
				(int)boardPos.getX() + 420
		};

		for (int i = 0; i < playersData.length; i++) {
			for (int j = 0; j < playersData[i].length; j++) {
				g2d.drawString(playersData[i][j], xs[i], baseY + j * lineHeight);
			}
		}
	}

	/**
	 * Draws the back/erase button
	 * @param g2d Graphics2D object
	 */
	public void drawBackErase(Graphics2D g2d) {
		BufferedImage texture = Assets.backEraseTexture;
		g2d.drawImage(
				Assets.backEraseTexture,
				Game1.SCREEN_WIDTH - texture.getWidth() - 30,
				Game1.SCREEN_HEIGHT - texture.getHeight() - 30,
				null
		);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Saves the last typed key
	 * @param e the event to be processed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		lastTypedKey = e;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Returns the last typed key
	 * @return the last typed key
	 */
	public KeyEvent getLastTypedKey() {
		return lastTypedKey;
	}
}















