package game.gameObjects.entities;

import game.HitBox;
import game.gameObjects.GameObject;
import game.util.MathUtil;

public abstract class BasicWalkingEntity extends BasicMovingEntity {
	private static final float SPEED = 0.175f;
	private static final float JUMP_ACCELERATION = 0.3f;
	private static final float DOWN_ACCELERATION = 0.1f;
	private static final float GRAVITY_ACCELERATION = 0.015f;
	private static final float MAX_GRAVITY_SPEED = 0.15f;

	private boolean onGround;

	private float mx;
	private float jumping;
	private float down;

	public BasicWalkingEntity(HitBox hitBox) {
		super(hitBox);
		mx = 0;
		jumping = 0;
		down = 0;
		onGround = false;
	}

	@Override
	public void update() {
		vx = mx * SPEED;
		if (-vy < MAX_GRAVITY_SPEED) vy -= GRAVITY_ACCELERATION;
		if (onGround && jumping > 0.5f) vy += jumping * JUMP_ACCELERATION;
		if (!onGround) vy -= down * DOWN_ACCELERATION;

		onGround = false;
		super.update();
	}

	@Override
	public void collide(GameObject gameObject, HitBox.HitBoxDirection direction) {
		if (direction == HitBox.HitBoxDirection.DOWN) {
			onGround = true;
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
