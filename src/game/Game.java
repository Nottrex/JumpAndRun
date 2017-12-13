package game;

import game.gameObjects.CollisionObject;
import game.gameObjects.Drawable;
import game.gameObjects.GameObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Game {
	private boolean running = true;

	private List<GameObject> gameObjects;
	private List<CollisionObject> collisionObjects;
	private List<Drawable> drawables;

	private Queue<GameObject> toRemove;
	private Queue<GameObject> toAdd;

	private Game() {
		gameObjects = new LinkedList<>();
		collisionObjects = new LinkedList<>();
		drawables = new LinkedList<>();

		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		gameLoop();
	}

	private void gameLoop() {
		while (running) {
			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();
				gameObjects.add(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.add((CollisionObject) gameObject);
				if (gameObject instanceof Drawable) drawables.add((Drawable) gameObject);
			}

			while (!toRemove.isEmpty()) {
				GameObject gameObject = toRemove.poll();
				if (gameObject instanceof CollisionObject) collisionObjects.remove(gameObject);
				if (gameObject instanceof Drawable) drawables.remove(gameObject);
			}

			gameObjects.sort((o1, o2) -> (int) Math.signum(o2.getPriority()-o1.getPriority()));

			for (GameObject gameObject: gameObjects) {
				gameObject.update();
			}

			//TODO: WAIT FOR TICK TIME
		}
	}

	public void addGameObject(GameObject gameObject) {
		toAdd.add(gameObject);
	}

	public void removeGameObject(GameObject gameObject) {
		toRemove.add(gameObject);
	}

	public List<CollisionObject> getCollisionObjects() {
		return collisionObjects;
	}

	private static Game INSTANCE;
	public static Game getInstance() {
		if (INSTANCE == null) {
			synchronized (Game.class) {
				if (INSTANCE == null) INSTANCE = new Game();
			}
		}
		return INSTANCE;
	}
}
