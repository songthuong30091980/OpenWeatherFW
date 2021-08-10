package utilities.loggers;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import configuration.ConfigConstant;
import configuration.ConfigReader;

public class Log {
	private Log() {}
	
	private static Logger logger = Logger.getLogger(Log.class.getName());

	public static void initLog() {
		PropertyConfigurator.configure(ConfigReader.getLog4jConfig());
	}

	public static void startLog(String testName) {
		logger.info("[" + testName + "] is Starting...");
	}

	public static void endLog(String testName) {
		logger.info("[" + testName + "] is Ending...");
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void warn(String message) {
		logger.warn(message);
	}

	public static void error(String message, String errorMessage) {
		logger.error(message + ConfigConstant.LINE_DOWN + errorMessage);
	}

	public static void fatal(String message) {
		logger.fatal(message);
	}

	public static void debug(String message) {
		logger.debug(message);
	}
	
}
