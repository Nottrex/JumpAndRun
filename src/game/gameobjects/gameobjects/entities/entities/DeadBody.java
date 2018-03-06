package game.gameobjects.gameobjects.entities.entities;

import game.data.hitbox.HitBox;
import game.data.Sprite;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;

import java.awt.*;

/**
 * A DeadBody, that remains when a player or zombie dies
 */
public class DeadBody extends BasicWalkingEntity {

	public DeadBody(float x, float y, String entity, Color color, boolean direction) {
		super(new HitBox(x, y, 0.75f, 1f, HitBox.HitBoxType.NOT_BLOCKING), 0f);
		setColor(color == null ? Color.BLACK : color);
		Sprite idle = new Sprite(100, entity + (direction ? "_r" : "_l") + "_dead");
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
