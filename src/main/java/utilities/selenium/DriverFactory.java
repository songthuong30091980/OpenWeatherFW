package utilities.selenium;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import configuration.ConfigConstant;
import configuration.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.reporting.WebAssertion;
//import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	private DriverFactory() {
	}

	private static BrowserOptionsManager optionsManager = new BrowserOptionsManager();
	private static ThreadLocal<WebDriver> threadlocalDriver = new ThreadLocal<WebDriver>();

	private static int defaultWaitTimer = 60;

	public static WebDriver setDriver(String browser) {
		String driversFolderPath = ConfigReader.getDriverPath();
		String downloaddriverPath;
		if (browser.equalsIgnoreCase(BrowserFactory.FIREFOX)) {			
			downloaddriverPath = FirefoxWebDriverManagerSetUp();	
			System.setProperty("webdriver.gecko.driver",downloaddriverPath);				
			threadlocalDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			
		} else if (browser.equalsIgnoreCase(BrowserFactory.CHROME)) {
			downloaddriverPath = ChromeWebDriverManagerSetUp();
			System.setProperty("webdriver.chrome.driver",downloaddriverPath);
			threadlocalDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
		} else if (browser.equalsIgnoreCase(BrowserFactory.INTERNET_EXPLORER)) {
			System.setProperty("webdriver.ie.driver",
					driversFolderPath + ConfigConstant.FILE_SEPARATOR + "IEDriverServer.exe");
			threadlocalDriver.set(new InternetExplorerDriver(optionsManager.getInternetExplorerOption()));
		} else if (browser.equalsIgnoreCase(BrowserFactory.SAFARI)) {
			threadlocalDriver.set(new SafariDriver());
		} else {
			WebAssertion.fail("Invalid browser name! This browser is not supported or not defined",
					new Throwable("Invalid browser name!"));
		}
		return threadlocalDriver.get();
	}

	public static WebDriverWait getWait(WebDriver driver) {
		return new WebDriverWait(driver, defaultWaitTimer);
	}

	public static WebDriver getDriver() {
		return threadlocalDriver.get();
	}

	private static String ChromeWebDriverManagerSetUp()
	{
		String driversFolderPath = ConfigReader.getDriverPath();
		WebDriverManager.chromedriver().cachePath(driversFolderPath);
		WebDriverManager.chromedriver().arch64();
		WebDriverManager.chromedriver().proxy("rb-proxy-de.bosch.com:8080");
		WebDriverManager.chromedriver().proxyUser("ttr2hc");
		String pwd = decrypt("XZ0RxKGI9VrrVHDRrnh8BA==");
		WebDriverManager.chromedriver().proxyPass(pwd);
		WebDriverManager.chromedriver().setup();
		String driverPath = WebDriverManager.chromedriver().getDownloadedDriverPath();
		
		return driverPath;	
	}
	
	private static String FirefoxWebDriverManagerSetUp()
	{
		String driversFolderPath = ConfigReader.getDriverPath();
		WebDriverManager.firefoxdriver().cachePath(driversFolderPath);
		WebDriverManager.firefoxdriver().arch64();
		WebDriverManager.firefoxdriver().proxy("rb-proxy-de.bosch.com:8080");
		WebDriverManager.firefoxdriver().proxyUser("ttr2hc");
		String pwd = decrypt("XZ0RxKGI9VrrVHDRrnh8BA==");
		WebDriverManager.firefoxdriver().proxyPass(pwd);
		WebDriverManager.firefoxdriver().setup();
		String driverPath = WebDriverManager.firefoxdriver().getDownloadedDriverPath();
	
		return driverPath;
		
	}
	
	 public static String decrypt(String strToDecrypt) {
		    String SECRET_KEY = "mySecretKey@12345.";
		    String SALT = "superSalt@321";
	        try {
	            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	            IvParameterSpec ivspec = new IvParameterSpec(iv);

	            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
	            SecretKey tmp = factory.generateSecret(spec);
	            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
	            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	        } catch (Exception e) {
	            System.out.println("Error while decrypting: " + e.toString());
	        }
	        return null;
	    }
	/**
	 * public static synchronized void setGridDriver(String browser, @Optional
	 * String gridHost) { if (null == gridHost) { gridHost =
	 * "http://localhost:4444/wd/hub"; } if (browser.equals("firefox")) { try {
	 * threadlocalDriver.set(new RemoteWebDriver(new URL(gridHost),
	 * optionsManager.getFirefoxOptions())); } catch (MalformedURLException e) {
	 * e.printStackTrace(); }
	 * 
	 * } else if (browser.equals("chrome")) { try { threadlocalDriver.set(new
	 * RemoteWebDriver(new URL(gridHost), optionsManager.getChromeOptions())); }
	 * catch (MalformedURLException e) { e.printStackTrace(); } } }
	 **/
}
