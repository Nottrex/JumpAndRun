package game.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextureHandler {
	public static Map<String, BufferedImage> textures_png;
	public static Map<String, Rectangle> textures_sprite_sheet;
	public static Map<String, String> textures_sprite_sheet_texture;

	static {
		textures_png = new HashMap<>();
		textures_sprite_sheet = new HashMap<>();
		textures_sprite_sheet_texture = new HashMap<>();
	}

	private TextureHandler() {
	}

	public static void loadImagePng(String textureName, String fileName) {
		try {
			textures_png.put(textureName, ImageIO.read(ClassLoader.getSystemResource("res/textures/" + fileName + ".png")));
		} catch (IOException e) {
			ErrorUtil.printError(String.format("Loading texture: %s", textureName));
		}
	}

	public static void loadImagePngSpriteSheet(String spriteSheetName, String fileName) {
		try {
			loadImagePng(spriteSheetName, fileName);
			Scanner s = new Scanner(ClassLoader.getSystemResourceAsStream("res/textures/" + fileName + ".text"));

			int amount = Integer.valueOf(s.nextLine());

			for (int i = 0; i < amount; i++) {
				String[] line = s.nextLine().split(" ");

				String texture = line[0];
				int x = Integer.valueOf(line[1]);
				int y = Integer.valueOf(line[2]);
				int width = Integer.valueOf(line[3]);
				int height = Integer.valueOf(line[4]);

				textures_sprite_sheet.put(spriteSheetName + "_" + texture, new Rectangle(x, y, width, height));
				textures_sprite_sheet_texture.put(spriteSheetName + "_" + texture, spriteSheetName);
			}
		} catch (Exception e) {
			ErrorUtil.printError(String.format("Loading spritesheet: %s", spriteSheetName));
		}
	}

	public static Rectangle getSpriteSheetBounds(String textureName) {
		return textures_sprite_sheet.get(textureName);
	}

	public static String getSpriteSheetImage(String textureName) {
		return textures_sprite_sheet_texture.get(textureName);
	}

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

	public static int createImage(BufferedImage image) {
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

		for(int y = image.getHeight() - 1; y >= 0 ; y--){
			for(int x = 0; x < image.getWidth(); x++){
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

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

		return texture;
	}
}
