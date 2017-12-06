package game.gameObjects.entities;

import game.gameObjects.GameObject;
import game.HitBox;

public class Player extends SimpleMovementEntity {

	public Player() {
		super(new HitBox(0,0,1,1));
	}

	@Override
	public void collide(GameObject gameObject) {

	}

	@Override
	public float getDrawingPriority() {
		return 1;
	}
}
