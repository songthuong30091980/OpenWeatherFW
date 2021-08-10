package unitbasetesting;

import java.lang.reflect.Method;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import configuration.ConfigReader;
import utilities.common.JavaUtils;
import utilities.loggers.Log;
import utilities.reporting.ExtentManager;
import utilities.reporting.ExtentTestManager;
import utilities.reporting.WebAssertion;
import utilities.selenium.BrowserFactory;
import utilities.selenium.DriverFactory;
import utilities.selenium.GetScreenshot.DriverScreenshot;

public class NoListenerUnitTestingBase {

	private String getBrowserFromSetup(String browser) {
		if (null == browser || browser.equals("")) {
			Properties configFile = ConfigReader.getConfiguration();
			if (configFile != null) {
				browser = configFile.getProperty("defaultBrowserType");
				if (browser == null || browser.equals("")) {
					Log.info(
							"[defaultBrowserType] value in [project.properties] is not set up! Firefox will be started as default.");
					browser = "firefox";
				}
			} else {
				browser = "firefox";
			}
		}
		BrowserFactory.setBrowserName(browser);
	 return browser;	
	}

	@BeforeTest
	@Parameters({"browser"})
	public final void initializeBeforeTest(ITestContext iTestCtx, String browser) {
		browser = getBrowserFromSetup(browser);
		String suiteName = iTestCtx.getSuite().getXmlSuite().getName();
		Log.initLog();
		ExtentManager.createInstance(suiteName, "Web_" + browser);

	}

	@BeforeClass(alwaysRun = true)
	@Parameters({"browser"})
	public final void initializeBeforeClass(ITestContext iTestCtx, String browser) {		
		String className = this.getClass().getSimpleName();
		Log.startLog(className);
		ExtentTestManager.startClassTest(className);
		browser = getBrowserFromSetup(browser);
		if (browser != null) {
			WebDriver webDriver = DriverFactory.setDriver(browser);
			webDriver.manage().window().maximize();
		}
	}

	@BeforeMethod(alwaysRun = true)
	@Parameters({"browser"})
	public final void initializeBeforeMethod(Method method, String browser) {
		String methodName = method.getName();
		String testDescription = method.getAnnotation(Test.class).description();

		Log.startLog(methodName);
		WebAssertion.setSoftValidate();

		if (testDescription == null || testDescription.equals("")) {
			testDescription = methodName;
		}
		ExtentTestManager.startTest("Testing [" + testDescription + "] on browser [" + browser + "]");
	}

	@AfterMethod(alwaysRun = true)
	public final void onTestFailure(ITestResult testResult) {
		if (testResult.isSuccess()) {
			WebAssertion.softValidateAll();
		} else if (testResult.getThrowable() != null
				&& testResult.getThrowable().toString().contains("org.testng.SkipException")) {
			Log.warn(testResult.getThrowable().getMessage());
			ExtentTestManager.setStepWarning(testResult.getThrowable().getMessage());
		} else {
			Log.error(testResult.getThrowable().getMessage(), JavaUtils.stackTraceToString(testResult.getThrowable()));
			ExtentTestManager.setStepFail(testResult.getThrowable().getMessage(), testResult.getThrowable());
			ExtentTestManager.addSnapshot(DriverScreenshot.WEB_SCREENSHOT);
		}
	}

	@AfterClass
	public void onAfterClass() {
		WebDriver webDriver = DriverFactory.getDriver();
		if (webDriver != null) {
			webDriver.quit();
		}
		Log.endLog(this.getClass().getName());
	}

	@AfterSuite(alwaysRun = true)
	public final void onFinish(ITestContext iTestCtx) {
		Log.info("End Test suite [" + iTestCtx.getSuite().getXmlSuite().getName() + "] and Flush reporting");
		ExtentManager.getExtentReport().flush();
	}
}
