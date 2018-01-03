package game.gameobjects.gameobjects.wall;

import game.Game;
import game.data.HitBox;
import game.data.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.GameObject;
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
		hitBoxList.add(new Pair<>(new HitBox(1, -4, 1, 1), "wall"));
		hitBoxList.add(new Pair<>(new HitBox(1, -3, 1, 1), "wall"));
		hitBoxList.add(new Pair<>(new HitBox(1, -2, 1, 1), "wall"));
		super.updateContent(hitBoxList);

		hitBoxes = hitBoxList.stream().map(Pair::getKey).collect(Collectors.toList());
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void collide(GameObject gameObject, HitBoxDirection direction, float velocity) {

	}

	@Override
	public void update(Game game) {

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
