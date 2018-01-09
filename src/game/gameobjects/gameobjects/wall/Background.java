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

public class Background extends StaticDraw {
	private List<Pair<HitBox, String>> hitBoxList;
	private List<HitBox> hitBoxes;
	private float priority;

	public Background(List<Pair<HitBox, String>> hitBoxList, float priority) {
		this.hitBoxList = hitBoxList;
		this.priority = priority;
		super.updateContent(hitBoxList);

		hitBoxes = hitBoxList.stream().map(Pair::getKey).collect(Collectors.toList());
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public float getDrawingPriority() {
		return priority;
	}
}
