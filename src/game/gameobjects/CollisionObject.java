package game.gameobjects;

import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;

import java.util.List;

public interface CollisionObject {
	/**
	 * @return a list of all HitBoxes of this object
	 **/
	List<HitBox> getCollisionBoxes();

	/**
	 * the priority of the collision -> higher priority means that this object is checked first for collision
	 *
	 * @return the priority
	 **/
	float getCollisionPriority();

	/**
	 * executed on collision with another collisionObject
	 *
	 * @param gameObject the other collisionObject
	 * @param direction  the direction of the other object relative to this one
	 * @param velocity   the velocity of the moving object of this collision
	 * @param source     if this object was the moving object for this collision
	 **/
	void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source);

	/**
	 * executed on interaction by another collisionObject
	 *
	 * @param gameObject      the other collisionObject
	 * @param hitBox          of the Interaction
	 * @param interactionType the type of this interaction e.g. ATTACK, STOMP...
	 **/
	void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType);

	/**
	 * all possible interaction types
	 **/
	enum InteractionType {
		ATTACK, INTERACT, STOMP
	}
}
