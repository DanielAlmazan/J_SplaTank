package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Assets class is used to load all the images and fonts used in the game.
 */
public class Assets {
	public static BufferedImage backgroundTexture;
	public static BufferedImage mainMenuTexture;
	public static BufferedImage selectionArrow0Texture;
	public static BufferedImage selectionArrow1Texture;
	public static BufferedImage selectionArrow2Texture;
	public static BufferedImage menuOptionsTexture;
	public static BufferedImage tutorialTexture;
	public static BufferedImage backEraseTexture;
	public static BufferedImage leaderBoardTexture;
	public static BufferedImage tank1Texture;
	public static BufferedImage tank2Texture;
	public static BufferedImage blockTexture;
	public static BufferedImage bullet1Texture;
	public static BufferedImage bullet2Texture;
	public static BufferedImage cell0Texture;
	public static BufferedImage cell1Texture;
	public static BufferedImage cell2Texture;
	public static BufferedImage VSBarTexture;
	public static Font font1;
	public static Font font2;

	/**
	 * Loads all the images and fonts used in the game.
	 */
	public static void init() {
		mainMenuTexture = Loader.ImageLoader("/mainScreen.png");
		selectionArrow0Texture = Loader.ImageLoader("/selectionArrow0.png");
		selectionArrow1Texture = Loader.ImageLoader("/selectionArrow1.png");
		selectionArrow2Texture = Loader.ImageLoader("/selectionArrow2.png");
		menuOptionsTexture = Loader.ImageLoader("/MenuOptions.png");
		tutorialTexture = Loader.ImageLoader("/tutorial.png");
		backEraseTexture = Loader.ImageLoader("/backErase.png");
		leaderBoardTexture = Loader.ImageLoader("/leaderBoard.png");
		VSBarTexture = Loader.ImageLoader("/VSBar.png");

		tank1Texture = Loader.ImageLoader("/tank1.png");
		tank2Texture = Loader.ImageLoader("/tank2.png");
		backgroundTexture = Loader.ImageLoader("/background.png");
		blockTexture = Loader.ImageLoader("/block.png");
		bullet1Texture = Loader.ImageLoader("/bullet1.png");
		bullet2Texture = Loader.ImageLoader("/bullet2.png");
		cell0Texture = Loader.ImageLoader("/cell0.png");
		cell1Texture = Loader.ImageLoader("/cell1.png");
		cell2Texture = Loader.ImageLoader("/cell2.png");

		try {
			InputStream is = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("Typori-Regular.ttf"
					);

			font1 = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException | IOException e) {
			throw new RuntimeException(e);
		}

		font1 = new Font(Assets.font1.getName(), Font.BOLD, 40);
		font2 = new Font(Assets.font1.getName(), Font.BOLD, 20);
	}
}
