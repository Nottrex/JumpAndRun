package game;

import game.gameObjects.CollisionObject;
import game.gameObjects.Drawable;
import game.gameObjects.GameObject;
import game.gameObjects.Wall;
import game.gameObjects.entities.Player;
import game.util.TimeUtil;
import game.window.Keyboard;
import game.window.Window;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Game {
	public static final int TPS = 60;

	private List<GameObject> gameObjects;
	private List<CollisionObject> collisionObjects;

	private Queue<GameObject> toRemove;
	private Queue<GameObject> toAdd;

	private Player player;

	private Game() {
		gameObjects = new LinkedList<>();
		collisionObjects = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		player = new Player();

		this.addGameObject(new Wall());
		this.addGameObject(player);
	}

	public void gameLoop() {
		long time = TimeUtil.getTime();
		while (Window.getInstance().isRunning()) {
			Keyboard keyboard = Window.getInstance().getKeyboard();

			player.setJumping(keyboard.getPressed(Keyboard.GAMEPAD_BUTTON_A));
			player.setMx(keyboard.getPressed(Keyboard.GAMEPAD_AXIS_LEFT_X_RIGHT) - keyboard.getPressed(Keyboard.GAMEPAD_AXIS_LEFT_X_LEFT));
			if (keyboard.isPressed(Keyboard.GAMEPAD_BUTTON_B)) Window.getInstance().getCamera().addScreenshake(0.03f);

			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();
				gameObjects.add(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.add((CollisionObject) gameObject);
				if (gameObject instanceof Drawable) Window.getInstance().addDrawable((Drawable) gameObject);
			}

			while (!toRemove.isEmpty()) {
				GameObject gameObject = toRemove.poll();
				if (gameObject instanceof CollisionObject) collisionObjects.remove(gameObject);
				if (gameObject instanceof Drawable) Window.getInstance().removeDrawable((Drawable) gameObject);
			}

			gameObjects.sort((o1, o2) -> Float.compare(o2.getPriority(),o1.getPriority()));

			for (GameObject gameObject: gameObjects) {
				gameObject.update();
			}

			long newTime = TimeUtil.getTime();
			TimeUtil.sleep((int) (1000.0f/TPS - (newTime-time)));
			time = newTime;
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
