package utilities.selenium;

import org.openqa.selenium.Platform;

public class ExecutionPlatform {
	private ExecutionPlatform() {}
	
	private static Platform platform;
	private static String runPlaformType;

	public static Platform getCurrentPlatform() {
		if (platform == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				platform = Platform.WINDOWS;
			} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
				platform = Platform.LINUX;
			} else if (operSys.contains("mac")) {
				platform = Platform.MAC;
			}
		}
		return platform;
	}
	
	public static void setPlaformType(String runtype) {
		runPlaformType = runtype;
	}
	
	public static String getRunPlaformType() {
		return runPlaformType;
	}
}
