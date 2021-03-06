package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

/**
 * A ladder that can be climbed
 */
public class Ladder extends BasicStaticEntity {
	private static Sprite wood = new Sprite(100, "wood_ladder");
	private static Sprite steel = new Sprite(100, "steel_ladder");

	public Ladder(float x, float y, float drawingPriority, boolean isWooden) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);

		setSprite((isWooden ? wood : steel));
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {

	}

	@Override
	public void update(Game game) {

	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public float getCollisionPriority() {
		return -1;
	}
}
