/** @author Daniel Enrique Almazán Sellés
 * Multiplatform Applications Development - 1º A
 * IES San Vicente
 * SPLATANK
 * */
package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Map;

import javax.swing.*;

import graphics.Assets;
import input.KeyBoard;
import sprites.Player;
import states.IGameState;
import states.Match;
import states.MainMenu;

/**
 * Main class of the game
 */
public class Game1 extends JFrame implements Runnable, KeyListener {
	/**
	 * Enum for the different states of the game
	 */
	public enum status { MENU, MATCH, EXIT };
	/**
	 * Represents the current state of the game.
	 * The statusEnum object is an instance of the Status enumeration, which
	 * defines the possible states of the game.
	 * It allows for control and management of the game's state.
	 * It is initialized to the MENU state.
	 */
	public status statusEnum = status.MENU;
	/**
	 * Represents the current state of the game as an instance of the IGameState
	 * interface. This variable is responsible for managing the game's current
	 * state and handling updates and rendering.
	 */
	private IGameState currentStatus;
	/**
	 * Thread for the game
	 */
	private Thread thread;
	private boolean running = false;
	/**
	 * Object for the players management
	 */
	public PlayersManagement playersManagement;
	/**
	 * Name of player 1
	 */
	public String player1Name = "";
	/**
	 * Name of player 2
	 */
	public String player2Name = "";
	/**
	 * Width and height of the screen
	 */
	public static final int SCREEN_WIDTH = 1934, SCREEN_HEIGHT = 1087;
	/**
	 * Canvas for the game
	 */
	public static Canvas canvas;
	/**
	 * BufferStrategy for the game
	 */
	private BufferStrategy bs;
	/**
	 * Graphics object for the game
	 */
	private Graphics g;
	/**
	 * Map for the players
	 */
	public static Map<String, Player> playersMap;
	/**
	 * FPS rate for the game
	 */
	private final int FPS = 60;
	/**
	 * Average FPS rate for the game
	 */
	private int averageFps = FPS;
	/**
	 * KeyBoard object for the game
	 */
	private KeyBoard keyBoard;

	/**
	 * Constructor for the Game
	 */
	public Game1() {
		setTitle("SplaTank");
		ImageIcon icon = new ImageIcon("res/icon.png");
		setIconImage(icon.getImage());
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

		canvas = new Canvas();
		keyBoard = new KeyBoard();

		canvas.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		canvas.setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		canvas.setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		canvas.setFocusable(true);

		add(canvas);
		canvas.addKeyListener(keyBoard);

		currentStatus = new MainMenu(this);
		canvas.addKeyListener((MainMenu) currentStatus);

		playersManagement = new PlayersManagement();
		playersMap = playersManagement.readJsonToMap();
	}

	/**
	 * The main entry point of the game
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		new Game1().start();
	}

	/**
	 * Manages the Game States and updates the keyBoard
	 */
	private void update() {
		if (statusEnum == status.MENU && !(currentStatus instanceof MainMenu)) {
			// Remove the current keyListener
			if (currentStatus instanceof Match) {
				canvas.removeKeyListener((Match) currentStatus);
			}

			// Set the new status
			currentStatus = new MainMenu(this);

			// Add the new keyListener
			canvas.addKeyListener((MainMenu) currentStatus);
		} else if (statusEnum == status.MATCH && !(currentStatus instanceof Match)) {
			// Initialize the players
			Player[] players = playersManagement.initializePlayers(
					player1Name, player2Name, playersMap
			);

			// Set the new status
			currentStatus = new Match(this, players[0], players[1]);

			// Add the new keyListener
			canvas.addKeyListener((Match) currentStatus);

			// Request focus
			canvas.requestFocus();

			// Reset the player names
			player1Name = "";
			player2Name = "";

		} else if (statusEnum == status.EXIT) {
			//
			running = false;
			System.exit(0);
		}

		currentStatus.update();
	}

	/**
	 * Draws the current status
	 */
	private void draw() {
		// Get the buffer strategy
		bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(2);
			return;
		}

		// Get the graphics object
		g = bs.getDrawGraphics();

		// Draw the current status
		currentStatus.draw(g);

		// Dispose and show the buffer strategy
		g.dispose();
		bs.show();
	}

	/**
	 * Initializes the assets
	 */
	private void init() {
		Assets.init();
	}

	/**
	 * Main game loop
	 */
	@Override
	public void run() {

		long now;
		long lastTime = System.nanoTime();
		int frames = 0;
		long time = 0;
		double delta = 0;

		init();
		while(running) {
			now = System.nanoTime();
			double targetTime = (double) 1000000000 / FPS;
			delta += (now - lastTime) / targetTime;
			time += (now - lastTime);
			lastTime = now;

			if (delta >= 1) {
				update();
				draw();
				delta--;
				frames++;
			}
			if (time >= 1000000000) {
				averageFps = frames;
				frames = 0;
				time = 0;
			}
		}
		stop();
	}

	/**
	 * Starts the thread
	 */
	private void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	/**
	 * Stops the thread
	 */
	private void stop() {
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }

}
