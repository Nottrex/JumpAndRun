package game.gameObjects.entities;

import game.HitBox;
import game.Sprite;
import game.gameObjects.Drawable;
import game.util.TextureHandler;
import game.window.BasicShader;
import game.window.Window;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public abstract class BasicDrawingEntity implements Drawable {
	protected HitBox hitBox;

	public BasicDrawingEntity(HitBox hitBox) {
		this.hitBox = hitBox;
	}

	@Override
	public void setup(Window window) {
		window.getShaderHandler().loadShader("BasicShader");
	}

	@Override
	public void draw(Window window, long time) {
		Rectangle bounds = getCurrentSprite().getTexture(time);

		BasicShader shader = (BasicShader) window.getShaderHandler().getShader("BasicShader");

		shader.start();
		shader.setTextureSheetBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		shader.setBounds(hitBox.x, hitBox.y, hitBox.width, hitBox.height);

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void cleanUp(Window window) {
		window.getShaderHandler().unloadShader("BasicShader");
	}

	public abstract Sprite getCurrentSprite();
}
