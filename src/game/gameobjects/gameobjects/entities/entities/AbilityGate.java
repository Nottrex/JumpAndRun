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
	private static Sprite left = new Sprite(100, "ability_gate_left");
	private static Sprite right = new Sprite(100, "ability_gate_right");
	private Map<Ability, Boolean> abilities;

	public AbilityGate(float x, float y, float drawingPriority, Map<Ability, Boolean> abilities, boolean isRight) {
		super(new HitBox(x, y, 0.5f, 1f), drawingPriority);
		this.abilities = abilities;

		setSprite(isRight ? right : left);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		if (gameObject instanceof Player) {
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
