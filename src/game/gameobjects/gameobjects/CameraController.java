package game.gameobjects.gameobjects;

import game.Game;
import game.gameobjects.AbstractGameObject;
import game.gameobjects.gameobjects.entities.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class CameraController extends AbstractGameObject {
	private List<Area> cameraAreas;
	private float spawnX, spawnY;

	private float lastMinX, lastMinY, lastMaxX, lastMaxY;
	private boolean start;

	public CameraController() {
		cameraAreas = new ArrayList<>();

		spawnX = 0;
		spawnY = 0;

		lastMinX = 0;
		lastMinY = 0;
		lastMaxX = 0;
		lastMaxY = 0;

		start = true;
	}

	@Override
	public void update(Game game) {
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;

		List<Player> players = game.getPlayers();

		if (players.size() > 0) {
			for (Player p: players) {
				Area area = getAreaAt(p.getHitBox().getCenterX(), p.getHitBox().getCenterY());

				if (area == null) {
					if (minX > p.getHitBox().getCenterX())
						minX = p.getHitBox().getCenterX();
					if (minY > p.getHitBox().getCenterY())
						minY = p.getHitBox().getCenterY();

					if (maxX < p.getHitBox().getCenterX())
						maxX = p.getHitBox().getCenterX();
					if (maxY < p.getHitBox().getCenterY())
						maxY = p.getHitBox().getCenterY();
				} else {
					if (minX > area.getX1())
						minX = area.getX1();
					if (minY > area.getY1())
						minY = area.getY1();

					if (maxX < area.getX2())
						maxX = area.getX2();
					if (maxY < area.getY2())
						maxY = area.getY2();
				}
			}
		} else {
			Area area = getAreaAt(spawnX, spawnY);

			if (area == null) {
				if (minX > spawnX)
					minX = spawnX;
				if (minY > spawnY)
					minY = spawnY;

				if (maxX < spawnX)
					maxX = spawnX;
				if (maxY < spawnY)
					maxY = spawnY;
			} else {
				if (minX > area.getX1())
					minX = area.getX1();
				if (minY > area.getY1())
					minY = area.getY1();

				if (maxX < area.getX2())
					maxX = area.getX2();
				if (maxY < area.getY2())
					maxY = area.getY2();
			}
		}

		if (minX != lastMinX || minY != lastMinY || maxX != lastMaxX || maxY != lastMaxY) {
			if (start) {
				game.getCamera().setPosition((minX + maxX)/2.0f, (minY + maxY)/2.0f);
				start = false;
			} else {
				game.getCamera().setPositionSmooth((minX + maxX)/2.0f, (minY + maxY)/2.0f, 300);
			}

			lastMinX = minX;
			lastMinY = minY;
			lastMaxX = maxX;
			lastMaxY = maxY;
		}
	}

	private Area getAreaAt(float x, float y) {
		for (Area area: cameraAreas) {
			if (area.contains(x, y)) return area;
		}

		return null;
	}

	@Override
	public float getPriority() {
		return -1;
	}

	public void addCameraArea(Area area) {
		cameraAreas.add(area);
	}

	public void setSpawn(float x, float y) {
		this.spawnX = x;
		this.spawnY = y;
	}
}
