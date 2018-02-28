package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.gameobjects.AbstractGameObject;
import game.gameobjects.gameobjects.particle.ParticleType;
import java.util.HashMap;
import java.util.Map;

public class DeadBodyHandler extends AbstractGameObject {

	private Map<DeadBody, Integer> deadBodies;

	public DeadBodyHandler() {
		deadBodies = new HashMap<>();
	}

	@Override
	public void update(Game game) {

	}

	public void addDeadBody(DeadBody deadBody) {
		game.addGameObject(deadBody);
		deadBodies.put(deadBody, game.getGameTick());
		game.getParticleSystem().createParticle(ParticleType.EXPLOSION, deadBody.getHitBox().getCenterX(), deadBody.getHitBox().getCenterY(), 0, 0);

	}

	@Override
	public void remove(Game game, boolean mapChange) {
		super.remove(game, mapChange);

		for (DeadBody d: deadBodies.keySet()) {
			game.removeGameObject(d);
		}
	}

	public Map<DeadBody, Integer> getDeadBodies() {
		return deadBodies;
	}

	@Override
	public float getPriority() {
		return 0;
	}
}
