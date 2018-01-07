package game;

import game.util.ErrorUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Options {

	public static Map<String, Integer> controls = new HashMap<>();

	static {
		new File(Constants.DATA_PATH).mkdirs();
	}

	public static void save() {
		DumperOptions op = new DumperOptions();
		op.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(op);

		Map<String, Object> data = new HashMap<>();
		data.put("CONTROLS", controls);

		try {
			yaml.dump(data, new FileWriter(new File(Constants.DATA_FILE_PATH)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load() {
		DumperOptions op = new DumperOptions();
		op.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(op);
		if (new File(Constants.DATA_FILE_PATH).exists()) {
			try {
				Map<String, Object> data = (Map<String, Object>) yaml.load(new FileInputStream(new File(Constants.DATA_FILE_PATH)));

				if (data.containsKey("CONTROLS")) {
					controls = (Map<String, Integer>) data.get("CONTROLS");}
				else {
					controls = Constants.DEFAULT_CONTROLS;}
			} catch (FileNotFoundException e) {
				ErrorUtil.printError("Loading options: " + e.toString());
			}
		} else {
			controls = Constants.DEFAULT_CONTROLS;
		}
	}
}
