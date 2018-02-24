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
	protected HitBoxDirection onWall;
	protected int jumpTicks;
	private boolean jumpingLastTick;
	private boolean hasDoubleJumped;

	protected float lastMX;
	protected float mx;
	protected boolean jumping;
	protected boolean down;

	private float maxSpeed = 1;
	private float maxJumpHeight = 1;

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
		vx = mx * Constants.MAX_WALKING_SPEED * maxSpeed;
		if(Math.abs(mx) >= 0.2f) lastMX = mx;

		if ((onGround && !jumpingLastTick) && jumping) {
			vy = Constants.JUMP_ACCELERATION * maxJumpHeight;
			jumpTicks = 1;
		} else if ((onWall == HitBoxDirection.RIGHT || onWall == HitBoxDirection.LEFT) && !jumpingLastTick && jumping && (this instanceof Player && ((Player) this).hasAbility(Ability.WALL_JUMP))) {
			jumpTicks = 1;
			vy = Constants.JUMP_ACCELERATION * maxJumpHeight;
			this.addKnockBack(- onWall.getXDirection() * 0.27f, 0);
			hasDoubleJumped = false;
		} else if (jumpTicks == 0 && !jumpingLastTick && jumping && !hasDoubleJumped && (this instanceof Player && ((Player) this).hasAbility(Ability.DOUBLE_JUMP))) {
			jumpTicks = 1;
			vy = Constants.JUMP_ACCELERATION * maxJumpHeight;
			game.getParticleSystem().createParticle(ParticleType.EXPLOSION, hitBox.getCenterX(), hitBox.y, 0, -0.02f);
			hasDoubleJumped = true;
		} else if (jumpTicks > 0 && jumping && vy > 0) {
			jumpTicks++;

			vy -= Constants.GRAVITY_ACCELERATION_JUMPING;
		} else {
			jumpTicks = 0;

			if (-vy < Constants.MAX_GRAVITY_SPEED) vy = Math.max(vy - Constants.GRAVITY_ACCELERATION, -Constants.MAX_GRAVITY_SPEED);
			if ((this instanceof Player && ((Player) this).hasAbility(Ability.STOMP)) && down && -vy < Constants.MAX_DOWN_SPEED) vy = Math.max(vy - Constants.DOWN_ACCELERATION, -Constants.MAX_DOWN_SPEED);
		}

		if (onLadder) {
			vy = (jumping? 0.1f: -0.1f);
		}

		jumpingLastTick = jumping;

		onGround = false;
		onLadder = false;
		onWall = HitBoxDirection.COLLIDE;
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
			hasDoubleJumped = false;
		}

		if ((direction == HitBoxDirection.LEFT || direction == HitBoxDirection.RIGHT) && velocity != 0) {
			onWall = direction;
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

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public void setMaxJumpHeight(float maxJumpHeight) {
		this.maxJumpHeight = maxJumpHeight;
	}

	@Override
	protected boolean fallThroughBlock() {
		return down;
	}
}
