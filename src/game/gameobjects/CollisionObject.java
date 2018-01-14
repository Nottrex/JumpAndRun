package game.gameobjects;

import game.data.HitBox;
import game.data.HitBoxDirection;

import java.util.List;

public interface CollisionObject {
	List<HitBox> getCollisionBoxes();

	float getCollisionPriority();

	void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity);
}
