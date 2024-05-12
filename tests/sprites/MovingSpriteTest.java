package sprites;

import graphics.Assets;
import math.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovingSpriteTest {

	private Player player;

	@BeforeEach
	void setUp() {
		// We initialize the assets and a player
		Assets.init();
		player = new Player("Test Name", true);
	}

	@Test
	void move() {
		// We set the position of the tank
		Vector2D originalPosition = player.tank.position;

		// We set the velocity of the tank
		player.tank.velocity = 2;

		// We set the rotation of the tank
		player.tank.position = player.tank.move();

		// We set the expected position of the tank
		Vector2D expectedPosition = new Vector2D(originalPosition.getX() + 2, originalPosition.getY());

		// We check if the position of the tank is the expected one
		assertEquals(expectedPosition.getX(), player.tank.position.getX());
		assertEquals(expectedPosition.getY(), player.tank.position.getY());
	}
}
