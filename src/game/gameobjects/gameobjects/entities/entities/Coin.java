package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.HitBox;
import game.data.HitBoxDirection;
import game.data.Sprite;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicMovingEntity;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;
import game.window.Window;
import game.window.light.Light;

public class Coin extends BasicMovingEntity implements Light {

	private Sprite idle = new Sprite(100, "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin","coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin_idle1_0", "coin_idle1_1", "coin_idle1_2", "coin_idle1_3", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin","coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin_idle2_0", "coin_idle2_1", "coin_idle2_0");
	private boolean collected = false;

	public Coin(float x, float y) {
		super(new HitBox(x, y, 0.75f, 1f));

		setSprite(idle);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {
		if (!collected) game.removeGameObject(this);
		collected = true;
	}

	@Override
	public void setup(Window window) {
		super.setup(window);

		window.getLightHandler().addLight(this);
	}

	@Override
	public void update(Game game) {
		super.update(game);
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
	public float getDrawingPriority() {
		return 0;
	}


	@Override
	public void getLightColor(float[] values) {
		values[0] = 1f;
		values[1] = 1f;
		values[2] = 0f;
	}

	@Override
	public void getLightPosition(float[] values) {
		values[0] = hitBox.x + hitBox.width / 2;
		values[1] = hitBox.y + hitBox.height / 2;
		values[2] = 0.95f;
	}

	@Override
	public boolean updateLight() {
		return true;
	}
}
