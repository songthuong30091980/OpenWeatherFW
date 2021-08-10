package utilities.selenium;

import org.openqa.selenium.support.PageFactory;

public class PageGenerator {
	
	public <T extends WebActions> T getInstance(Class<T> pageClass) {
		try {
			return PageFactory.initElements(DriverFactory.getDriver(), pageClass);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
