package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

/**
 * Loader class is used to load all the images and fonts used in the game.
 */
public class Loader {
	/**
	 * Loads all the images and fonts used in the game.
	 * @param path The path of the image.
	 */
	public static BufferedImage ImageLoader(String path) {
		try {
			return ImageIO.read(Objects.requireNonNull(Loader.class.getResource(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
