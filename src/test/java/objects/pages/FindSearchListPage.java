package objects.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.reporting.WebAssertion;
import utilities.selenium.DriverFactory;
import utilities.selenium.WebActions;

public class FindSearchListPage extends WebActions {
	public FindSearchListPage() {
		PageFactory.initElements(DriverFactory.getDriver(), this);
	}

	// ---------Web Elements----------
	@FindBy(css = "div[id='forecast-list'] a")
	WebElement lstResultList;

	// ---------Declare XPATH variables----
	String xpath_citynamelink = "//*[@id='forecast-list']//*[contains(text(),'%s')]";

	// ---------Web Actions--------------

	/** To verify the result list of city name is displayed with its links **/
	
	public void verifySearchResultListPage(String sCityName) {
		try {
			waitElementVisible(lstResultList);
			String currentTitle = getCurrentPageTitle();
			if (currentTitle.contains("Find")) {
				verifyElementPresent(lstResultList, "The result list page is displayed on the list page");
				WebElement citylink = getElementByXpath(xpath_citynamelink, sCityName);
				verifyElementPresent(citylink, "The city name: " + sCityName + "link is displayed");
			} else {
				WebAssertion.fail("Cannot navigate to Find - OpenWeatherMap page");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	/** To navigate to detail of weather forecast page by clicking on city name link in the result list **/
	
	public WeatherForecastPage openWeatherForecast(String sCityName) {
		try {
			WebElement citylink = getElementByXpath(xpath_citynamelink, sCityName);
			if (citylink.isDisplayed()) {
				click(citylink, sCityName);
				waitPageTitleContains("Weather forecast");
			} else {
				WebAssertion.fail("Cannot find your city " + sCityName + " in the result list");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
		return new WeatherForecastPage();
	}
}
