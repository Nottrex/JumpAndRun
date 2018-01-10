package game.util;

import game.data.HitBox;
import game.GameMap;
import game.gameobjects.gameobjects.entities.entities.Coin;
import game.gameobjects.gameobjects.wall.Background;
import game.gameobjects.gameobjects.wall.Wall;
import javafx.util.Pair;

import java.util.*;
import java.io.*;
import java.util.List;

public class MapLoader {


	public static GameMap load(String mapName) {
		GameMap map = new GameMap();
		Map<Integer,String> textureDef = new HashMap<>();

		File mapFile = new File(mapName);
		try {
			Scanner fileScanner = new Scanner(mapFile);
			String line = fileScanner.nextLine();
			while (line.contains("#")) {
				int key = Integer.parseInt(line.substring(1,line.indexOf(" ")));
				String textureName = line.substring(line.indexOf("_")+1);
				textureDef.put(key, textureName);
				line = fileScanner.nextLine();
			}
			while (line.contains("layer")){
				List<Pair<HitBox, String>> hitBoxList = new ArrayList<>();
				Scanner layerScanner = new Scanner(line);
				layerScanner.next();
				float z = Float.parseFloat(layerScanner.next().replace(";",""));
				int x = Integer.parseInt(layerScanner.next().replace(";",""));
				int y = Integer.parseInt(layerScanner.next().replace(";",""));

				for (int i = 0; i<x; i++) {
					for (int j = 0; j<y; j++) {
						String a = layerScanner.next();
						int tile = Integer.parseInt(a.substring(0, a.length()-1));
						if (tile != 0) {hitBoxList.add(new Pair<>(new HitBox(i, -j, 1, 1), textureDef.get(tile)));}
					}
				}
				if (z <= 0.55 && z >= 0.45) map.addObject(new Wall(hitBoxList, z));
				else map.addObject(new Background(hitBoxList, z));
				line = fileScanner.nextLine();
			}
			while (line.contains("put")) {
				Scanner putScanner = new Scanner(line);
				putScanner.next();
				float z = Float.parseFloat(putScanner.next().replace(";", ""));
				int type = Integer.parseInt(putScanner.next().replace(";", ""));
				float x = Float.parseFloat(putScanner.next().replace(";", ""));
				float y = Float.parseFloat(putScanner.next().replace("]", ""));
				if (textureDef.get(type).contains("player")) map.setSpawnpoint(x, -y);
				if (textureDef.get(type).contains("coin")) map.addObject(new Coin(x, -y));
				line = fileScanner.nextLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}
}
