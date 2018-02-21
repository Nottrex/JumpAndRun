package game.gameobjects.gameobjects.entities.entities;


import game.Game;
import game.audio.AudioHandler;
import game.audio.AudioPlayer;
import game.audio.Sound;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class Piano extends BasicStaticEntity {

	private static Sprite idle = new Sprite(100, "piano");

	public Piano(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 2, 2), drawingPriority);

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
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		if (gameObject instanceof Player) {
			game.getAudioPlayer().playAudio(Sound.EXPLOSION.fileName);
		}
	}
}
