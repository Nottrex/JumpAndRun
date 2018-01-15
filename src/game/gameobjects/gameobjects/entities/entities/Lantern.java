package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.Sprite;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.window.Window;
import game.window.light.Light;

public class Lantern extends BasicStaticEntity implements Light{
	private Sprite idle = new Sprite(100, "lantern");

	public Lantern(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 1f, 2f), drawingPriority);

		setSprite(idle);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {

	}

	@Override
	public void setup(Window window) {
		super.setup(window);

		window.getLightHandler().addLight(this);
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public void cleanUp(Window window) {
		super.cleanUp(window);

		window.getLightHandler().removeLight(this);
	}

	@Override
	public float getPriority() {
		return 1;
	}


	@Override
	public void getLightColor(float[] values) {
		values[0] = 255.0f/255.0f;
		values[1] = 231.0f/255.0f;
		values[2] = 98.0f/255.0f;
	}

	@Override
	public void getLightPosition(float[] values) {
		values[0] = hitBox.x + hitBox.width / 2;
		values[1] = hitBox.y + hitBox.height * 4 / 5;
		values[2] = 0.95f;
	}

	@Override
	public boolean updateLight() {
		return true;
	}

	@Override
	public float getCollisionPriority() {
		return -1;
	}
}
