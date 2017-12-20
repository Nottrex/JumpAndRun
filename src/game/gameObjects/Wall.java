package game.gameObjects;

import game.HitBox;
import game.Sprite;
import game.gameObjects.entities.BasicMovingEntity;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Wall extends StaticDraw implements CollisionObject {
	private List<Pair<HitBox, String>> hitBoxList;
	private List<HitBox> hitBoxes;

	public Wall() {
		hitBoxList = new ArrayList<>();


		for (int i = -8; i <= 8; i++) {
			hitBoxList.add(new Pair<>(new HitBox(i, -5, 1, 1), "wall"));
		}

		hitBoxList.add(new Pair<>(new HitBox(2, -4, 1, 1), "wall"));
		hitBoxList.add(new Pair<>(new HitBox(2, -3, 1, 1), "wall"));
		super.updateContent(hitBoxList);

		hitBoxes = hitBoxList.stream().map(Pair::getKey).collect(Collectors.toList());
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void collide(GameObject gameObject, HitBox.HitBoxDirection direction) {

	}

	@Override
	public void update() {

	}

	@Override
	public float getDrawingPriority() {
		return 0;
	}

	@Override
	public List<HitBox> getCollisionBoxes() {
		return hitBoxes;
	}
}
