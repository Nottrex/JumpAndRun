package game.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextureHandler {
	private static Map<String, BufferedImage> textures_png;			//saved images with name
	private static Map<String, Rectangle> textures_sprite_sheet;		//coordinates on spritesheet from given texture name
	private static Map<String, String> textures_sprite_sheet_texture;	//corresponding spritesheet names from given texture names

	static {
		textures_png = new HashMap<>();
		textures_sprite_sheet = new HashMap<>();
		textures_sprite_sheet_texture = new HashMap<>();
	}

	static {
		TextureHandler.loadImagePngSpriteSheet("textures", "textures");
		//exportTexturePng();
	}

	private TextureHandler() {
	}

	/** Loads texture and saves it
	*   @param textureName the name of texture
	*   @param fileName name of the PNG-file with the texture
	*//
	public static void loadImagePng(String textureName, String fileName) {
		try {
			textures_png.put(textureName, ImageIO.read(ClassLoader.getSystemResource("res/textures/" + fileName + ".png")));
		} catch (IOException e) {
			ErrorUtil.printError(String.format("Loading texture: %s", textureName));
		}
	}

	/** Loads spritesheet and saves if with all subimages
	*   @param spriteSheetName name of the spritesheet
	*   @param fileName name of the text-file with spritesheet 
	**/
	public static void loadImagePngSpriteSheet(String spriteSheetName, String fileName) {
		try {
			//loading spritesheet.png and reading number of subimages
			loadImagePng(spriteSheetName, fileName);
			Scanner s = new Scanner(ClassLoader.getSystemResourceAsStream("res/textures/" + fileName + ".text"));

			int amount = Integer.valueOf(s.nextLine());

			//Reading and saving of subimages
			for (int i = 0; i < amount; i++) {
				try {
					String[] line = s.nextLine().split(" ");

					String texture = line[0];
					int x = Integer.valueOf(line[1]);
					int y = Integer.valueOf(line[2]);
					int width = Integer.valueOf(line[3]);
					int height = Integer.valueOf(line[4]);

					textures_sprite_sheet.put(spriteSheetName + "_" + texture, new Rectangle(x, y, width, height));
					textures_sprite_sheet_texture.put(spriteSheetName + "_" + texture, spriteSheetName);
				} catch (Exception e) {
					ErrorUtil.printError(String.format("Loading spriteSheet: %s in line %d (%s)", spriteSheetName, i + 2, e.toString()));
				}
			}
		} catch (Exception e) {
			ErrorUtil.printError(String.format("Loading spriteSheet: %s (%s)", spriteSheetName, e.toString()));
		}
	}

	/** Gets the coordinates on spritesheet of given texture name
	*   @param textureName name of the texture 
	*   @return the corresponding coordinates on the spritesheet
	**/
	public static Rectangle getSpriteSheetBounds(String textureName) {
		if (textures_sprite_sheet.containsKey(textureName)) return textures_sprite_sheet.get(textureName);
		System.out.println(String.format("No such spriteSheet image: %s", textureName));
		return textures_sprite_sheet.get("textures_texture_error");
	}

	/** Gets the spritesheet of given texture name
	*   @param textureName name of the texture 
	*   @return the corresponding spritesheet name
	**/
	public static String getSpriteSheetImage(String textureName) {
		return textures_sprite_sheet_texture.get(textureName);
	}

	/** Gets Image of the texture name
	*   @param textureName name of the texture
	*   @return texture of the texture name
	**/
	public static BufferedImage getImagePng(String textureName) {
		if (textures_png.containsKey(textureName))
			return textures_png.get(textureName);
		else if (textures_sprite_sheet.containsKey(textureName)) {
			Rectangle rec = textures_sprite_sheet.get(textureName);
			return textures_png.get(textures_sprite_sheet_texture.get(textureName)).getSubimage(rec.x, rec.y, rec.width, rec.height);
		}
		ErrorUtil.printError(String.format("No such image: %s", textureName));
		return null;
	}

	/** Converts BufferedImage to OpenGL image
	*   @param image image to be converted
	*   @return 
	*//
	public static int createImage(BufferedImage image) {
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
				buffer.put((byte) (pixel & 0xFF));               // Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component
			}
		}
		buffer.flip();

		int texture = GL11.glGenTextures();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,  GL14.GL_MIRRORED_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,  GL14.GL_MIRRORED_REPEAT);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

		return texture;
	}

	/** Exports every texture in bigger scale => used for wiki textures
	**/
	private static void exportTexturePng() {
		for (String s: textures_sprite_sheet.keySet()) {
			try {
				BufferedImage i = getImagePng(s);
				int width = i.getWidth();
				int height = i.getHeight();
				while(width < 100 && height < 100) {
					width *= 2;
					height *=2;
				}

				BufferedImage ni = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				ni.getGraphics().drawImage(i, 0, 0, width, height, null);

				ImageIO.write(ni, "png", new File(System.getProperty("user.dir") + File.separator + "wiki_textures"  + File.separator + s + ".png"));
				System.out.println( new File(System.getProperty("user.dir") + s + ".png").getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
