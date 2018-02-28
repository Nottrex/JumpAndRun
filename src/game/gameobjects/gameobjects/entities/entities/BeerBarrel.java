package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class BeerBarrel extends BasicStaticEntity{
	private static Sprite idle = new Sprite(100, "barrel");
	private static Sprite leak = new Sprite(200, "barrel_0", "barrel_1", "barrel_2", "barrel_3");

	public BeerBarrel(float x, float y, float drawingPriority) {
		super(new HitBox(x+0.125f, y, 0.875f, 1), drawingPriority);

		hitBox.type = HitBox.HitBoxType.BLOCKING;

		setSprite(idle);
	}
	@Override
	public float getCollisionPriority() {
		return 0;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, CollisionObject.InteractionType interactionType) {
		if (interactionType == CollisionObject.InteractionType.ATTACK) {
			setSprite(leak);
		}
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {

	}
}
