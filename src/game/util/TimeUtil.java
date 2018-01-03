package game.util;

public class TimeUtil {

	public static long getTime() {
		return System.currentTimeMillis();
	}

	public static void sleep(int time) {
		try {
			Thread.sleep(Math.max(0, time));
		} catch (Exception e) {
			ErrorUtil.printError("Error while sleeping ???");
		}
	}
}
