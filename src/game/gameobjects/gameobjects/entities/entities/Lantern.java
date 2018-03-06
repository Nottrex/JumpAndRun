package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.Sprite;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.window.Window;
import game.window.light.Light;

/**
 * A lantern that emits light
 */
public class Lantern extends BasicStaticEntity implements Light {
	private static Sprite on = new Sprite(100, "lantern");
	private static Sprite off = new Sprite(100, "lantern_off");

	private Tree activated;
	private boolean isOn;

	public Lantern(float x, float y, float drawingPriority, Tree activated) {
		super(new HitBox(x, y, 1f, 2f), drawingPriority);

		this.activated = activated;

		isOn = false;
		setSprite(off);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {

	}

	@Override
	public void setup(Window window) {
		super.setup(window);

		window.getLightHandler().addLight(this);
	}

	@Override
	public void update(Game game) {
		isOn = (Boolean) activated.get(game);

		Sprite s = isOn ? on : off;
		if (s != sprite) setSprite(s);
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
		values[0] = 1.35f * 255.0f / 255.0f;
		values[1] = 1.35f * 231.0f / 255.0f;
		values[2] = 1.35f * 98.0f / 255.0f;
	}

	@Override
	public void getLightPosition(float[] values) {
		values[0] = hitBox.x + hitBox.width / 2;
		values[1] = hitBox.y + hitBox.height * 4 / 5;
		values[2] = isOn ? 0.91f : 0;
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
