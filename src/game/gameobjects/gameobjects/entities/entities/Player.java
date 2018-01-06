package game.gameobjects.gameobjects.entities.entities;

import game.data.HitBox;
import game.data.Sprite;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;
import game.window.Window;
import game.window.light.Light;

public class Player extends BasicWalkingEntity implements Light {

	private Sprite attack_r = new Sprite(90, "player_r_sword_0", "player_r_sword_1", "player_r_sword_2", "player_r_sword_3", "player_r_sword_4", "player_r_sword_5", "player_r_sword_6");
	private Sprite walking_r = new Sprite(100, "player_r_move_0", "player_r_move_1", "player_r_move_2", "player_r_move_3");
	private Sprite idle_r = new Sprite(250, "player_r_idle_0", "player_r_idle_0", "player_r_idle_0", "player_r_idle_0", "player_r_idle_0", "player_r_idle_0", "player_r_idle_0", "player_r_idle_0", "player_r_idle_1");
	private Sprite falling_r = new Sprite(250, "player_r_fall");
	private Sprite attack_l = new Sprite(90, "player_l_sword_0", "player_l_sword_1", "player_l_sword_2", "player_l_sword_3", "player_l_sword_4", "player_l_sword_5", "player_l_sword_6");
	private Sprite walking_l = new Sprite(100, "player_l_move_0", "player_l_move_1", "player_l_move_2", "player_l_move_3");
	private Sprite idle_l = new Sprite(250, "player_l_idle_0", "player_l_idle_0", "player_l_idle_0", "player_l_idle_0", "player_l_idle_0", "player_l_idle_0", "player_l_idle_0", "player_l_idle_0", "player_l_idle_1");
	private Sprite falling_l = new Sprite(250, "player_l_fall");

	public Player() {
		super(new HitBox(3, -3, 0.75f, 1f));

		setSprite(idle_r);
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
