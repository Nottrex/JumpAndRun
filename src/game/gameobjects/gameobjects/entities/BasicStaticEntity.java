package game.gameobjects.gameobjects.entities;

import game.data.HitBox;
import game.gameobjects.CollisionObject;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicStaticEntity extends BasicDrawingEntity implements CollisionObject{

	private List<HitBox> hitBoxes;

	public BasicStaticEntity(HitBox hitBox, float drawingPriority) {
		super(hitBox, drawingPriority);

		hitBoxes = new ArrayList<>();
		hitBoxes.add(hitBox);
	}

	@Override
	public List<HitBox> getCollisionBoxes() {
		return hitBoxes;
	}
}
