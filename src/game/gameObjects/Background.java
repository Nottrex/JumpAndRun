package game.gameObjects;

import game.HitBox;
import game.Sprite;
import game.gameObjects.entities.BasicMovingEntity;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Background extends StaticDraw {
	private List<Pair<HitBox, String>> hitBoxList;

	public Background() {
		hitBoxList = new ArrayList<>();


		for (int x = -10; x <= 10; x++) {
			for (int y = -10; y <= 10; y++) {
				hitBoxList.add(new Pair<>(new HitBox(x, y, 1, 1), "background"));
			}
		}
		super.updateContent(hitBoxList);

	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update() {

	}

	@Override
	public float getDrawingPriority() {
		return 5;
	}
}
