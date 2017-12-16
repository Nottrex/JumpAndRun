package game.gameObjects.entities;

import game.HitBox;
import game.Sprite;
import game.gameObjects.Drawable;
import game.util.TextureHandler;
import game.window.Window;

import java.awt.*;

public abstract class BasicDrawingEntity implements Drawable {
	protected HitBox hitBox;

	public BasicDrawingEntity(HitBox hitBox) {
		this.hitBox = hitBox;
	}

	@Override
	public void draw(Window window, long time) {
		String texture = getCurrentSprite().getTexture(time);

		Rectangle bounds = TextureHandler.getSpriteSheetBounds("textures_" + texture);
	}

	public abstract Sprite getCurrentSprite();
}
