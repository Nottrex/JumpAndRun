package game;

import game.util.TextureHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sprite {
	private List<Rectangle> textures;
	private int time;

	public Sprite(int time, String... textureNames) {
		this.textures = new ArrayList<>();
		this.time = time;

		for (String texture: textureNames) {
			textures.add(TextureHandler.getSpriteSheetBounds("textures_" + texture));
		}
	}

	public Rectangle getTexture(long currentTime) {
		return textures.get((int) ((currentTime / time) % textures.size()));
	}

}
