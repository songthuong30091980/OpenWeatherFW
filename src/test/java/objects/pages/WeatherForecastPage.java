package objects.pages;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.reporting.WebAssertion;
import utilities.selenium.DriverFactory;
import utilities.selenium.WebActions;

public class WeatherForecastPage extends WebActions {
	public WeatherForecastPage() {
		PageFactory.initElements(DriverFactory.getDriver(), this);
	}

	// ---------Web Elements----------
	@FindBy(css = ".section-content .orange-text")
	WebElement lbdatetime;

	@FindBy(xpath = "//*[@class='section-content']//h2")
	WebElement lbcity;

	@FindBy(css = ".current-temp span.heading")
	WebElement lbTemperature;

	// ---------Web Actions--------------
	/** To verify weather forecast regardless its number is matching with searched city, date **/

	public void verifySearchWeatherResult(Date sdate, String sDisplayName) {
		try {
			String currentTitle = getCurrentPageTitle();
			if (currentTitle.contains("Weather forecast")) {
				waitUntilElementDisplayed(lbdatetime);
				String mydate = new SimpleDateFormat("MMM d").format(sdate);
				String actualDate = lbdatetime.getText();
				String actualCity = lbcity.getText();
				if (actualDate.contains(mydate) && actualCity.contains(sDisplayName)) {
					WebAssertion.pass("The current date: " + mydate + " is displayed on the Weather Forecast page");
					WebAssertion.pass("The city name: " + sDisplayName + " is displayed on the Weather Forecast page");
					String actualTemp = lbTemperature.getText();
					String shortTemp = actualTemp.substring(0, actualTemp.length() - 2);
					if (isNumeric(shortTemp)) {
						WebAssertion.pass("The temperature: " + shortTemp + " is displayed regardless its number");
					} else {
						WebAssertion
								.fail("Incorrect temperature format. The temperature does not display with its number: "
										+ shortTemp);
					}

				} else {
					WebAssertion.fail("The searched current date " + mydate + " or the your city: " + sDisplayName
							+ " are not matching with your city in the Weather forecast page");
				}
			} else {
				WebAssertion.fail("Cannot navigate to Weather forecast page - OpenWeatherMap page");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

}
