package utilities.reporting;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.api.SoftAssertions;

import utilities.loggers.Log;
import utilities.selenium.GetScreenshot.DriverScreenshot;

public class TestAssert extends Assertions {
	protected static ThreadLocal<SoftAssertions> softAssert = new ThreadLocal<SoftAssertions>();

	protected static synchronized void pass(String validateMessage) {
		Log.info(validateMessage);
		ExtentTestManager.setStepPass(validateMessage);
	}

	protected static synchronized void info(String validateMessage) {
		Log.info(validateMessage);
		ExtentTestManager.setStepInfo(validateMessage);
	}

	public static synchronized void setSoftValidate() {
		softAssert.set(new SoftAssertions());
	}

	public static synchronized SoftAssertions getSoftValidate() {
		return softAssert.get();
	}

	public static synchronized void softValidateAll() {
		try {
			softAssert.get().assertAll();
		} catch (SoftAssertionError softAssertionError) {
			List<String> errors = softAssertionError.getErrors();
			Log.error(softAssertionError.getMessage(), errors.toString());
			ExtentTestManager.setStepFail(softAssertionError.getMessage(), softAssertionError);
			ExtentTestManager.addSnapshot(DriverScreenshot.WEB_SCREENSHOT);
		}
	}

	// Invoke logs and report to AssertJ
	public synchronized static void assertTrue(boolean assertObject, String assertMessage) {
		assertThat(assertObject).as(assertMessage).isTrue();
		pass(assertMessage + ". Assert Passed. Expected value [True], found [True]");
	}

	public synchronized static void assertFalse(boolean assertObject, String assertMessage) {
		assertThat(assertObject).as(assertMessage).isFalse();
		pass(assertMessage + ". Assert Passed. Expected value [False], found [False]");
	}

	public synchronized static void assertEqual(Object assertObjectValue, Object expectedObjectValue,
			String assertMessage) {
		assertThat(assertObjectValue).as(assertMessage).isEqualTo(expectedObjectValue);
		pass(assertMessage + ". Assert Passed. Expected value [" + String.valueOf(expectedObjectValue) + "], found ["
				+ String.valueOf(assertObjectValue) + "]");
	}

	public synchronized static void assertNotEqual(Object assertObjectValue, Object expectedObjectValue,
			String assertMessage) {
		assertThat(assertObjectValue).as(assertMessage).isNotEqualTo(expectedObjectValue);
		pass(assertMessage + ". Assert Passed, two object are not equal. Not Expected value ["
				+ String.valueOf(expectedObjectValue) + "], found [" + String.valueOf(assertObjectValue) + "]");
	}
}
