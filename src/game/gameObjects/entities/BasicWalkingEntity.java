package game.gameObjects.entities;

import game.HitBox;
import game.gameObjects.GameObject;
import game.util.MathUtil;

public abstract class BasicWalkingEntity extends BasicMovingEntity {
	private static final float SPEED = 0.05f;
	private static final float JUMP_ACCELERATION = 0.3f;
	private static final float GRAVITY_ACCELERATION = 0.01f;

	private boolean onGround;

	private float mx;
	private float jumping;

	public BasicWalkingEntity(HitBox hitBox) {
		super(hitBox);
		mx = 0;
		jumping = 0;
		onGround = false;
	}

	@Override
	public void update() {
		vx = mx * SPEED;
		vy -= GRAVITY_ACCELERATION;
		if (onGround) vy += jumping * JUMP_ACCELERATION;

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
}
