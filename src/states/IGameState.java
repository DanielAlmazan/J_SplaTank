package states;

import java.awt.*;

/**
 * IGameState interface is used to implement the State design pattern
 */
public interface IGameState {
	/**
	 * initialize method is used to initialize the state
	 */
	void initialize();
	/**
	 * update method is used to update the state
	 */
	void update();
	/**
	 * draw method is used to draw the state
	 * @param g Graphics object
	 */
	void draw(Graphics g);
}
