package utilities.selenium;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import configuration.ConfigReader;
import utilities.loggers.Log;

public class BrowserOptionsManager {

	public ChromeOptions getChromeOptions() {
		boolean activeProfile = ConfigReader.getBrowserProfileConfig();
		boolean privateModeFlag = ConfigReader.getBrowserPrivateModeConfig();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("disable-infobars");
		options.addArguments("test-type");
		options.addArguments("enable-strict-powerful-feature-restrictions");
		options.addArguments("enable-geolocation");
		
		if (activeProfile) {
			Log.info(
					"[startWithProfile] option is activated, But profile is not supported for Chrome! Setting will not affecting");
		}
		if (privateModeFlag) {
			Log.info("[browser.allow.private] option is not defined or [yes]! Browser will run in Private Mode!!");
			options.addArguments("--incognito");
		} else {
			Log.info("[browser.allow.private] option is [no]! Browser will run in Normal Mode!!");
		}
		return options;
	}

	public FirefoxOptions getFirefoxOptions() {
		
		boolean privateModeFlag = ConfigReader.getBrowserPrivateModeConfig();
		FirefoxOptions option = new FirefoxOptions();
		option.setAcceptInsecureCerts(true);

		FirefoxProfile profile = null;	
		if (privateModeFlag) {
			Log.info(
					"[startWithProfile] is Inactive for running profile and [browser.allow.private] is [yes] or not defined. Project will run in default mode [Private]!!");
			option.addArguments("-private");
			profile = new FirefoxProfile();
		} else {
			Log.info(
					"[startWithProfile] is Inactive for running profile and [browser.allow.private] is [no]. Project will run in mode [Normal]!!");
			profile = new FirefoxProfile();
		}
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/vnd.ms-excel;application/vnd.ms-excel.addin.macroenabled.12;application/vnd.ms-excelsheet.binary.macroenabled.12;application/vnd.ms-excel.template.macroenabled.12;application/vnd.ms-excel.sheet.macroenabled.12");
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("security.mixed_content.block_active_content", false);
		profile.setPreference("security.mixed_content.block_display_content", true);
		profile.setPreference("geo.enabled", true);
        profile.setPreference("geo.provider.use_corelocation", true);
        profile.setPreference("geo.prompt.testing", true);
        profile.setPreference("geo.prompt.testing.allow", true);
		option.setProfile(profile);
		option.setCapability("locationContextEnabled", true);
		

		//FirefoxProfile profile = new FirefoxProfile();
		//profile.setPreference("geo.enabled", false);
		//profile.setPreference("geo.provider.use_corelocation", false);
		//profile.setPreference("geo.prompt.testing", false);
		//profile.setPreference("geo.prompt.testing.allow", false);
		//DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		//capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		
		//driver = new FirefoxDriver(capabilities);
		//option.setCapability("locationContextEnabled", false);

		return option;

	}

	public InternetExplorerOptions getInternetExplorerOption() {
		boolean privateModeFlag = ConfigReader.getBrowserPrivateModeConfig();

		InternetExplorerOptions options = new InternetExplorerOptions();
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
		options.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
		options.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
		options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		options.setCapability("requireWindowFocus", false);
		options.setCapability("enablePersistentHover", false);
		options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);
		options.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "");
		if (privateModeFlag) {
			Log.info("[browser.allow.private] option is [yes]! Browser will run in Private Mode!!");
			options.addCommandSwitches("-private");
		}
		return options;
	}
}
