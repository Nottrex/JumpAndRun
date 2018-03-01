package game.data;

import game.util.TextureHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Sprite {
	private List<Rectangle> textures;      	       //Coordinates on spritesheet from textures
	private int time;                 			   //Time (in ms) before changing sprite

	public Sprite(int time, String... textureNames) {
		this.textures = new ArrayList<>();
		this.time = time;

		//Adding texture coordinates to list
		for (String texture : textureNames) {
			textures.add(TextureHandler.getSpriteSheetBounds("textures_" + texture));
		}
	}

	/**
	 * Gets current sprite of the animation
	 *
	 * @param startTime   time when the animation started (in ms)
	 * @param currentTime the real time when the the texture is needed (in ms)
	 * @return coordinates from current sprite
	 */
	public Rectangle getTexture(long startTime, long currentTime) {
		return textures.get((int) (((currentTime - startTime) / time) % textures.size()));
	}

	@Override
	public boolean equals(Object b) {
		if (b instanceof Sprite) {
			Sprite s = (Sprite) b;
			if (s.time != time) return false;
			if (textures.size() != s.textures.size()) return false;

			for (Rectangle r : textures) {
				boolean has = false;
				for (Rectangle r2 : s.textures) {
					if (r2.equals(r)) has = true;
				}
				if (!has) return false;
			}
			return true;
		}
		return false;
	}
}
