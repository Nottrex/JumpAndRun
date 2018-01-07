package game.gameobjects.gameobjects.wall;

import game.Game;
import game.data.HitBox;
import game.data.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.GameObject;
import game.util.MapLoader;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Wall extends StaticDraw implements CollisionObject {
	private List<Pair<HitBox, String>> hitBoxList;
	private List<HitBox> hitBoxes;

	public Wall() {
		hitBoxList = MapLoader.load();
		super.updateContent(hitBoxList);

		hitBoxes = hitBoxList.stream().map(Pair::getKey).collect(Collectors.toList());
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity) {

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
