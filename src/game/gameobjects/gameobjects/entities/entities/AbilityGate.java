package game.gameobjects.gameobjects.entities.entities;

import game.Ability;
import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

import java.util.Map;

public class AbilityGate extends BasicStaticEntity {
	private static Sprite idle = new Sprite(100, "ability_gate");
	private Map<Ability, Boolean> abilities;

	public AbilityGate(float x, float y, float drawingPriority, Map<Ability, Boolean> abilities) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);
		this.abilities = abilities;

		setSprite(idle);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		if (gameObject instanceof Player) {
			((Player) gameObject).addAbility(Ability.STOMP);
			for (Ability a: abilities.keySet()) {
				if (abilities.get(a)) ((Player) gameObject).addAbility(a);
				else ((Player) gameObject).removeAbility(a);
			}
		}
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
		return 0;
	}
}
