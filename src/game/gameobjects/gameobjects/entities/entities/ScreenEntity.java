package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicDrawingEntity;
import game.window.Window;

public class ScreenEntity extends BasicDrawingEntity {
	private HitBox hitBox2;
	private float anchorX, anchorY;

	public ScreenEntity(HitBox hitBox, float drawingPriority, Sprite sprite, float anchorX, float anchorY) {
		super(hitBox, drawingPriority);

		this.hitBox2 = hitBox.clone();

		setSprite(sprite);
		setUseCamera(false);

		this.anchorX = anchorX;
		this.anchorY = anchorY;
	}

	@Override
	public void draw(Window window, long time) {
		hitBox = hitBox2.clone();
		hitBox.width /= window.getAspectRatio();
		hitBox.x -= anchorX*hitBox.width;
		hitBox.y -= anchorY*hitBox.height;

		super.draw(window, time);
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {

	}

	public float getWidth() {
		return hitBox.width;
	}

	public float getHeight() {
		return hitBox.height;
	}
}
