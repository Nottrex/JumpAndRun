package game.gameobjects.gameobjects.entities.entities;

import game.data.HitBox;
import game.data.Sprite;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;
import game.window.Window;
import game.window.light.Light;

public class Player extends BasicWalkingEntity implements Light {

	private Sprite attack = new Sprite(90, "player_sword_1", "player_sword_2", "player_sword_3", "player_sword_4", "player_sword_5", "player_sword_6", "player_sword_7");
	private Sprite walking = new Sprite(1000, "player_idle_0");

	public Player() {
		super(new HitBox(3, -3, 0.75f, 1f));

		setSprite(attack);
	}

	@Override
	public void setup(Window window) {
		super.setup(window);

		window.getLightHandler().addLight(this);
	}

	@Override
	public void cleanUp(Window window) {
		super.cleanUp(window);

		window.getLightHandler().removeLight(this);
	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public float getDrawingPriority() {
		return 0;
	}


	@Override
	public void getLightColor(float[] values) {
		values[0] = 1f;
		values[1] = 1f;
		values[2] = 0f;
	}

	@Override
	public void getLightPosition(float[] values) {
		values[0] = hitBox.x + hitBox.width / 2;
		values[1] = hitBox.y + hitBox.height / 2;
		values[2] = 0.95f;
	}

	@Override
	public boolean updateLight() {
		return true;
	}
}
