package game.gameobjects;

import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;

import java.util.List;

public interface CollisionObject {
	List<HitBox> getCollisionBoxes();

	float getCollisionPriority();

	void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity);
	void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType);

	enum InteractionType {
		ATTACK, INTERACT
	}
}
