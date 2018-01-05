package game;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Options {
	public static final String DATA_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator;
	public static final String DATA_FILE_PATH = DATA_PATH + "options.yml";

	public static Map<String, Integer> PRIMARY_CONTROLS = new HashMap<>();
	public static Map<String, Integer> SECONDARY_CONTROLS = new HashMap<>();
	public static Map<String, Integer> CONTROLLER_CONTROLS = new HashMap<>();

	static {
		new File(DATA_PATH).mkdirs();
	}

	public static void save() {
		DumperOptions op = new DumperOptions();
		op.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(op);

		Map<String, Object> data = new HashMap<>();
		data.put("PRIMARY_CONTROLS", PRIMARY_CONTROLS);
		data.put("SECONDARY_CONTROLS", SECONDARY_CONTROLS);
		data.put("CONTROLLER_CONTROLS", CONTROLLER_CONTROLS);

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

				if (data.containsKey("PRIMARY_CONTROLS")) {PRIMARY_CONTROLS = (Map<String, Integer>) data.get("PRIMARY_CONTROLS");}
				else {PRIMARY_CONTROLS = Constants.PRIMARY_CONTROLS;}
				if (data.containsKey("SECONDARY_CONTROLS")) {SECONDARY_CONTROLS = (Map<String, Integer>) data.get("SECONDARY_CONTROLS");}
				else {SECONDARY_CONTROLS = Constants.SECONDARY_CONTROLS;}
				if (data.containsKey("CONTROLLER_CONTROLS")) {CONTROLLER_CONTROLS = (Map<String, Integer>) data.get("CONTROLLER_CONTROLS");}
				else {CONTROLLER_CONTROLS = Constants.CONTROLLER_CONTROLS;}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			PRIMARY_CONTROLS = Constants.PRIMARY_CONTROLS;
			SECONDARY_CONTROLS = Constants.SECONDARY_CONTROLS;
			CONTROLLER_CONTROLS = Constants.CONTROLLER_CONTROLS;
		}
	}
}
