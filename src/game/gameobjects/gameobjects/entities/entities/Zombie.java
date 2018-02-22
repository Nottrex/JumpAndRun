package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;

import java.awt.*;
import java.util.Optional;

public class Zombie extends BasicWalkingEntity {
	private static Sprite walking_r = new Sprite(250, "zombie_r_move_0", "zombie_r_move_1", "zombie_r_move_2", "zombie_r_move_3");
	private static Sprite idle_r = new Sprite(250, "zombie_r_idle_0");
	private static Sprite falling_r = new Sprite(250, "zombie_r_fall");
	private static Sprite walking_l = new Sprite(250, "zombie_l_move_0", "zombie_l_move_1", "zombie_l_move_2", "zombie_l_move_3");
	private static Sprite idle_l = new Sprite(250, "zombie_l_idle_0");
	private static Sprite falling_l = new Sprite(250, "zombie_l_fall");

	public Zombie(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 0.5f, 0.875f), drawingPriority);

		setSprite(idle_r);

		setMaxSpeed(0.15f);
		setMaxJumpHeight(0.75f);
	}

	@Override
	public void update(Game game) {
		super.update(game);

		Sprite newSprite = null;
		if(!onGround && mx != 0) newSprite = (mx < 0? falling_l: falling_r);
		if(!onGround && mx == 0) newSprite = (lastMX < 0? falling_l: falling_r);
		if(onGround && mx == 0) newSprite = (lastMX < 0? idle_l: idle_r);
		if(onGround && mx != 0) newSprite = (mx < 0? walking_l: walking_r);

		if(!sprite.equals(newSprite)) setSprite(newSprite);


		Optional<Player> nearestPlayer = game.getPlayers().stream().sorted((p1, p2) -> Float.compare(hitBox.distance(p1.getHitBox()), hitBox.distance(p2.getHitBox()))).findFirst();

		if (nearestPlayer.isPresent()) {
			Player p = nearestPlayer.get();
			setDown(hitBox.getCenterY() > p.getHitBox().getCenterY());
			setJumping((game.getGameTick() % 60 == 0 || jumpTicks > 0) && hitBox.getCenterY() < p.getHitBox().getCenterY() - 1);
			setMx(p.getHitBox().getCenterX() - hitBox.getCenterX());
		} else {
			setDown(false);
			setJumping(false);
			setMx((game.getGameTick() % 121)-60);
		}
	}

	@Override
	public void remove(Game game, boolean mapChange) {
		super.remove(game, mapChange);

		if (!mapChange) {
			if (game.getDeadBodyHandler() != null) game.getDeadBodyHandler().addDeadBody((new DeadBody(getHitBox().x, getHitBox().y, "zombie", Color.BLACK, lastMX > 0)));
		}
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public float getCollisionPriority() {
		return -3;
	}

}
