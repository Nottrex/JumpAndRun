package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.HitBox;
import game.data.HitBoxDirection;
import game.data.Sprite;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;
import game.window.Window;

public class Spikes extends BasicStaticEntity {
	private Sprite idle = new Sprite(100, "spikes_bot");

	public Spikes(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);

		setSprite(idle);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {
		if (gameObject instanceof Player) {
			game.removeGameObject((Player) gameObject);
		}
	}

	@Override
	public void setup(Window window) {
		super.setup(window);
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public void cleanUp(Window window) {
		super.cleanUp(window);
	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public float getCollisionPriority() {
		return -5;
	}
}
