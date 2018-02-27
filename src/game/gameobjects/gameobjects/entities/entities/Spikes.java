package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.Sprite;
import game.gameobjects.CollisionObject;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class Spikes extends BasicStaticEntity {
	private static Sprite down = new Sprite(100, "spikes_bot_blood");
	private static Sprite up = new Sprite(100, "spikes_top_blood");
	private static Sprite right = new Sprite(100, "spikes_right_blood");
	private static Sprite left = new Sprite(100, "spikes_left_blood");

	public enum SpikeDirection{
		UP(up), DOWN(down), LEFT(left), RIGHT(right);

		private Sprite sprite;

		SpikeDirection(Sprite sprite) {
			this.sprite = sprite;
		}

		Sprite getSprite() {
			return sprite;
		}
	}

	public Spikes(float x, float y, float drawingPriority, SpikeDirection spikeDirection) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);

		setSprite(spikeDirection.getSprite());
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		if (gameObject instanceof Player || gameObject instanceof Zombie) {
			game.removeGameObject((GameObject) gameObject);
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
		return -5;
	}
}
