package game.util;

public class TimeUtil {

	/**
	 * @return the current time in milli seconds
	 */
	public static long getTime() {
		return System.currentTimeMillis();
	}

	/**
	 * waits the given amount of time
	 *
	 * @param time the time in milli seconds
	 */
	public static void sleep(int time) {
		try {
			Thread.sleep(Math.max(0, time));
		} catch (Exception e) {
			ErrorUtil.printError("Error while sleeping ???");
		}
	}
}
