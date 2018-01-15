package game.gamemap;

import game.data.HitBox;
import game.gameobjects.gameobjects.entities.entities.Door;
import game.gameobjects.gameobjects.entities.entities.Lantern;
import game.gameobjects.gameobjects.wall.Wall;

import java.util.HashMap;
import java.util.Map;

public class GameLobby {
	private GameMap map;

	public GameLobby(String... mapNames) {
		map = new GameMap();
		for (int i = 0; i < mapNames.length; i++) {
			Map<HitBox, String> hitBoxList = new HashMap<>();
			hitBoxList.put(new HitBox(i * 9, 0, 1f, 1f), "block_stone_middle");
			hitBoxList.put(new HitBox(i * 9 + 1, 0, 1f, 1f), "block_stone_middle");
			hitBoxList.put(new HitBox(i * 9 + 2, 0, 1f, 1f), "block_stone_middle");
			hitBoxList.put(new HitBox(i * 9 + 3, 0, 1f, 1f), "block_stone_middle");
			hitBoxList.put(new HitBox(i * 9 + 4, 0, 1f, 1f), "block_stone_middle");
			hitBoxList.put(new HitBox(i * 9 + 5, 0, 1f, 1f), "block_stone_middle");
			hitBoxList.put(new HitBox(i * 9 + 6, 0, 1f, 1f), "block_stone_middle");
			hitBoxList.put(new HitBox(i * 9 + 7, 0, 1f, 1f), "block_stone_middle");
			hitBoxList.put(new HitBox(i * 9 + 8, 0, 1f, 1f), "block_stone_middle");
			hitBoxList.put(new HitBox(i * 9 + 3, 3, 1f, 1f), "block_wood_middle");
			hitBoxList.put(new HitBox(i * 9 + 4, 3, 1f, 1f), "block_wood_middle");
			hitBoxList.put(new HitBox(i * 9 + 5, 3, 1f, 1f), "block_wood_middle");
			map.addGameObject(new Wall(hitBoxList, 0.5f));
			map.addGameObject(new Door(i * 9 + 4, 4, 1f, mapNames[i]));
			map.addGameObject(new Lantern(i* 9 + 2, 1, 1f));
			map.addGameObject(new Lantern(i* 9 + 6, 1, 1f));
		}
		map.setSpawnPoint(0,1,0.5f);
	}

	public GameMap getMap() {
		return map;
	}

}
