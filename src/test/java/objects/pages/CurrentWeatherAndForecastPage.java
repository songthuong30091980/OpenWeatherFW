package objects.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.reporting.WebAssertion;
import utilities.selenium.DriverFactory;
import utilities.selenium.WebActions;

public class CurrentWeatherAndForecastPage extends WebActions {
	public CurrentWeatherAndForecastPage() {
		PageFactory.initElements(DriverFactory.getDriver(), this);
	}

	// ---------Web Elements----------
	@FindBy(css = ".search input")
	WebElement txtSearch;

	@FindBy(xpath = "//*[@id='desktop-menu']//*[@name='q']")
	WebElement txtGlobalSearch;

	@FindBy(css = ".logo")
	WebElement llogo;

	@FindBy(css = "button[type='submit']")
	WebElement btnSearch;

	@FindBy(xpath = "//*[@class='search']//*[contains(text(),'Ha Noi')]")
	WebElement spSearchOption;

	@FindBy(xpath = "//*[@lass='owm-loader-container']//svg")
	WebElement iloader;
	
	// ---------Declare XPATH variables----
	String xpath_seachoption = "//*[@class='search']//*[contains(text(),'%s')]";

	// ---------Web Actions--------------
	
	/** Navigate to the Open Weather system **/
	
	public CurrentWeatherAndForecastPage access(String url) {
		try {
			navigateToURL(url);
			waitPageReady();
			String currentTitle = getCurrentPageTitle();
			waitElementVisible(llogo);
			if (currentTitle.contains("weather and forecast")) {
				WebAssertion.pass("Current weather and forecast - OpenWeatherMap page is displayed successful");
			} else {
				WebAssertion.fail("Cannot navigate to Current weather and forecast - OpenWeatherMap pagee");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
		return this;
	}

	/** Enter your key work to search weather in your city **/
	
	public FindSearchListPage searchWeatherInYourCity(String sCity) {
		try {
			WebAssertion.info("Search the weather for your city: " + sCity);
			waitPageReady();
			waitElementNotDisplayed(iloader,6);
			waitUntilElementDisplayed(txtGlobalSearch);
			inputByAction(txtGlobalSearch, "Global Search", sCity);
			actionSendKey(Keys.ENTER);
			waitPageTitleContains("Find");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
		return new FindSearchListPage();
	}

}
