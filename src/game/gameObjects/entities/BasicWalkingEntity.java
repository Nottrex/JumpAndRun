package game.gameObjects.entities;

import game.Game;
import game.HitBox;
import game.gameObjects.GameObject;
import game.util.MathUtil;

public abstract class BasicWalkingEntity extends BasicMovingEntity {
	private static final float SPEED = 0.175f;
	private static final float JUMP_ACCELERATION = 0.3f;
	private static final float DOWN_ACCELERATION = 0.1f;
	private static final float GRAVITY_ACCELERATION = 0.04f;
	private static final float MAX_GRAVITY_SPEED = 0.3f;
	private static final int MAX_JUMP_TICKS = 10;

	private boolean onGround;
	private int jumpTicks;
	private boolean jumpingLastTick;

	private float mx;
	private boolean jumping;
	private float down;

	public BasicWalkingEntity(HitBox hitBox) {
		super(hitBox);

		mx = 0;
		jumping = false;
		down = 0;

		jumpTicks = 0;
		jumpingLastTick = false;
		onGround = false;
	}

	@Override
	public void update(Game game) {
		test = game;
		vx = mx * SPEED;
		if (-vy < MAX_GRAVITY_SPEED) vy -= GRAVITY_ACCELERATION;

		if (((onGround && !jumpingLastTick) || (jumpTicks < MAX_JUMP_TICKS && jumpTicks > 0)) && jumping) {
			vy = JUMP_ACCELERATION;
			jumpTicks++;
		} else {
			jumpTicks = 0;
		}

		jumpingLastTick = jumping;

		vy -= down * DOWN_ACCELERATION;

		lastTickOnGround = onGround;
		onGround = false;
		super.update(game);
	}

	//TEST
	private boolean lastTickOnGround;
	private Game test;


	@Override
	public void collide(GameObject gameObject, HitBox.HitBoxDirection direction) {
		if (direction == HitBox.HitBoxDirection.DOWN) {
			if (!lastTickOnGround && down > 0) {
				test.getCamera().addScreenshake(0.03f);
			}

			onGround = true;
		}
	}

	public void setMx(float mx) {
		this.mx = MathUtil.clamp(mx, -1, 1);
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public void setDown(float down) {
		this.down = MathUtil.clamp(down, 0, 1);
	}
}
