package utilities.selenium;

public class BrowserFactory {
	private BrowserFactory() {}
	
	public static final String INTERNET_EXPLORER = "IE";
	public static final String FIREFOX = "Firefox";
	public static final String CHROME = "Chrome";
	public static final String SAFARI = "Safari";

	private static String tlBrowserName;

	public static void setBrowserName(String browserName) {
		tlBrowserName = browserName;
	}

	public static String getBrowserName() {
		return tlBrowserName;
	}
}
