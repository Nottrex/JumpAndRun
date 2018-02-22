package game.gameobjects.gameobjects.entities;

import game.Ability;
import game.Constants;
import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.entities.Ladder;
import game.gameobjects.gameobjects.entities.entities.Player;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.util.MathUtil;

public abstract class BasicWalkingEntity extends BasicMovingEntity {
	protected boolean onGround, onLadder;
	private int jumpTicks;
	private boolean jumpingLastTick;

	protected float lastMX;
	protected float mx;
	protected boolean jumping;
	protected boolean down;

	public BasicWalkingEntity(HitBox hitBox, float drawingPriority) {
		super(hitBox, drawingPriority);

		lastMX = -1;
		mx = 0;
		jumping = false;
		down = false;
		jumpingLastTick = false;

		jumpTicks = 0;
		onGround = false;
		onLadder = false;
	}

	@Override
	public void update(Game game) {
		vx = mx * Constants.MAX_WALKING_SPEED;
		if(Math.abs(mx) >= 0.2f) lastMX = mx;

		if ((onGround && !jumpingLastTick) && jumping) {
			vy = Constants.JUMP_ACCELERATION;
			jumpTicks = 1;
		} else if (jumpTicks > 0 && jumping && vy > 0) {
			jumpTicks++;

			vy -= Constants.GRAVITY_ACCELERATION_JUMPING;
		} else {
			jumpTicks = 0;

			if (-vy < Constants.MAX_GRAVITY_SPEED) vy = Math.max(vy - Constants.GRAVITY_ACCELERATION, -Constants.MAX_GRAVITY_SPEED);
			if (this instanceof Player && ((Player) this).hasAbility(Ability.STOMP) && down && -vy < Constants.MAX_DOWN_SPEED) vy = Math.max(vy - Constants.DOWN_ACCELERATION, -Constants.MAX_DOWN_SPEED);
		}

		if (onLadder) {
			vy = (jumping? 0.1f: -0.1f);
		}

		jumpingLastTick = jumping;

		onGround = false;
		onLadder = false;
		super.update(game);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		super.collide(gameObject, direction, velocity, source);

		if (direction == HitBoxDirection.DOWN && velocity != 0) {
			if (velocity > Constants.MAX_GRAVITY_SPEED + 0.01f) {
				game.getCamera().addScreenshake(velocity / 15);

				game.getParticleSystem().createParticle(ParticleType.EXPLOSION, hitBox.getCenterX(), hitBox.y, 0, 0);
			}

			onGround = true;
		}

		if (gameObject instanceof Ladder) {
			onLadder = true;
		}
	}

	public void setMx(float mx) {
		this.mx = MathUtil.clamp(mx, -1, 1);
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	@Override
	protected boolean fallThroughBlock() {
		return down;
	}
}
