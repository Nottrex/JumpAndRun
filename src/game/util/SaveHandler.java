package game.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SaveHandler {
	private static String saveDirectory = System.getProperty("user.dir") + File.separator + "saves";

	static {
		new File(saveDirectory).mkdir();
	}

	public static Map<Integer, String> getSaves() {
		File[] listOfFiles = new File(saveDirectory).listFiles();

		Map<Integer, String> saves = new HashMap<>();

		for (File f : listOfFiles) {
			if (f.isFile() && f.getName().endsWith(".save")) {
				saves.put(Integer.valueOf(f.getName().split("-")[0]), f.getName().split("-")[1].replaceAll(".save", ""));
			}
		}
		return saves;
	}

	public static Map<String, Integer> readSave(String saveName) {
		Map<String, Integer> values = new HashMap<>();

		try {
			Scanner scanner = new Scanner(new File(saveDirectory + File.separator + saveName + ".save"));
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split("~");
				values.put(line[0], Integer.parseInt(line[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	public static void writeSave(Map<String, Integer> values, String saveName) {
		try {
			PrintWriter writer = new PrintWriter(new File(saveDirectory + File.separator + saveName + ".save"));
			for (String key: values.keySet()) {
				writer.println(key + "~" + values.get(key));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
