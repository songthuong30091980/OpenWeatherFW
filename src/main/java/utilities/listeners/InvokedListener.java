package utilities.listeners;

import org.openqa.selenium.WebDriver;
import org.testng.IClassListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;

import org.testng.ITestClass;
import org.testng.ITestResult;

import utilities.common.JavaUtils;
import utilities.loggers.Log;
import utilities.reporting.ExtentTestManager;
import utilities.reporting.TestAssert;
import utilities.selenium.BrowserFactory;
import utilities.selenium.DriverFactory;
import utilities.selenium.GetScreenshot.DriverScreenshot;

public class InvokedListener implements IInvokedMethodListener, IClassListener {

	@Override
	public void onBeforeClass(ITestClass testClass) {
		Log.startLog(testClass.getName());
		ExtentTestManager.startClassTest(testClass.getName());
		String browserName = BrowserFactory.getBrowserName();
		if (browserName != null) {
			WebDriver webDriver = DriverFactory.setDriver(browserName);
			webDriver.manage().window().maximize();
		}
	}

	@Override
	public void onAfterClass(ITestClass testClass) {
		WebDriver webDriver = DriverFactory.getDriver();
		if (webDriver != null) {
			webDriver.quit();
		}
		Log.endLog(testClass.getName());
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		String methodName = method.getTestMethod().getMethodName();

		String testDescription = method.getTestMethod().getDescription();
		String browserName = BrowserFactory.getBrowserName();
		if (method.isTestMethod()) {
			Log.startLog(methodName);
			TestAssert.setSoftValidate();
			if (null == testDescription || testDescription.equals("")) {
				testDescription = methodName;
			}
			ExtentTestManager.startTest("Testing [" + testDescription + "] on browser [" + browserName + "]");
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		if (method.isTestMethod()) {
			if (testResult.isSuccess()) {
				TestAssert.softValidateAll();
			} else if (testResult.getThrowable() != null
					&& testResult.getThrowable().toString().contains("org.testng.SkipException")) {
				Log.warn(testResult.getThrowable().getMessage());
				ExtentTestManager.setStepWarning(testResult.getThrowable().getMessage());
			} else {
				Log.error(testResult.getThrowable().getMessage(),
						JavaUtils.stackTraceToString(testResult.getThrowable()));
				ExtentTestManager.setStepFail(testResult.getThrowable().getMessage(), testResult.getThrowable());
				ExtentTestManager.addSnapshot(DriverScreenshot.WEB_SCREENSHOT);
			}
		}
	}
}
