package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.Sprite;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;

/**
 * A box that can be moved
 */
public class Box extends BasicWalkingEntity {
	private static Sprite idle = new Sprite(100, "box");

	public Box(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);

		setSprite(idle);
	}

	@Override
	public void update(Game game) {
		super.update(game);

		this.setMx(0);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		super.collide(gameObject, direction, velocity, source);

		/*if (gameObject instanceof Player && (direction == HitBoxDirection.LEFT || direction == HitBoxDirection.RIGHT)) {
			this.setMx(direction.invert().getXDirection()*0.1f);
		}*/

		if (direction == HitBoxDirection.DOWN && velocity > 0.15f) {
			game.getCamera().addScreenshake(0.01f);
		}
	}

	@Override
	public float getCollisionPriority() {
		return 1;
	}

	@Override
	public float getPriority() {
		return 1;
	}
}
