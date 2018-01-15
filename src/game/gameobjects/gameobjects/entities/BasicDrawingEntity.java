package game.gameobjects.gameobjects.entities;

import game.Constants;
import game.data.hitbox.HitBox;
import game.data.Sprite;
import game.gameobjects.AbstractGameObject;
import game.window.Drawable;
import game.util.TimeUtil;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.BasicShader;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public abstract class BasicDrawingEntity extends AbstractGameObject implements Drawable {
	protected HitBox hitBox;
	protected Sprite sprite;
	private float drawingPriority;
	private long startTime;

	public BasicDrawingEntity(HitBox hitBox, float drawingPriority) {
		this.hitBox = hitBox;
		this.drawingPriority = drawingPriority;
	}

	@Override
	public void setup(Window window) {

	}

	@Override
	public void draw(Window window, long time) {
		if (sprite == null) return;

		Rectangle bounds = sprite.getTexture(startTime, time);

		BasicShader shader = (BasicShader) window.getShaderHandler().getShader(ShaderType.BASIC_SHADER);

		shader.start();
		shader.setTextureSheetBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		shader.setBounds(hitBox.getCenterX() - bounds.width/(2 * (float) Constants.PIXEL_PER_TILE), hitBox.getCenterY() - bounds.height/(2*(float) Constants.PIXEL_PER_TILE), bounds.width/((float) Constants.PIXEL_PER_TILE), bounds.height/((float) Constants.PIXEL_PER_TILE));

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void cleanUp(Window window) {
		
	}

	@Override
	public float getDrawingPriority() {
		return drawingPriority;
	}

	protected void setSprite(Sprite sprite) {
		this.sprite = sprite;
		this.startTime = TimeUtil.getTime();
	}

	protected void setDrawingPriority(float drawingPriority) {
		this.drawingPriority = drawingPriority;
	}
}
