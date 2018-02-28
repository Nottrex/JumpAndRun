package game.gameobjects.gameobjects.entities.entities;


import game.Game;
import game.audio.AudioHandler;
import game.audio.AudioPlayer;
import game.audio.Sound;
import game.audio.Source;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

import java.util.Random;

public class Piano extends BasicStaticEntity {
	private static final int[] JINGLES = new int[] {3*60, 3*60, 13*60 };
	private static Sprite idle = new Sprite(100, "piano");

	private Source source;

	private int waitTil = -1;

	public Piano(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 2, 2), drawingPriority);
		this.source = new Source();
		setSprite(idle);
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public float getCollisionPriority() {
		return 0;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		if (gameObject instanceof Player && game.getGameTick() > waitTil) {
			int jingle = new Random().nextInt(JINGLES.length);

			waitTil = game.getGameTick() + JINGLES[jingle] + 60;
			source.play(AudioHandler.getMusicWav("piano\" + jingle"));
		}
	}
}
