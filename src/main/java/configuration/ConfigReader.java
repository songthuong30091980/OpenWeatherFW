package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.openqa.selenium.Platform;

import utilities.common.JavaUtils;
import utilities.datadriven.PropertiesReader;
import utilities.loggers.Log;
import utilities.selenium.ExecutionPlatform;

public class ConfigReader {
	private ConfigReader() {
	}

	public static String getProjectConfigFilePath() {
		Path configPath = Paths.get(ConfigConstant.CURRENT_DIR,"src","main","resources","configs");
		if (!Files.exists(configPath)) {
			JavaUtils.createDirectoryPath(configPath.toString());
		}
		return configPath.toString();
	}

	public static Properties getConfiguration() {
		Properties projectPro = PropertiesReader
				.loadPropertiesFile(getProjectConfigFilePath() + ConfigConstant.FILE_SEPARATOR + "configuration.properties");
		if (projectPro == null) {
			Log.info("[configuration.properties] does not exist!!! All configurations for Web App will be default");
		}
		return projectPro;
	}

	public static Properties getMobileConfig() {
		Properties projectPro = PropertiesReader
				.loadPropertiesFile(getProjectConfigFilePath() + ConfigConstant.FILE_SEPARATOR + "mobile.properties");
		if (projectPro == null) {
			Log.info("[mobile.properties] does not exist!!! All configurations for mobile app will be default");
		}
		return projectPro;
	}

	public static InputStream getLog4jConfig() {
		InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("configs" + ConfigConstant.FILE_SEPARATOR +"log4j.properties");
		if (inputStream == null) {
			try {
				inputStream = new FileInputStream(new File(getProjectConfigFilePath() + ConfigConstant.FILE_SEPARATOR +"log4j.properties"));
			} catch (FileNotFoundException e) {
				throw new AssertionError("[log4j.properties] does not exist!!!");
			}
			
		}
		return inputStream;
	}

	public static String getDriverPath() {
		String driverPath = null;
		
		String defaultDriverPath = null;
		try {
			if(ConfigConstant.DRIVER_FOLDER_PATH == null || ConfigConstant.DRIVER_FOLDER_PATH.isEmpty()) {
				defaultDriverPath = ConfigConstant.DRIVER_FOLDER;
			} else {
				defaultDriverPath = ConfigConstant.DRIVER_FOLDER_PATH;
				JavaUtils.createDirectoryPath(defaultDriverPath);
			}
		} catch(NullPointerException ex) {
			defaultDriverPath = ConfigConstant.DRIVER_FOLDER_PATH;
			JavaUtils.createDirectoryPath(defaultDriverPath);
		}

		Properties configFile = getConfiguration();
		Platform currentPlatform = ExecutionPlatform.getCurrentPlatform();
		if (configFile != null) {
			driverPath = configFile.getProperty("driver.folder");
			if (driverPath == null || driverPath.equals("")) {
				Log.info("[driver.folder] is not set up or not have value!! Default driver path will be used: "
						+ defaultDriverPath);
				driverPath = defaultDriverPath;
			} else {
				if (currentPlatform.equals(Platform.MAC) || currentPlatform.equals(Platform.LINUX)) {
					return driverPath;
				} else {
					File smokeCheckFile = new File(driverPath + ConfigConstant.FILE_SEPARATOR + "geckodriver.exe");
					if (!smokeCheckFile.exists()) {
						throw new AssertionError("There is no driver files in [DRIVER_FOLDER_PATH]. Please re-check");
					}
				}
			}
		} else {
			Log.info("driver.folder] is not set up or not have value!! Default driver path will be used.");
			driverPath = defaultDriverPath;
		}
		return driverPath;
	}

	public static boolean getBrowserProfileConfig() {
		boolean goWithProfile = false;
		Properties configFile = getConfiguration();
		if (configFile != null) {
			String startWithProfile = configFile.getProperty("startWithProfile");
			if (startWithProfile == null || startWithProfile.equalsIgnoreCase("no")) {
				goWithProfile = false;
			} else {
				goWithProfile = true;
			}
		}
		return goWithProfile;
	}
	
	public static boolean getBrowserPrivateModeConfig() {
		boolean goWithPrivate = true;
		Properties configFile = getConfiguration();
		if (configFile != null) {
			String startPrivate = configFile.getProperty("browser.allow.private");
			if (startPrivate == null || startPrivate.equalsIgnoreCase("yes")) {
				goWithPrivate = true;
			} else if(startPrivate.equalsIgnoreCase("no")) {
				goWithPrivate = false;
			} else {
				throw new AssertionError("Invalid value for key [browser.allow.private]!!! Please recheck, only [yes] or [no] is accepted.");
			}
		}
		return goWithPrivate;
	}

	public static boolean getBrowserProxyConfig() {
		boolean goWithProxy = false;
		Properties configFile = getConfiguration();
		if (configFile != null) {
			String startWithProxy = configFile.getProperty("startWithProxy");
			if (startWithProxy == null || startWithProxy.equalsIgnoreCase("yes")) {
				goWithProxy = true;
			}
		}
		return goWithProxy;
	}

	public static String getExtentReportPath() {
		String reportPath = null;
		Properties configFile = getConfiguration();
		if (configFile != null) {
			reportPath = configFile.getProperty("report.extent.folder");
			if (reportPath == null || reportPath.equals("")) {
				Log.info("[report.extent.folder] is not set up or not have value!! Default report path will be used.");
				reportPath = ConfigConstant.CURRENT_DIR + ConfigConstant.FILE_SEPARATOR + "reports";
			}
		} else {
			Log.info("[report.extent.folder] is not set up or not have value!! Default report path will be used.");
			reportPath = ConfigConstant.CURRENT_DIR + ConfigConstant.FILE_SEPARATOR + "reports";
		}
		return reportPath;
	}

	public static String getRunTypeConfig() {
		String runType = null;
		Properties configFile = getConfiguration();
		if (configFile != null) {
			runType = configFile.getProperty("runtype");
			if (runType == null || runType.equals("")) {
				Log.info("Can not read [runtype] value! Project will run only for WebApp as default");
				runType = "Web";
			}
		} else {
			Log.info("[runtype] is not set up or not have value!! Project will run only for WebApp as default");
			runType = "Web";
		}
		return runType;
	}
}
