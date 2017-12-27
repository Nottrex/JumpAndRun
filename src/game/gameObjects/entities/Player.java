package game.gameObjects.entities;

import game.HitBox;
import game.Sprite;
import game.gameObjects.GameObject;
import game.window.Light;
import game.window.Window;

public class Player extends BasicWalkingEntity implements Light {

	private Sprite walking = new Sprite(1000, "player", "player2");

	public Player() {
		super(new HitBox(3,-3,1,1));
	}

	@Override
	public void setup(Window window) {
		super.setup(window);

		window.getLightHandler().addLight(this);
	}

	@Override
	public void cleanUp(Window window) {
		super.cleanUp(window);

		window.getLightHandler().removeLight(this);
	}

	@Override
	public void collide(GameObject gameObject, HitBox.HitBoxDirection direction) {
		super.collide(gameObject, direction);

	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public float getDrawingPriority() {
		return 0;
	}

	@Override
	public Sprite getCurrentSprite() {
		return walking;
	}


	@Override
	public void getLightColor(float[] values) {
		values[0] = 1f;
		values[1] = 1f;
		values[2] = 0f;
	}

	@Override
	public void getLightPosition(float[] values) {
		values[0] = hitBox.x + hitBox.width/2;
		values[1] = hitBox.y + hitBox.height/2;
		values[2] = 0.95f;
	}

	@Override
	public boolean updateLight() {
		return true;
	}
}
