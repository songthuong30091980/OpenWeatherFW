package utilities.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import utilities.selenium.GetScreenshot;
import utilities.selenium.GetScreenshot.DriverScreenshot;

public class ExtentTestManager {
	private ExtentTestManager() {
	}

	private static ExtentReports extentReport = ExtentManager.getExtentReport();
	private static ThreadLocal<ExtentTest> extentClassTest = new ThreadLocal<ExtentTest>();
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	private static ThreadLocal<ExtentTest> extentStep = new ThreadLocal<ExtentTest>();
	private static ThreadLocal<Integer> stepNumber = new ThreadLocal<>();

	public static synchronized void startClassTest(String groupTest) {
		extentClassTest.set(extentReport.createTest(groupTest));
	}

	public static synchronized ExtentTest getClassTest() {
		return extentClassTest.get();
	}

	public static synchronized void startTestNode(ExtentTest classTest, String nodeDesc) {
		initStepNumber();
		extentTest.set(classTest.createNode(nodeDesc));
	}

	public static synchronized void startTest(String nodeDesc) {
		initStepNumber();
		extentTest.set(extentClassTest.get().createNode(nodeDesc));
	}

	public static synchronized ExtentTest getTest() {
		return extentTest.get();
	}

	public static synchronized void startStep(String testDesc) {
		extentStep.set(getTest().createNode("Step " + getStepNumber() + ": " + testDesc));
		increaseStepNumber();
	}

	public static synchronized ExtentTest getStep() {
		if (extentStep.get() == null) {
			startStep("");
		}
		return extentStep.get();
	}

	public static synchronized void setStepInfo(String resultDesc) {
		getStep().info(resultDesc);
	}

	public static synchronized void setStepWarning(String resultDesc) {
		getStep().warning("<p style=\"color:orange\">" + resultDesc);
	}

	public static synchronized void setStepPass(String resultDesc) {
		getStep().pass(resultDesc);
	}

	public static synchronized void setStepFail(String stepDescription, Throwable... error) {
		if (error.length == 0) {
			getStep().fail("<p style=\"color:red\">" + stepDescription);
		} else {
			getStep().fail("<p style=\"color:red\">" + stepDescription + "</br>" + error[0] + "</br>");
		}
	}

	public static synchronized void addSnapshot(DriverScreenshot driverScreenshot) {
		try {
			getStep().addScreenCaptureFromBase64String(GetScreenshot.captureError(driverScreenshot),
					"Failure snapshoot");

		} catch (Exception ex) {
			getStep().fail("</h5></br>Can't get screenshot: " + ex.getMessage());
		}
	}

	private static synchronized void initStepNumber() {
		stepNumber.set(1);
	}

	private static synchronized int getStepNumber() {
		return stepNumber.get();
	}

	private static synchronized void increaseStepNumber() {
		stepNumber.set(getStepNumber() + 1);
	}

}
