package utilities.listeners;

import java.util.Properties;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import configuration.ConfigReader;
import utilities.loggers.Log;
import utilities.reporting.ExtentManager;
import utilities.selenium.BrowserFactory;
import utilities.selenium.ExecutionPlatform;

public class SuiteListener implements ISuiteListener {

	private void setUpWebSuite(ISuite suite) {
		String browserName = System.getProperty("browser");

		if (null == browserName || browserName.equals("")) {
			browserName = suite.getXmlSuite().getParameter("browser");
		}

		if (null == browserName || browserName.equals("")) {
			Properties configFile = ConfigReader.getConfiguration();
			if (configFile != null) {
				browserName = configFile.getProperty("defaultBrowserType");
				if (browserName == null || browserName.equals("")) {
					Log.info(
							"[defaultBrowserType] value in [configuration.properties] is not set up! Firefox will be started as default.");
					browserName = "firefox";
				}
			} else {
				browserName = "firefox";
			}
		}
		BrowserFactory.setBrowserName(browserName);
	}

	private String lookupRunType(ISuite suite) {
		String runType = System.getProperty("runtype");
		if (null == runType || runType.equals("")) {
			runType = suite.getXmlSuite().getParameter("runtype");
		}
		if (null == runType || runType.equals("")) {
			runType = ConfigReader.getRunTypeConfig();
		}
		ExecutionPlatform.setPlaformType(runType);
		return runType;
	}

	@Override
	public void onStart(ISuite suite) {
		String suiteName = suite.getXmlSuite().getName();

		Log.initLog();
		String runtype = lookupRunType(suite);
		if (runtype.equalsIgnoreCase("Web")) {
			setUpWebSuite(suite);
			ExtentManager.createInstance(suiteName, "Web_" + BrowserFactory.getBrowserName());
		} else {
			throw new AssertionError("Invalid Run Type!!!");
		}
	}

	@Override
	public void onFinish(ISuite suite) {
		Log.info("End Test suite [" + suite.getName() + "] and Flush reporting");
		ExtentManager.getExtentReport().flush();
	}

}
