package game.gameobjects;

import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;

import java.util.List;

public interface CollisionObject {
	List<HitBox> getCollisionBoxes();

	float getCollisionPriority();

	void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity);
}
