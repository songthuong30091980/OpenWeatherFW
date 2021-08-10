package testscripts;

import java.util.Date;

import org.testng.annotations.Test;

import objects.pages.CurrentWeatherAndForecastPage;
import objects.pages.FindSearchListPage;
import objects.pages.WeatherForecastPage;
import unitbasetesting.NoListenerUnitTestingBase;
import utilities.datadriven.ReadJson;
import utilities.reporting.ExtentTestManager;

public class ToVerifySearchWeatherInYourCity extends NoListenerUnitTestingBase {
	@Test
	public void SearchWeather() {

		/* Get test data */
		String sYourCity = ReadJson.getTestData("yourcity").toString();
		String sUrl = ReadJson.getTestData("appurl").toString();
		String sCityname = ReadJson.getTestData(sYourCity, "cityname");
		String sDisplaycityname = ReadJson.getTestData(sCityname, "displaycityname");

		/* Step 1: Navigate to the Open Weather system */
		ExtentTestManager.startStep("Navigate to the Open Weather system");
		CurrentWeatherAndForecastPage currentweatherpage = new CurrentWeatherAndForecastPage();
		currentweatherpage.access(sUrl);

		/* Step 2: Searching for a city of your choice (E.g. Ha Noi) */
		ExtentTestManager.startStep("Searching for a city of your choice (E.g. Ha Noi)");
		FindSearchListPage findsearchlistpage = currentweatherpage.searchWeatherInYourCity(sYourCity);
		findsearchlistpage.verifySearchResultListPage(sCityname);

		/* Step 3: Click on the link in result list */
		ExtentTestManager.startStep("Click on the link in result list ");
		WeatherForecastPage weatherforecastpage = findsearchlistpage.openWeatherForecast(sCityname);

		/*
		 * Step 4: Verify the current date (E.g. Jun 9), city name and the weather
		 * display correct
		 */
		ExtentTestManager.startStep("Verify the current date (E.g. Jun 9), city name and the weather display correct");
		Date currentdate = new Date();
		weatherforecastpage.verifySearchWeatherResult(currentdate, sDisplaycityname);

	}
}