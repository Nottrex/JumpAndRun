package game.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHandler {

	public static String loadFile(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("res/files/" + fileName)));

			StringBuilder source = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				source.append(line).append("\n");
			}
			reader.close();

			return source.toString();
		} catch (Exception e) {
			ErrorUtil.printError(String.format("Loading file: %s", fileName));
		}
		return null;
	}
}
