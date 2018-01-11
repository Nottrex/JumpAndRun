package game;

import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.CameraController;
import game.util.MapLoader;
import game.window.Drawable;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.entities.entities.Player;
import game.gameobjects.gameobjects.particle.ParticleSystem;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.util.TimeUtil;
import game.window.Camera;
import game.window.Keyboard;
import game.window.Window;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
	private Window window;

	private int gameTick;

	private List<GameObject> gameObjects;
	private List<CollisionObject> collisionObjects;
	private List<Player> players;
	private List<Integer> inputs;
	private GameMap map;

	private GameMap newMap;
	private Queue<GameObject> toRemove;
	private Queue<GameObject> toAdd;

	private ParticleSystem particleSystem;

	public Game(Window window) {
		this.window = window;

		gameTick = 0;

		players = new ArrayList<>();
		inputs = new ArrayList<>();
		gameObjects = new LinkedList<>();
		collisionObjects = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		setGameMap("test2");
	}

	public void gameLoop() {
		long time;
		while (window.isRunning()) {
			gameTick++;
			time = TimeUtil.getTime();

			handleInput();

			if (newMap != null) {
				if (map != null) {
					for (GameObject gameObject: map.getGameObjects()) {
						this.removeGameObject(gameObject);
					}
				}

				for (GameObject gameObject: newMap.getGameObjects()) {
					this.addGameObject(gameObject);
				}

				for (Player player: players) {
					player.respawn(newMap.getSpawnX(), newMap.getSpawnY(), newMap.getPlayerDrawingPriority());
				}

				map = newMap;
				newMap = null;
			}

			while (!toRemove.isEmpty()) {
				GameObject gameObject = toRemove.poll();

				gameObjects.remove(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.remove(gameObject);
				if (gameObject instanceof Drawable) window.removeDrawable((Drawable) gameObject);
				if (gameObject instanceof ParticleSystem) particleSystem = null;
				if (gameObject instanceof Player) {
					int id = players.indexOf((Player) gameObject);
					players.remove(id);
					inputs.remove(id);
				}
			}

			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();

				gameObject.init(this);

				gameObjects.add(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.add((CollisionObject) gameObject);
				if (gameObject instanceof Drawable) window.addDrawable((Drawable) gameObject);
				if (gameObject instanceof ParticleSystem) particleSystem = (ParticleSystem) gameObject;
				if (gameObject instanceof Player) players.add((Player) gameObject);
			}

			gameObjects.sort((o1, o2) -> Float.compare(o2.getPriority(), o1.getPriority()));

			for (GameObject gameObject : gameObjects) {
				gameObject.update(this);
			}

			long newTime = TimeUtil.getTime();

			TimeUtil.sleep((int) (1000.0f / Constants.TPS - (newTime - time)));
		}

		cleanUp();
	}

	private void handleInput() {
		Keyboard keyboard = window.getKeyboard();

		for (int i = 0; i < 18; i++) {
			if (keyboard.isPressed(Options.controls.get("UP"+i)) && !inputs.contains(i)) {
				Player newPlayer = new Player(map.getSpawnX(), map.getSpawnY(), map.getPlayerDrawingPriority());
				this.addGameObject(newPlayer);
				inputs.add(i);
			}
		}
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			int input = inputs.get(i);

			player.setJumping(keyboard.isPressed(Options.controls.get("UP" + input)));
			player.setMx(keyboard.getPressed(Options.controls.get("RIGHT" + input)) - keyboard.getPressed(Options.controls.get("LEFT" + input)));
			player.setDown(keyboard.isPressed(Options.controls.get("DOWN" + input)));
		}

		if(keyboard.isPressed(Keyboard.GAMEPAD_1_BUTTON_Y)) {
			getCamera().setRotationSmooth((float) Math.random() * 2 * (float) Math.PI, 200);
		}
	}

	private void cleanUp() {
		Options.save();
	}

	public void setGameMap(String name) {
		if (newMap == null) {
			newMap = MapLoader.load(name);
		}
	}

	public void addGameObject(GameObject gameObject) {
		if (!toAdd.contains(gameObject) && !gameObjects.contains(gameObject)) toAdd.add(gameObject);
	}

	public void removeGameObject(GameObject gameObject) {
		if (!toRemove.contains(gameObject) && gameObjects.contains(gameObject)) toRemove.add(gameObject);
	}

	public List<CollisionObject> getCollisionObjects() {
		return collisionObjects;
	}

	public Camera getCamera() {
		return window.getCamera();
	}

	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getGameTick() {
		return gameTick;
	}
}
