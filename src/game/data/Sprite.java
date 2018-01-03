package game.data;

import game.util.TextureHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Sprite {
	private List<Rectangle> textures;
	private int time;

	public Sprite(int time, String... textureNames) {
		this.textures = new ArrayList<>();
		this.time = time;

		for (String texture : textureNames) {
			textures.add(TextureHandler.getSpriteSheetBounds("textures_" + texture));
		}
	}

	public Rectangle getTexture(long startTime, long currentTime) {
		return textures.get((int) (((currentTime - startTime) / time) % textures.size()));
	}

}
