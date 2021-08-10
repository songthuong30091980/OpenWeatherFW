package utilities.selenium;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.common.RobotKeyboard;
import utilities.reporting.WebAssertion;

public class WebActions {
	private WebDriver webDriver;
	private WebDriverWait defaultWait;
	private int defaultWaitTime = 60;
    private static final String SECRET_KEY = "mySecretKey@12345.";
    private static final String SALT = "superSalt@321";

	public WebActions() {
		webDriver = DriverFactory.getDriver();
		defaultWait = new WebDriverWait(webDriver, defaultWaitTime);
	}

	// Common methods
	/**
	 * Hard delay in Seconds for application
	 * 
	 * @category Common methods
	 * @param
	 **/
	protected void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Wait Web page loading complete and ready for performing actions. Wait for
	 * document ready state and jQuery complete.
	 * 
	 * @category JavaScript methods
	 **/
	protected boolean waitPageReady(int... timeOutInSeconds) {
		if (timeOutInSeconds.length == 0) {
			timeOutInSeconds = new int[] { 60 };
		}

		WebDriverWait wait = new WebDriverWait(webDriver, timeOutInSeconds[0], 200);

		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver1) {
				return ((JavascriptExecutor) webDriver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};

		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver1) {
				try {
					return ((Long) ((JavascriptExecutor) webDriver).executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};

		return wait.until(jsLoad) && wait.until(jQueryLoad);
	}

	// JavaScript methods
	/**
	 * Execute javascript on Web browser with driver
	 * 
	 * @category JavaScript methods
	 * @param
	 **/
	protected void executeJavaScript(String script) {

		try {
			((JavascriptExecutor) webDriver).executeScript(script);
			WebAssertion.info("[Executed] Javascript [" + script + "]");
		} catch (Exception ex) {
			WebAssertion.fail("failAndSnapshot to execute Javascript!", ex);
		}
	}

	protected void executeJavaScript(String script, WebElement element) {

		try {
			((JavascriptExecutor) webDriver).executeScript(script, element);
			WebAssertion.info("[Executed] Javascript [" + script + "]");
		} catch (Exception ex) {
			WebAssertion.fail("failAndSnapshot to execute Javascript!", ex);
		}
	}

	protected void focusCurrentBrowser() {
		try {
			((JavascriptExecutor) webDriver).executeScript("window.focus();");
			sleep(1000);
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void openNewTabByJS() {
		try {
			((JavascriptExecutor) webDriver).executeScript("window.open()");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	/**
	 * Move viewing focus to specify WebElement on browser
	 * 
	 * @category JavaScript methods
	 * @param
	 **/
	protected void focusByJSUsingElement(WebElement element) {
		try {
			((JavascriptExecutor) webDriver).executeScript("arguments[0].focus();", element);
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	/**
	 * Scroll to Web Element by executing Javascript with driver. Element need to be
	 * Not NULL.
	 * 
	 * @category JavaScript methods
	 * @param
	 **/
	protected void scrollIntoViewElement(WebElement element) {
		try {
			((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", element);
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	/**
	 * Scroll to top of page for website on browser.
	 * 
	 * @category JavaScript methods
	 **/
	protected void scrollToTopOfPage() {
		try {
			((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, -document.body.scrollHeight)");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	/**
	 * Scroll to Bottom of page for website on browser.
	 * 
	 * @category JavaScript methods
	 **/
	protected void scrollToBottomOfPage() {
		try {
			((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	/**
	 * Click WebElement on website by using Javascripts.
	 * 
	 * @category JavaScript methods
	 * @param
	 **/
	protected void clickByJSUsingElement(WebElement element, String elementName) {
		try {
			waitElementVisible(element);
			waitElementClickable(element);
			focusByJSUsingElement(element);
			sleep(1000);
			((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);

			WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void clickByJSUsingXpath(String eXpath, String elementName) {
		try {
			String actualScript = String.format(
					"document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.click()",
					eXpath);
			((JavascriptExecutor) webDriver).executeScript(actualScript);
			sleep(1000);
			WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void dropFileFormSystem(String filePath, WebElement target, String eName, int offsetX, int offsetY) {
		try {
			File fileToUpload = new File(filePath);
			if (!fileToUpload.exists()) {
				WebAssertion.fail("File not found: " + filePath.toString());
			} else {
				WebDriver wrappedDriver;
				if (WrapsElement.class.isAssignableFrom(target.getClass())) {
					wrappedDriver = ((RemoteWebElement) ((WrapsElement) target).getWrappedElement()).getWrappedDriver();
				} else {
					wrappedDriver = ((RemoteWebElement) target).getWrappedDriver();
				}

				JavascriptExecutor jse = (JavascriptExecutor) wrappedDriver;

				String JS_DROP_FILE = "var target = arguments[0]," + "    offsetX = arguments[1],"
						+ "    offsetY = arguments[2]," + "    document = target.ownerDocument || document,"
						+ "    window = document.defaultView || window;" + ""
						+ "var input = document.createElement('INPUT');" + "input.type = 'file';"
						+ "input.style.display = 'none';" + "input.onchange = function () {"
						+ "  var rect = target.getBoundingClientRect(),"
						+ "      x = rect.left + (offsetX || (rect.width >> 1)),"
						+ "      y = rect.top + (offsetY || (rect.height >> 1)),"
						+ "      dataTransfer = { files: this.files };" + ""
						+ "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {"
						+ "    var evt = document.createEvent('MouseEvent');"
						+ "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);"
						+ "    evt.dataTransfer = dataTransfer;" + "    target.dispatchEvent(evt);" + "  });" + ""
						+ "  setTimeout(function () { document.body.removeChild(input); }, 25);" + "};"
						+ "document.body.appendChild(input);" + "return input;";

				WebElement input = (WebElement) jse.executeScript(JS_DROP_FILE, target, offsetX, offsetY);
				input.sendKeys(filePath);
			}

			WebAssertion.info("Attach File into " + eName + " with value [" + filePath + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	// Wait Element methods

	/**
	 * Wait for Element on website displaying and visible on browser.
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 **/
	protected boolean waitElementVisible(WebElement element) {
		try {
			defaultWait.until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (NoSuchElementException | TimeoutException ex) {
			return false;
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
			throw new AssertionError(ex.getMessage());
		}
	}

	protected boolean waitElementVisible(WebElement element, int expectedTimeout) {
		try {
			new WebDriverWait(webDriver, expectedTimeout).until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (NoSuchElementException | TimeoutException ex) {
			return false;
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
			throw new AssertionError(ex.getMessage());
		}
	}

	protected boolean waitElementVisible(By eby) {
		try {
			defaultWait.until(ExpectedConditions.visibilityOfElementLocated(eby));
			return true;
		} catch (NoSuchElementException | TimeoutException ex) {
			return false;
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
			throw new AssertionError(ex.getMessage());
		}
	}

	protected boolean waitElementVisible(By eby, int expectedTimeout) {
		try {
			new WebDriverWait(webDriver, expectedTimeout).until(ExpectedConditions.visibilityOfElementLocated(eby));
			return true;
		} catch (NoSuchElementException | TimeoutException ex) {
			return false;
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
			throw new AssertionError(ex.getMessage());
		}
	}

	/**
	 * Wait for List of Elements on website displaying and visible on browser.
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 * @author HQP1HC
	 **/
	protected boolean waitElementsVisible(List<WebElement> lstElements) {
		try {
			defaultWait.until(ExpectedConditions.visibilityOfAllElements(lstElements));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitElementsVisible(By eby) {
		try {
			defaultWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(eby));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Wait for List of Elements on website displaying and visible on browser.
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 **/
	protected boolean waitElementsVisible(List<WebElement> lstElements, int expectedTimeout) {
		try {
			new WebDriverWait(webDriver, expectedTimeout)
					.until(ExpectedConditions.visibilityOfAllElements(lstElements));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Wait for Element on website become click able and allow to Click on browsers
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 * @author HQP1HC
	 **/
	protected boolean waitElementClickable(WebElement webElement) {
		try {
			defaultWait.until(ExpectedConditions.elementToBeClickable(webElement));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Wait for url of current interacting browser contain specific url (or text)
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 * @author HQP1HC
	 **/
	protected boolean waitUrlContains(String url) {
		try {
			defaultWait.until(ExpectedConditions.urlContains(url));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitPageTitleContains(String pageTitle) {
		try {
			defaultWait.until(ExpectedConditions.titleContains(pageTitle));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitUntilElementDisplayed(WebElement element) {
		WebDriverWait wait = new WebDriverWait(webDriver, 30);
		ExpectedCondition<Boolean> elementIsDisplayed = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver arg0) {
				try {
					element.isDisplayed();
					return true;
				} catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {
					return false;
				}
			}
		};
		try {
			wait.until(elementIsDisplayed);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Wait for element on website is disappeared or invisible on browser. </br>
	 * <i>Note: Recommend to use this methods instead of other wait invisible
	 * methods</i>
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 **/
	protected boolean waitElementNotDisplayed(WebElement webElement, int timeout) {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds(timeout))
					.pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
			return wait.until(ExpectedConditions.invisibilityOf(webElement));
		} catch (NoSuchElementException | InvalidElementStateException | StaleElementReferenceException ex) {
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitElementNotDisplayed(By byLocator, int timeout) {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds(timeout))
					.pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
			return wait.until(ExpectedConditions.invisibilityOfElementLocated(byLocator));
		} catch (NoSuchElementException | InvalidElementStateException | StaleElementReferenceException ex) {
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean fluentWaitClickable(WebElement element) {
		WebElement waitingElement = null;
		Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds(60))
				.pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
		waitingElement = wait.until(ExpectedConditions.elementToBeClickable(element));
		return (waitingElement != null);
	}

	protected boolean fluentWaitVisible(WebElement element) {
		WebElement waitingElement = null;
		Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds(60))
				.pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
		waitingElement = wait.until(ExpectedConditions.visibilityOf(element));
		return (waitingElement != null);
	}

	public boolean waitElementStaledOf(WebElement element, int timeOut) {
		try {
			return new WebDriverWait(webDriver, timeOut).until(ExpectedConditions.stalenessOf(element));
		} catch (Exception e) {
			return false;
		}
	}

	public WebElement waitElementRefreshedAndClickable(WebElement element, int timeOut) {
		return new WebDriverWait(webDriver, timeOut)
				.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));
	}

	public WebElement waitElementRefreshedAndClickable(By byLocator, int timeOut) {
		return new WebDriverWait(webDriver, timeOut)
				.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(byLocator)));
	}

	/**
	 * Wait for element on website is disappeared or invisible on browser.
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 **/
	protected boolean waitElementInvisible(By eBy) {
		try {
			defaultWait.until(ExpectedConditions.invisibilityOfElementLocated(eBy));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Wait for element on website is disappeared or invisible on browser.
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 **/
	protected boolean waitElementInvisible(WebElement element) {
		try {
			defaultWait.until(ExpectedConditions.invisibilityOf(element));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Wait for element on website is disappeared or invisible on browser.
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 **/
	protected boolean waitElementInvisible(WebElement element, int timeout) {
		try {
			new WebDriverWait(webDriver, timeout).until(ExpectedConditions.invisibilityOf(element));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Wait for an element have containing an Attribute with expected value
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 **/
	protected boolean waitAttributeElementContains(WebElement element, String attribute, String value) {
		try {
			defaultWait.until(ExpectedConditions.attributeContains(element, attribute, value));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitAttributeElementContains(WebElement element, String attribute, String value, int timeout) {
		try {
			new WebDriverWait(webDriver, timeout)
					.until(ExpectedConditions.attributeContains(element, attribute, value));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitElementAttributeNotEmpty(WebElement element, String attribute) {
		try {
			defaultWait.until(ExpectedConditions.attributeToBeNotEmpty(element, attribute));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitAttributeElementEquals(WebElement element, String attribute, String value) {
		try {
			defaultWait.until(ExpectedConditions.attributeToBe(element, attribute, value));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitElementTextEquals(WebElement element, String text) {
		try {
			defaultWait.until(ExpectedConditions.textToBePresentInElement(element, text));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitAllElementsContainsText(List<WebElement> lstElements, String expectedText) {
		boolean contains = true;
		try {
			if (waitElementsVisible(lstElements)) {
				for (WebElement element : lstElements) {
					String elementText = element.getText();
					if (!elementText.contains(expectedText))
						contains = false;
				}
			}
		} catch (Exception ex) {
			contains = false;
		}
		return contains;
	}

	protected boolean waitListTextContainAllElementsText(List<String> expectedListText, List<WebElement> lstElements) {
		boolean contains = true;
		try {
			if (waitElementsVisible(lstElements)) {
				for (WebElement element : lstElements) {
					String eText = element.getText();
					if (!expectedListText.contains(eText)) {
						contains = false;
					}
				}
			}
		} catch (Exception ex) {
			contains = false;
		}
		return contains;
	}

	protected boolean waitAllElementsTextContainListText(List<WebElement> lstElements, List<String> expectedListText) {
		boolean contains = true;
		try {
			List<String> lstActualText = new ArrayList<String>();
			if (waitElementsVisible(lstElements)) {
				for (WebElement element : lstElements)
					lstActualText.add(element.getText());
				for (String text : expectedListText)
					if (!lstActualText.contains(text))
						contains = false;
			}
		} catch (Exception ex) {
			contains = false;
		}
		return contains;
	}

	/**
	 * Wait for an Iframe element is displayed on browser and Switch driver to this
	 * iframe.
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 **/
	protected boolean waitFrameDisplayAndSwitchTo(WebElement iFrame) {
		try {
			defaultWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iFrame));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitAnyElementsEqualsText(List<WebElement> lstElements, String expectedText) {
		boolean equals = false;
		try {
			if (waitElementsVisible(lstElements)) {
				for (WebElement element : lstElements) {
					if (element.getText().equals(expectedText)) {
						equals = true;
					}
				}
			}
		} catch (Exception ex) {
			equals = false;
		}
		return equals;
	}

	/**
	 * Wait for url of current interacting browser is not contained specific url (or
	 * text)
	 * 
	 * @category Wait Element methods
	 * @param
	 * @return true/false
	 **/
	protected boolean waitUrlNotEquals(String lastUrl) {
		try {
			defaultWait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(lastUrl)));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitUrlEquals(String expectedUrl) {
		try {
			defaultWait.until(ExpectedConditions.urlToBe(expectedUrl));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean waitNumberElementsEquals(List<WebElement> lstElements, int expectedNumber) {
		try {
			int actualNumber = lstElements.size();
			int currentSeconds = 0;
			while (actualNumber != expectedNumber && currentSeconds < 60) {
				actualNumber = lstElements.size();
				sleep(1000);
			}
			return (actualNumber == expectedNumber);

		} catch (Exception ex) {
			return false;
		}
	}

	protected WebElement findElementByParameter(By by) {
		try {
			waitElementVisible(by);
			return webDriver.findElement(by);
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
			return null;
		}
	}

	protected WebElement findElementByParameter(By by, int timeOut) {
		try {
			waitElementVisible(by, timeOut);
			return webDriver.findElement(by);
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
			return null;
		}
	}

	protected List<WebElement> findElementsByParameter(By locator, int... timeOut) {
		if (timeOut.length <= 0) {
			timeOut = new int[] { 30 };
		}
		for (int i = 0; i < timeOut[0]; i++) {
			try {
				List<WebElement> listElement = webDriver.findElements(locator);
				if (!listElement.isEmpty()) {
					return listElement;
				} else {
					sleep(1000);
				}
			} catch (Exception ex) {
				sleep(1000);
			}
		}
		return Collections.emptyList();
	}

	protected boolean customWaitAndCheckErrorPageDisplay(By locator, int timeout, By... errorLocators) {
		int tryTime = 0;
		try {
			do {
				List<WebElement> listElement = webDriver.findElements(locator);
				if (!listElement.isEmpty()) {
					return true;
				} else {
					for (By byError : errorLocators) {
						if (isElementExist(byError)) {
							WebAssertion.fail("The Website is down or Error: " + byError.toString());
							break;
						} else {
							sleep(1000);
						}
					}
				}
			} while (tryTime < timeout);
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	protected boolean isElementExist(By by) {
		boolean exist = false;
		if (!webDriver.findElements(by).isEmpty()) {
			exist = true;
		} else {
			exist = false;
		}
		return exist;
	}

	// Handle internal contents (iframe, alert) methods
	/**
	 * Switch driver to default content of website (Default iframe, switch back to
	 * browser from Alert)
	 * 
	 * @category Selenium action methods
	 * @return N/A. Error will trigger failAndSnapshot in report.
	 **/
	protected void switchToDefaultContent() {
		try {
			webDriver.switchTo().defaultContent();
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void switchToIframe(WebElement element) {
		try {
			webDriver.switchTo().frame(element);
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected boolean isAlertPresent() {
		try {
			defaultWait.until(ExpectedConditions.alertIsPresent());
			webDriver.switchTo().alert();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected void checkAndAcceptAlert() {
		try {
			if (isAlertPresent()) {
				Alert alert = webDriver.switchTo().alert();
				alert.accept();
				webDriver.switchTo().defaultContent();
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void checkAndDismissAlert() {
		try {
			if (isAlertPresent()) {
				Alert alert = webDriver.switchTo().alert();
				alert.dismiss();
				webDriver.switchTo().defaultContent();
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void loginOnAuthenticationAlert(String username, String password) {
		try {
			if (isAlertPresent()) {
				webDriver.switchTo().alert();
				sleep(1000);
				inputUsingRobot("Username", username);
				pressKeyByRobot(KeyEvent.VK_TAB);
				releaseKeyPressedByRobot(KeyEvent.VK_TAB);
				inputUsingRobot("Password", password);
				sleep(1000);
				pressKeyByRobot(KeyEvent.VK_ENTER);
				releaseKeyPressedByRobot(KeyEvent.VK_ENTER);
				webDriver.switchTo().defaultContent();
				WebAssertion.pass("Logged in successfully with account [" + username + "]");
			} else {
				WebAssertion.info("Authentication Alert is not displayed! No log-in require.");
			}
		} catch (Exception ex) {
			WebAssertion.fail("Can not login on Authentication Alert!!!", ex);
		}
	}

	protected String checkAndGetAlertMessage() {
		String alertMessage = null;
		try {
			if (isAlertPresent()) {
				Alert alert = webDriver.switchTo().alert();
				alertMessage = alert.getText();
				alert.dismiss();
				webDriver.switchTo().defaultContent();
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
		return alertMessage;
	}

	// Interact action (click, navigate, input, hover, focus) methods
	/**
	 * Navigate to 1 website by its url.
	 * 
	 * @category Selenium action methods
	 * @param
	 * @return N/A. Error will trigger failAndSnapshot in report.
	 **/
	protected void navigateToURL(String webUrl) {
		try {
			webDriver.navigate().to(webUrl);
			WebAssertion.info("Successfull navigated to url: [" + webUrl + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void accessAndWaitUrlChange(String webUrl, String pageTitle) {
		try {
			webDriver.navigate().to(webUrl);
			waitUrlNotEquals(webUrl);
			WebAssertion.info("Succesfully access to [" + pageTitle + "] with url [" + webUrl + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void navigateBack() {
		try {
			webDriver.navigate().back();
			WebAssertion.info("Successfully Back to the previous page");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected String getCurrentPageTitle() {
		String pageTitle = null;
		try {
			waitPageReady();
			pageTitle = webDriver.getTitle();
			WebAssertion.info("Current Page Title is [" + pageTitle + "]");
			return pageTitle;
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
			return null;
		}
	}

	protected String getCurrentPageUrl() {
		String currentURL = null;
		try {
			waitPageReady();
			currentURL = webDriver.getCurrentUrl();
			WebAssertion.info("Current URL is [" + currentURL + "]");
			return currentURL;
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
			return null;
		}
	}

	protected WebElement getWebTableCell(WebElement webTable, int colIndex, String textMatch) {
		try {
			List<WebElement> rows = webTable.findElements(By.xpath("tbody/tr"));
			for (WebElement row : rows) {
				WebElement cell = row.findElement(By.xpath(String.format("td[%s]", colIndex)));
				if (cell.getText().equals(textMatch)) {
					return cell;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	protected WebElement getWebTableRow(WebElement webTable, int rowIndex) {
		List<WebElement> rows = webTable.findElements(By.xpath("tbody/tr"));
		return rows.get(rowIndex);
	}

	protected WebElement getWebElementInTableCell(WebElement webTable, String textMatch, By eLocator) {
		try {
			for (int i = 0; i < 60; i++) {
				List<WebElement> rows = webTable.findElements(By.xpath("tbody/tr"));
				for (WebElement row : rows) {
					if (row.getText().contains(textMatch)) {
						return row.findElement(eLocator);
					}
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	protected void hoverElement(WebElement element, String elementName) {
		try {
			Actions action = new Actions(webDriver);
			action.moveToElement(element).build().perform();
			WebAssertion.info("Successfully Move and hover on [" + elementName + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void hoverAndSelectSubMenuOption(WebElement parrentMenu, String parrentName, WebElement subMenu,
			String subMenuName) {
		try {
			Actions action = new Actions(webDriver);
			action.moveToElement(parrentMenu).click().build().perform();
			sleep(1000);
			WebAssertion.info("Successfully Move and hover on [" + parrentName + "]");

			action.moveToElement(subMenu).click().build().perform();
			WebAssertion.info("Successfully Hover on and Select [" + subMenuName + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void hoverAndSelectSubMenuByClickAndHold(WebElement parrentMenu, String parrentName, WebElement subMenu,
			String subMenuName) {
		try {
			waitElementVisible(parrentMenu);
			Actions action = new Actions(webDriver);
			action.moveToElement(parrentMenu).clickAndHold().build().perform();
			WebAssertion.info("Successfully Move,click and hold on [" + parrentName + "]");
			sleep(1000);
			waitElementVisible(subMenu);
			action.moveToElement(subMenu).release(subMenu).click(subMenu).build().perform();
			WebAssertion.info("Successfully Move and hover on and click on [" + subMenuName + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	/**
	 * Input text into textbox or textarea. The textbox will not be cleared before
	 * inputting. Action be performed with Actions class to simulate user's
	 * interactions. </br>
	 * <i>Note: if textbox name having "password", the input string show in Report
	 * will be change to covered symbol ***</i>
	 * 
	 * @category Selenium action methods
	 * @param
	 * @return N/A. Error will trigger failAndSnapshot in report.
	 **/
	protected void inputByAction(WebElement element, String elementName, String strInput) {
		try {
			waitElementVisible(element);
			Actions actions = new Actions(webDriver);
			actions.click(element);
			actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).build().perform();
			actions.click(element).sendKeys(strInput).build().perform();
			WebAssertion.info("[Inputted] into [" + elementName + "] with value [" + strInput + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void deleteContentOfTextBox(WebElement element, String eName) {
		try {
			waitElementVisible(element);
			Actions actions = new Actions(webDriver);
			actions.click(element);
			element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
			/*
			 * actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys
			 * .DELETE).build().perform();
			 */
			WebAssertion.info(String.format("Deleted content of %s", eName));
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);

		}
	}

	protected void sweepBlockInputtedText(WebElement element) {
		try {
			waitElementVisible(element);
			Actions actions = new Actions(webDriver);
			actions.click(element);
			sleep(500);
			element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		} catch (Exception ex) {
			WebAssertion.fail("failAndSnapshoted to sweep block inputed text", ex);
		}
	}

	/**
	 * Click on Web Element on Website within interacting browser. Element could be
	 * WebElement already found or its By Locator.
	 * 
	 * @category Selenium action methods
	 * @param
	 * @return N/A. Error will trigger failAndSnapshot in report.
	 **/
	protected <T> void clickByAction(T elementAttr, String elementName) {
		try {
			WebElement element = null;
			if (elementAttr.getClass().getName().contains("By")) {
				element = webDriver.findElement((By) elementAttr);
			} else {
				element = (WebElement) elementAttr;
			}
			waitElementVisible(element);
			waitElementClickable(element);
			Actions actions = new Actions(webDriver);
			actions.moveToElement(element).pause(Duration.ofSeconds(1, 1));
			actions.click(element).build().perform();
			WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
		} catch (Exception ex) {
			WebAssertion.fail("failAndSnapshot to click on element!", ex);
		}
	}

	protected void rightClickByAction(WebElement element, String elementName) {
		try {
			waitElementVisible(element);
			waitElementClickable(element);
			focusByJSUsingElement(element);
			Actions actions = new Actions(webDriver);
			actions.moveToElement(element).pause(Duration.ofSeconds(2, 1));
			actions.contextClick(element).build().perform();
			WebAssertion.info("Successfull Right Clicked on: [" + elementName + "]");
		} catch (Exception ex) {
			WebAssertion.fail("failAndSnapshot to perform Right click on element!", ex);
		}
	}

	protected void trippleClick(WebElement clickElement) {
		String jsTrippleClick = "var target = arguments[0];                                 "
				+ "var offsetX = arguments[1];                                "
				+ "var offsetY = arguments[2];                                "
				+ "var rect = target.getBoundingClientRect();                 "
				+ "var cx = rect.left + (offsetX || (rect.width / 2));        "
				+ "var cy = rect.top + (offsetY || (rect.height / 2));        "
				+ "                                                           "
				+ "emit('mousedown', {clientX: cx, clientY: cy, buttons: 1}); "
				+ "emit('mouseup',   {clientX: cx, clientY: cy});             "
				+ "emit('mousedown', {clientX: cx, clientY: cy, buttons: 1}); "
				+ "emit('mouseup',   {clientX: cx, clientY: cy});             "
				+ "emit('mousedown', {clientX: cx, clientY: cy, buttons: 1}); "
				+ "emit('mouseup',   {clientX: cx, clientY: cy});             "
				+ "emit('click',     {clientX: cx, clientY: cy, detail: 3});  "
				+ "                                                           "
				+ "function emit(name, init) {                                "
				+ "target.dispatchEvent(new MouseEvent(name, init));        "
				+ "}                                                          ";

		Actions actions = new Actions(webDriver);
		actions.moveToElement(clickElement, 0, 0).perform();

		((JavascriptExecutor) webDriver).executeScript(jsTrippleClick, clickElement, 0, 0);
	}

	/**
	 * Input text into textbox or textarea. The textbox will be cleared before
	 * inputting. Element could be WebElement already found or its By Locator. </br>
	 * <i>Note: if textbox name having "password", the input string show in Report
	 * will be change to covered symbol ***</i>
	 * 
	 * @category Selenium action methods
	 * @param
	 * @return N/A. Error will trigger failAndSnapshot in report.
	 **/
	protected <T> void inputText(T elementAttr, String elementName, String strInput) {
		try {
			WebElement element = null;
			if (elementAttr.getClass().getName().contains("By")) {
				element = webDriver.findElement((By) elementAttr);
			} else {
				element = (WebElement) elementAttr;
			}
			waitElementVisible(element);

			element.click();
			element.clear();
			element.sendKeys(strInput);
			if (elementName.toLowerCase().contains("password")) {
				strInput = "********";
			}
			WebAssertion.info("[Inputted] into [" + elementName + "] with value [" + strInput + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected <T> void inputWithDelay(T elementAttr, String elementName, String strInput, int delayTimeInMiliseconds) {
		try {
			WebElement element = null;
			if (elementAttr.getClass().getName().contains("By")) {
				element = webDriver.findElement((By) elementAttr);
			} else {
				element = (WebElement) elementAttr;
			}

			waitElementVisible(element);

			waitElementVisible(element);
			element.click();

			element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));

			for (int i = 0; i < strInput.length(); i++) {
				char c = strInput.charAt(i);
				String s = new StringBuilder().append(c).toString();
				element.sendKeys(s);
				sleep(delayTimeInMiliseconds);
			}

			if (elementName.toLowerCase().contains("password")) {
				strInput = "********";
			}
			WebAssertion.info("[Inputted] into [" + elementName + "] with value [" + strInput + "]");

		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	/**
	 * Input text into textbox or textarea. The textbox will not be cleared before
	 * inputting. Element could be WebElement already found or its By Locator. </br>
	 * <i>Note: if textbox name having "password", the input string show in Report
	 * will be change to covered symbol ***</i>
	 * 
	 * @category Selenium action methods
	 * @param
	 * @return N/A. Error will trigger failAndSnapshot in report.
	 **/
	protected <T> void inputWithoutClear(T elementAttr, String elementName, String strInput) {
		try {
			WebElement element = null;
			if (elementAttr.getClass().getName().contains("By")) {
				element = webDriver.findElement((By) elementAttr);
			} else {
				element = (WebElement) elementAttr;
			}

			waitElementVisible(element);

			element.click();
			element.sendKeys(strInput);
			if (elementName.toLowerCase().contains("password")) {
				strInput = "********";
			}
			WebAssertion.info("[Inputted] into [" + elementName + "] with value [" + strInput + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void attachFile(WebElement element, String elementName, String filePath) {
		try {
			element.sendKeys(filePath);
			WebAssertion.info("[Attach File] into [" + elementName + "] with value [" + filePath + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected <T> void click(T elementAttr, String elementName) {
		try {
			WebElement element = null;
			if (elementAttr.getClass().getName().contains("By")) {
				element = webDriver.findElement((By) elementAttr);
			} else {
				element = (WebElement) elementAttr;
			}
			waitElementVisible(element);
			waitElementClickable(element);

			focusByJSUsingElement(element);
			element.click();
			sleep(1000);
			WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
		} catch (Exception ex) {
			WebAssertion.fail("failAndSnapshot to click on element!", ex);
		}
	}

	protected void clickUntilURLChange(WebElement clickElement, String elementName, int tryTime) {
		try {
			int clickTime = 0;
			String currentURL = webDriver.getCurrentUrl();
			String newURL;
			do {
				fluentWaitVisible(clickElement);
				fluentWaitClickable(clickElement);
				focusByJSUsingElement(clickElement);
				clickElement.click();
				clickTime++;
				sleep(1000);
				newURL = webDriver.getCurrentUrl();
			} while (newURL.equals(currentURL) && clickTime < tryTime);
			if (clickTime == tryTime) {
				WebAssertion.fail("failAndSnapshot on [Click] on [" + elementName + "]");
			} else {
				WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void clickUntilElementUnselected(WebElement element, String elementName, int timeout) {
		try {
			waitElementClickable(element);
			int currentSeconds = 0;
			do {
				element.click();
				sleep(1000);
				waitPageReady();
				currentSeconds++;
			} while (element.isSelected() && currentSeconds != timeout);

			if (currentSeconds == timeout) {
				WebAssertion.fail("failAndSnapshot on [Click] on [" + elementName + "]");
			} else {
				WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void clickUntilAttributeContains(WebElement element, String elementName, String attribute, String value,
			int timeout) {
		try {
			int currentSeconds = 0;
			do {
				waitElementVisible(element);
				waitElementClickable(element);
				element.click();
				sleep(1000);
				waitPageReady();
				currentSeconds++;
			} while (!waitAttributeElementContains(element, attribute, value, 1) && currentSeconds != timeout);

			if (currentSeconds == timeout) {
				WebAssertion.fail("failAndSnapshot on [Click] on [" + elementName + "]");
			} else {
				WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void clickUntilElementInvisible(WebElement element, String elementName, int timeout) {
		try {
			int currentSeconds = 0;
			do {
				waitElementVisible(element);
				waitElementClickable(element);
				element.click();
				sleep(2000);
				waitPageReady();
				currentSeconds++;
			} while (waitElementVisible(element, 1) && currentSeconds != timeout);

			if (currentSeconds == timeout) {
				WebAssertion.fail("failAndSnapshot on [Click] on [" + elementName + "]");
			} else {
				WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void clickUntilNewElementVisible(WebElement element, String elementName, WebElement newElement,
			int timeout) {
		try {
			waitElementClickable(element);
			int currentSeconds = 0;
			do {
				element.click();
				sleep(1000);
				waitPageReady();
				currentSeconds++;
			} while (!waitElementVisible(newElement, 1) && currentSeconds != timeout);

			if (currentSeconds == timeout) {
				WebAssertion.fail("failAndSnapshot on [Click] on [" + elementName + "]");
			} else {
				WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void clickAndWaitUrlChange(WebElement element, String elementName) {
		try {
			String beforeUrl = webDriver.getCurrentUrl();
			waitElementClickable(element);
			focusByJSUsingElement(element);
			element.click();
			if (!waitUrlNotEquals(beforeUrl)) {
				WebAssertion.info("Successfull Clicked on: [" + elementName + "]");
			} else {
				WebAssertion.fail("failAndSnapshot on [Click] on [" + elementName + "]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	/**
	 * Simulate action Pressing keyboard key and send to web browser. Action be
	 * performed with Actions class to simulate user's interactions.
	 * 
	 * @category Selenium action methods
	 * @param
	 * @return N/A. Error will trigger failAndSnapshot in report.
	 **/
	protected void actionSendKey(Keys key) {
		try {
			Actions actions = new Actions(webDriver);
			actions.sendKeys(key).build().perform();
		} catch (Exception ex) {
			WebAssertion.fail("Can not input keyboard with key [" + key.toString() + "]", ex);
		}
	}

	protected void actionSendKeyByString(String keyString) {
		try {
			Actions actions = new Actions(webDriver);
			actions.sendKeys(keyString).build().perform();
		} catch (Exception ex) {
			WebAssertion.fail("Can not input keyboard with key [" + keyString + "]", ex);
		}
	}

	protected void dragAndDrop(WebElement sourceElement, String sourceElementName, WebElement targetElement,
			String targetElementName) {
		Actions action;
		try {
			action = new Actions(webDriver);
			scrollIntoViewElement(sourceElement);
			sleep(500);
			action.clickAndHold(sourceElement).release(targetElement).build().perform();
			sleep(1000);
			WebAssertion.info(
					"Successfully [Drag and Drop] the [" + sourceElementName + "] to [" + targetElementName + "]");

		} catch (Exception ex) {
			try {
				action = new Actions(webDriver);
				scrollIntoViewElement(sourceElement);
				sleep(500);
				action.clickAndHold(sourceElement).build().perform();
				scrollIntoViewElement(targetElement);
				sleep(500);
				action.release(targetElement).build().perform();
				sleep(1000);

				WebAssertion.info(
						"Successfully [Drag and Drop] the [" + sourceElementName + "] to [" + targetElementName + "]");
			} catch (Exception ex1) {
				WebAssertion.fail(ex1.getMessage(), ex1);
			}
		}
	}

	protected void selectComboBoxByValue(WebElement eSelect, String elementName, String optionValue) {
		try {
			new Select(eSelect).selectByValue(optionValue);
			sleep(500);
			WebAssertion
					.info("Successfully [Selected] option from [" + elementName + "] with value [" + optionValue + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void selectComboBoxByIndex(WebElement eSelect, String eName, int optionIndex) {

		try {
			new Select(eSelect).selectByIndex(optionIndex);
			sleep(2000);

			WebAssertion.info("[Select] option from [" + eName + "] at index  [" + optionIndex + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected <T> void selectRadioOption(T elementAttr, String elementName) {
		try {
			WebElement element = null;
			if (elementAttr.getClass().getName().contains("By")) {
				element = webDriver.findElement((By) elementAttr);
			} else {
				element = (WebElement) elementAttr;
			}
			waitElementVisible(element);
			waitElementClickable(element);

			focusByJSUsingElement(element);

			if (element.isSelected()) {
				// do nothing as already selected
			} else {
				element.click();
			}
			WebAssertion.info("[Selected] Radio option [" + elementName + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected <T> String readText(T elementAttr) {
		String returnedText = null;
		try {
			WebElement element = null;
			if (elementAttr.getClass().getName().contains("By")) {
				element = webDriver.findElement((By) elementAttr);
			} else {
				element = (WebElement) elementAttr;
			}
			waitElementVisible(element);
			focusByJSUsingElement(element);
			returnedText = element.getText();
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
		return returnedText;
	}

	protected void searchAndSelectItemInCustomDropdownByIndex(String parentLocator, String key,
			String allItemsLocator) {
		WebElement parentDropdown = webDriver.findElement(By.xpath(parentLocator));

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", parentDropdown);
		sleep(5000);
		parentDropdown.clear();
		parentDropdown.sendKeys(key);

		List<WebElement> allItemsDropdownList = webDriver.findElements(By.xpath(allItemsLocator));
		int itemNumber = allItemsDropdownList.size();

		defaultWait.until(ExpectedConditions.visibilityOfAllElements(allItemsDropdownList));

		for (int i = 0; i < itemNumber; i++) {
			allItemsDropdownList.get(-1).click();
		}
	}

	protected String getPageTitle() {
		try {
			return webDriver.getTitle();
		} catch (Exception ex) {
			WebAssertion.fail("Unable to get Title of current page: " + ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * Refresh current page of Website on Browser
	 * 
	 * @category Selenium action methods
	 * @return N/A. Error will trigger failAndSnapshot in report.
	 **/
	protected void refreshBrowser() {
		try {
			webDriver.navigate().refresh();
			waitPageReady();
			WebAssertion.info("Refreshed current Page");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void gobackPreviousPage() {
		try {
			webDriver.navigate().back();

			WebAssertion.info("Back to the previous page");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}

	}

	protected int getIndexByTextEqualy(List<WebElement> listElement, String textDefinition) {
		int index = 0;
		boolean indexFound = false;
		for (int i = 0; i < listElement.size(); i++) {
			if (listElement.get(i).getText().equalsIgnoreCase(textDefinition)) {
				index = i;
				indexFound = true;
				break;
			}
		}
		if (indexFound) {
			return index;
		} else {
			WebAssertion.fail("Element with text [" + textDefinition + "] is not found on page");
			return -1;
		}
	}

	protected int getIndexByContainText(List<WebElement> listElement, String textDefinition) {
		int index = 0;
		boolean indexFound = false;
		for (int i = 0; i < listElement.size(); i++) {
			if (listElement.get(i).getText().contains(textDefinition)) {
				index = i;
				indexFound = true;
				break;
			}
		}
		if (indexFound) {
			return index;
		} else {
			WebAssertion.fail("Element with text [" + textDefinition + "] is not found on page");
			return -1;
		}
	}

	protected WebElement getElementInListByContainText(List<WebElement> listElement, String textDefinition) {
		WebElement webElementFound = null;
		for (WebElement webElement : listElement) {
			if (webElement.getText().contains(textDefinition))
				webElementFound = webElement;
		}
		if (webElementFound == null) {
			WebAssertion.fail("Element with text [" + textDefinition + "] is not found on page");
			return null;
		} else {
			return webElementFound;
		}
	}

	protected String getElementAttributeValue(WebElement element, String attributelementName) {
		String attributeValue = null;
		try {
			attributeValue = element.getAttribute(attributelementName);
		} catch (Exception ex) {
			return null;
		}
		return attributeValue;
	}

	protected WebElement getChildWebElement(WebElement parentElement, By childLocator) {
		WebElement element = null;
		for (int i = 0; i < 30; i++) {
			try {
				element = parentElement.findElement(childLocator);
				sleep(1000);
				break;
			} catch (NoSuchElementException e) {
				sleep(1000);
			}
		}
		return element;
	}

	// Windows Handling methods

	protected int getNumberOfWindows() {
		return webDriver.getWindowHandles().size();
	}

	protected String getCurrentListWindow() {
		try {
			return webDriver.getWindowHandle();
		} catch (Exception ex) {
			WebAssertion.fail("Unable to get current openning browser Window", ex);
		}
		return null;
	}

	protected void switchToNewWindow(int expectedNumberOfWindows, int tabNumber) {
		WebDriverWait wait = new WebDriverWait(webDriver, defaultWaitTime);
		wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows));
		try {
			ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
			webDriver.switchTo().window(tabs.get(tabNumber));
		} catch (Exception ex) {
			WebAssertion.fail("Unable to switch to new opened browser Window-Tab", ex);
		}
	}

	protected void backToMainPage(String startWindow) {
		try {
			webDriver.close();
			webDriver.switchTo().window(startWindow);
		} catch (Exception ex) {
			WebAssertion.fail("Unable to switch to Started browser Window-Tab", ex);
		}
	}

	/** Common Verify methods **/
	protected void verifyElementPresent(WebElement element, String elementName) {
		try {
			defaultWait.until(ExpectedConditions.visibilityOf(element));
			WebAssertion.pass("[" + elementName + "] is displayed on page correctly");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void verifyElementNotPresent(By by, String elementName) {
		try {
			waitElementVisible(by, 5);
			webDriver.findElement(by);
			WebAssertion.fail("Element [" + elementName + "] is displayed on page");

		} catch (NoSuchElementException noElementEx) {
			WebAssertion.pass("[" + elementName + "] is not displayed on page");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void verifyText(WebElement element, String expectedText, String actual, String message) {
		try {
			waitElementVisible(element);
			WebAssertion.info(message);
			if (actual.equalsIgnoreCase(expectedText)) {
				WebAssertion
						.pass("Text is displayed correctly. Expected: [" + expectedText + "], found [" + actual + "]");
			} else {
				WebAssertion.fail("Text is not displayed correctly. Expected: [" + expectedText + "], but found ["
						+ actual + "]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void verifyTextInList(List<WebElement> listElement, String expectedText) {
		int index = 0;
		boolean matched = true;
		for (int i = 0; i < listElement.size(); i++) {
			if (!listElement.get(i).getText().equalsIgnoreCase(expectedText)) {
				index = i;
				matched = false;
				break;
			}
		}
		if (!matched) {
			WebAssertion.fail("Element with text [" + expectedText + "] is not match with row [" + index
					+ "] with found text [" + listElement.get(index).getText() + "]");
		} else {

			WebAssertion.pass("All records in list are matched with [" + expectedText + "]");
		}
	}

	protected void verifyListContainText(List<WebElement> listElement, String expectedText) {
		boolean matched = false;
		for (int i = 0; i < listElement.size(); i++) {
			if (listElement.get(i).getText().equalsIgnoreCase(expectedText)) {
				matched = true;
				break;
			}
		}
		if (!matched) {
			WebAssertion.fail("[" + expectedText + "] is not match with any rows in list");
		} else {
			WebAssertion.pass("Record found. Text is displayed correctly as [" + expectedText + "]");
		}
	}

	protected void verifyEqualityTextInList(List<WebElement> listElement, String[] expectedTextList, String message) {
		try {
			WebAssertion.info(message);
			waitElementsVisible(listElement);
			int totalItems = listElement.size();
			String[] actualTextList = new String[totalItems];
			for (int i = 0; i < totalItems; i++) {
				actualTextList[i] = listElement.get(i).getText();
			}
			if (Arrays.equals(actualTextList, expectedTextList)) {
				WebAssertion.pass("All records in list are matched with expected list texts");
			} else {
				WebAssertion.fail("Two array lists are not matched!");
			}
		} catch (Exception ex) {
			WebAssertion.fail("failAndSnapshot to compare two arrays!", ex);
		}
	}

	protected void verifyTextWildCard(WebElement element, String expectedText, String actual, String message) {
		try {
			waitElementVisible(element);
			WebAssertion.info("Verify Wildcard text for: " + message);
			if (actual.contains(expectedText) || expectedText.contains(actual)) {
				WebAssertion
						.pass("Text is displayed correctly. Expected: [" + expectedText + "], found [" + actual + "]");
			} else {
				WebAssertion.fail("Text is not displayed correctly. Expected: [" + expectedText + "], but found ["
						+ actual + ".*]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected void verifyListTextWildCard(List<WebElement> listElement, String expectedText, String actual,
			String message) {
		try {
			int index = 0;
			boolean matched = true;
			waitElementsVisible(listElement);
			WebAssertion.info("Verify Wildcard text for: " + message);
			for (int i = 0; i < listElement.size(); i++) {
				if (!listElement.get(i).getText().contains(expectedText)) {
					index = i;
					matched = false;
					break;
				}
			}
			if (!matched) {
				WebAssertion.fail("[" + expectedText + "] is not match with row [" + index + "] with found text ["
						+ listElement.get(index).getText() + "]");
			} else {
				WebAssertion.pass("All records in list are matched with [" + expectedText + "]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	}

	protected <T> void compareAttribute(T elementType, String elementName, String attributelementName,
			String expectedAttributeValue) {
		try {
			WebElement element = null;
			if (elementType.getClass().getName().contains("By")) {
				element = webDriver.findElement((By) elementType);
			} else {
				element = (WebElement) elementType;
			}
			String actualAttributeValue = element.getAttribute(attributelementName);
			if (actualAttributeValue.trim().equalsIgnoreCase(expectedAttributeValue.trim())) {
				WebAssertion.pass(String.format("Attribute [%s] of %s display as expected: %s", attributelementName,
						elementName, expectedAttributeValue));

			} else {
				WebAssertion.fail(String.format("Attribute [%s] of %s does not display as expected: %s. Actual: %s",
						attributelementName, elementName, expectedAttributeValue, actualAttributeValue));
			}
		} catch (Exception ex) {
			WebAssertion.fail("failAndSnapshoted to compare attribute.", ex);
		}
	}

	protected <T> void compareCssValue(T elementType, String elementName, String cssName, String expectedCssValue) {
		try {
			WebElement element = null;
			if (elementType.getClass().getName().contains("By")) {
				element = webDriver.findElement((By) elementType);
			} else {
				element = (WebElement) elementType;
			}
			String actualCssValue = element.getCssValue(cssName);
			if (actualCssValue.trim().equalsIgnoreCase(expectedCssValue.trim())) {
				WebAssertion.pass(String.format("Css [%s] of %s display as expected: %s", cssName, elementName,
						expectedCssValue));
			} else {
				WebAssertion.fail(String.format("Css [%s] of %s does not display as expected: %s. Actual: %s", cssName,
						elementName, expectedCssValue, actualCssValue));
			}
		} catch (Exception ex) {
			WebAssertion.fail("failAndSnapshoted to compare attribute.", ex);
		}
	}

	protected boolean areElementsOverlapping(WebElement element1, WebElement element2) {
		Rectangle r1 = element1.getRect();
		Point topRight1 = r1.getPoint().moveBy(r1.getWidth(), 0);
		Point bottomLeft1 = r1.getPoint().moveBy(0, r1.getHeight());

		Rectangle r2 = element2.getRect();
		Point topRight2 = r2.getPoint().moveBy(r2.getWidth(), 0);
		Point bottomLeft2 = r2.getPoint().moveBy(0, r2.getHeight());

		int x1 = bottomLeft1.getX();
		int y1 = bottomLeft1.getY();
		int x2 = topRight1.getX();
		int y2 = topRight1.getY();

		int x3 = bottomLeft2.getX();
		int y3 = bottomLeft2.getY();
		int x4 = topRight2.getX();
		int y4 = topRight2.getY();

		if (y2 < y3 || y1 > y4) {
			return false;
		}
		if (x2 < x3 || x1 > x4) {
			return false;
		}
		return true;
	}

	/** Action methods perform by Robot **/
	protected void inputUsingRobot(String elementName, String strInputText) {
		try {
			RobotKeyboard robotKeyboard = new RobotKeyboard();
			sleep(1000);
			for (char c : strInputText.toCharArray()) {
				robotKeyboard.type(c);
			}
			if (elementName.toLowerCase().contains("password")) {
				strInputText = "********";
			}
			WebAssertion.info("[Input] into " + elementName + " with value [" + strInputText + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage());
		}
	}

	protected void moveMouseByRobot(int x, int y) {
		try {
			Robot robot = new Robot();
			robot.mouseMove(x, y);
			robot.mouseRelease(0);
			sleep(500);
			WebAssertion.info("Moved mouse pointer to coordinates [" + x + "," + y + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage());
		}
	}

	protected void pressKeyByRobot(int keyEvent) {
		try {
			Robot robot = new Robot();
			robot.delay(300);
			robot.keyPress(keyEvent);
			WebAssertion.info("Pressed Key [" + KeyEvent.getKeyText(keyEvent) + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage());
		}
	}

	protected void releaseKeyPressedByRobot(int keyEvent) {
		try {
			Robot robot = new Robot();
			robot.delay(300);
			robot.keyRelease(keyEvent);
			WebAssertion.info("Release Key Pressed [" + KeyEvent.getKeyText(keyEvent) + "]");
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage());
		}
	}

	protected void clickCoordinatePointsByRobot(int xPoint, int yPoint) {

		try {
			Robot robot = new Robot();
			robot.mouseMove(xPoint, yPoint);
			sleep(1000);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			sleep(500);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);

			sleep(2000);
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage());
		}
	}

	public WebElement getElementByXpath(String xpath, String sText) {
		WebElement eweb = null;
		try {
			String finalXpath_text = String.format(xpath, sText);
			eweb = findElementByParameter(By.xpath(finalXpath_text));
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
			return null;
		}
		return eweb;
	}

	protected boolean verifyWebElementTextWildCard(WebElement element, String expectedText, String functionName) {
		try {
			waitElementVisible(element);
			WebAssertion.info("Verify WebElement displays with wildcard text for: " + functionName);
			String actual = element.getText();
			if (actual.contains(expectedText) || expectedText.contains(actual)) {
				WebAssertion
						.pass("Text is displayed correctly. Expected: [" + expectedText + "], found [" + actual + "]");
			} else {
				WebAssertion.fail("Text is not displayed correctly. Expected: [" + expectedText + "], but found ["
						+ actual + ".*]");
			}
		} catch (Exception ex) {
			WebAssertion.fail(ex.getMessage(), ex);
		}
	return true;
	}

	public boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}

    public String decrypt(String strToDecrypt) {
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
}
