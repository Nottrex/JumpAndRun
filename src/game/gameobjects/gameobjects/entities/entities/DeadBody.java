package game.gameobjects.gameobjects.entities.entities;

import game.data.HitBox;
import game.data.Sprite;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;

public class DeadBody extends BasicWalkingEntity {

	public DeadBody(float x, float y, String entity) {
		super(new HitBox(x, y, 0.75f, 1f, HitBox.HitBoxType.NOT_BLOCKING), 0f);
		Sprite idle = new Sprite(100, entity + "_dead");
		setSprite(idle);
	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public float getCollisionPriority() {
		return -10;
	}
}
