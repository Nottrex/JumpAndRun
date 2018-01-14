package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.HitBox;
import game.data.Sprite;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.window.Window;
import game.window.light.Light;

public class DeadBody extends BasicWalkingEntity {


	private Sprite idle;

	public DeadBody(float x, float y, String entity) {
		super(new HitBox(x, y, 0.75f, 1f, HitBox.HitBoxType.NOT_BLOCKING), 0f);
		idle = new Sprite(100, entity + "_dead");
		setSprite(idle);
	}

	@Override
	public void init(Game game) {
		super.init(game);
	}

	@Override
	public void setup(Window window) {
		super.setup(window);
	}

	@Override
	public void cleanUp(Window window) {
		super.cleanUp(window);
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
