package game.gameobjects.gameobjects.wall;

import game.Game;
import game.data.HitBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Background extends StaticDraw {
	private float priority;

	public Background(Map<HitBox, String> hitBoxList, float priority) {
		this.priority = priority;
		super.updateContent(hitBoxList);
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
