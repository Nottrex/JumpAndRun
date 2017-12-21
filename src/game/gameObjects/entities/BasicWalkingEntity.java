package game.gameObjects.entities;

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

	private int lastOnGround;

	private float mx;
	private float jumping;
	private float down;

	public BasicWalkingEntity(HitBox hitBox) {
		super(hitBox);
		mx = 0;
		jumping = 0;
		down = 0;
		lastOnGround = 0;
	}

	@Override
	public void update() {
		vx = mx * SPEED;
		if (-vy < MAX_GRAVITY_SPEED) vy -= GRAVITY_ACCELERATION;
		if (lastOnGround < MAX_JUMP_TICKS && jumping > 0.5f) vy = jumping * JUMP_ACCELERATION;
		vy -= down * DOWN_ACCELERATION;

		lastOnGround++;
		super.update();
	}

	@Override
	public void collide(GameObject gameObject, HitBox.HitBoxDirection direction) {
		if (direction == HitBox.HitBoxDirection.DOWN) {
			lastOnGround = 0;
		}
	}

	public void setMx(float mx) {
		this.mx = MathUtil.clamp(mx, -1, 1);
	}

	public void setJumping(float jumping) {
		this.jumping = MathUtil.clamp(jumping, 0, 1);
	}

	public void setDown(float down) {
		this.down = MathUtil.clamp(down, 0, 1);
	}
}
