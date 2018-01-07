package game.util;

import game.data.HitBox;
import javafx.util.Pair;

import java.util.*;
import java.io.*;
import java.util.List;

public class MapLoader {


	public static List<Pair<HitBox, String>> load() {
		Map<Integer,String> textureDef = new HashMap<>();
		List<Pair<HitBox, String>> hitBoxList = new ArrayList<>();
		File mapFile = new File("tutorial_1.map");

		try {
			Scanner fileScanner = new Scanner(mapFile);
			String line = fileScanner.nextLine();
			while (line.contains("#")) {
				int key = Integer.parseInt(line.substring(1,line.indexOf(" ")));
				String textureName = line.substring(line.indexOf("_")+1);
				textureDef.put(key, textureName);
				System.out.println(key);
				line = fileScanner.nextLine();
			}
			line = fileScanner.nextLine(); //TO-DO Fore- and Background
			line = fileScanner.nextLine();

			Scanner layerScanner = new Scanner(line);
			layerScanner.next();
			float z = Float.parseFloat(layerScanner.next().replace(";",""));
			int x = Integer.parseInt(layerScanner.next().replace(";",""));
			int y = Integer.parseInt(layerScanner.next().replace(";",""));

			for (int i = 0; i<x; i++) {
				for (int j = 0; j<y; j++) {
					int tile = Integer.parseInt(layerScanner.next().replace(",","").replace(";", "").replace("]", ""));
					if (tile != 0) {hitBoxList.add(new Pair<>(new HitBox(-i, -j, 1, 1), textureDef.get(tile)));}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return hitBoxList;
	}
}
