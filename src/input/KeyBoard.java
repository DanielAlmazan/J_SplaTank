package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * KeyBoard class handles user keyboard input.
 */
public class KeyBoard implements KeyListener {
	/**
	 * upKey, downKey, leftKey, rightKey, shootKey, boostKey are the key codes
	 * of the keys used to control the tank.
	 * <p>
	 * They are set in the constructor of the KeyBoard class depending on
	 * player index.
	 */
	public int upKey, downKey, leftKey, rightKey, shootKey, boostKey;

	/**
	 * keysPressed is a set of key codes of the keys that have been pressed.
	 * It is used for moving the tank.
	 */
	private static final Set<Integer> keysPressed = new HashSet<>();

	/**
	 * Constructor of the KeyBoard class.
	 * @param up the key code of the key used to move the tank up
	 * @param down the key code of the key used to move the tank down
	 * @param left the key code of the key used to move the tank left
	 * @param right the key code of the key used to move the tank right
	 * @param shoot the key code of the key used to shoot
	 * @param boost the key code of the key used to boost
	 */
	public KeyBoard(int up, int down, int left, int right, int shoot, int boost) {
		this.upKey = up;
		this.downKey = down;
		this.leftKey = left;
		this.rightKey = right;
		this.shootKey = shoot;
		this.boostKey = boost;
	}

	/**
	 * Default constructor of the KeyBoard class.
	 */
	public KeyBoard() { }


	/**
	 * When a keyboard key is pressed, it's added to the keysPressed set.
	 * @param e the event to be processed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		keysPressed.add(e.getKeyCode());
	}

	/**
	 * When a keyboard key is released, it's removed from the keysPressed set.
	 * @param e the event to be processed
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.remove(e.getKeyCode());
	}

	/**
	 * This method is not used.
	 * @param e the event to be processed
	 */
	@Override
	public void keyTyped(KeyEvent e) { }

	/**
	 * This method checks if a particular key is pressed.
	 * It is used in the moveTank() method of the Player class.
	 * @param keyCode the key code of the key to be checked
	 * @return true if the key is pressed, false otherwise
	 */
	public static boolean isKeyDown(int keyCode) {
		return keysPressed.contains(keyCode);
	}
}
