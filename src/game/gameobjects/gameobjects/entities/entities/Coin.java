package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.Sprite;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.window.Window;
import game.window.light.Light;

public class Coin extends BasicStaticEntity implements Light {

	private static Sprite idle = new Sprite(100, "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin","coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin_idle1_0", "coin_idle1_1", "coin_idle1_2", "coin_idle1_3", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin","coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin_idle2_0", "coin_idle2_1", "coin_idle2_0");

	public Coin(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 0.75f, 1f), drawingPriority);

		setSprite(idle);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {
		game.removeGameObject(this);
	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		game.removeGameObject(this);
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
		values[0] = 0.25f;
		values[1] = 0.25f;
		values[2] = 0f;
	}

	@Override
	public void getLightPosition(float[] values) {
		values[0] = hitBox.x + hitBox.width / 2;
		values[1] = hitBox.y + hitBox.height / 2;
		values[2] = 0.85f;
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
