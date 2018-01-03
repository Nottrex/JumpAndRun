package game;

import game.window.Keyboard;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Options {
	public static final String DATA_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator;
	public static final String DATA_FILE_PATH = DATA_PATH + "options.yml";

	public static Map<String, Integer> CONTROLS = new HashMap<>();

	static {
		new File(DATA_PATH).mkdirs();
	}

	public static void save() {
		DumperOptions op = new DumperOptions();
		op.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(op);

		Map<String, Object> data = new HashMap<>();
		data.put("CONTROLS", CONTROLS);

		try {
			yaml.dump(data, new FileWriter(new File(DATA_FILE_PATH)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load() {
		DumperOptions op = new DumperOptions();
		op.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(op);
		if (new File(DATA_FILE_PATH).exists()) {
			try {
				Map<String, Object> data = (Map<String, Object>) yaml.load(new FileInputStream(new File(DATA_FILE_PATH)));

				CONTROLS = (Map<String, Integer>) data.get("CONTROLS");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			CONTROLS.put("UP", Keyboard.KEY_W);
			CONTROLS.put("DOWN", Keyboard.KEY_S);
			CONTROLS.put("LEFT", Keyboard.KEY_A);
			CONTROLS.put("RIGHT", Keyboard.KEY_D);
			CONTROLS.put("SHAKE", Keyboard.KEY_V);
			CONTROLS.put("PARTICLE", Keyboard.KEY_X);
		}
	}
}
