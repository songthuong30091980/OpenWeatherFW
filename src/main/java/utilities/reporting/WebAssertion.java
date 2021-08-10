package utilities.reporting;

public class WebAssertion extends TestAssert {
	private WebAssertion() {}
	
	public static synchronized  void fail(String validateMessage, Throwable... error) {
		if(error.length == 0) {
			TestAssert.fail(validateMessage);
		} else {
			TestAssert.fail(validateMessage, error[0]);
		}
	}
	
	public static synchronized  void pass(String validateMessage) {
		TestAssert.pass(validateMessage);
	}
	
	public static synchronized  void info(String validateMessage) {
		TestAssert.info(validateMessage);
	}
}
