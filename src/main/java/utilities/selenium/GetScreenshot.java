package utilities.selenium;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import configuration.ConfigConstant;
import configuration.ConfigReader;
import utilities.common.JavaUtils;
import utilities.loggers.Log;

public class GetScreenshot {
	private GetScreenshot() {
	}
	
	public enum DriverScreenshot{
		MOBILE_SCREENSHOT, WEB_SCREENSHOT;
	}
	
	private static String errorScreenshotPath = ConfigReader.getExtentReportPath() + ConfigConstant.FILE_SEPARATOR
			+ "error-screenshot" + ConfigConstant.FILE_SEPARATOR;

	public static String captureError(DriverScreenshot driverScreenshot) {

		JavaUtils.createDirectoryPath(errorScreenshotPath);
		String encodedString = null;
		String currentTimeStampts = JavaUtils.getTimeStamps();
		try {
			File screenshot = null;
			if(driverScreenshot.equals(DriverScreenshot.WEB_SCREENSHOT)){
				screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
			} else {
				throw new AssertionError("Can not create screenshot");
			}
			BufferedImage inputImage = ImageIO.read(screenshot);

			BufferedImage newImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			newImage.createGraphics().drawImage(inputImage, 0, 0, Color.BLACK, null);
			File outputfile = new File(errorScreenshotPath + currentTimeStampts + ".jpg");
			ImageIO.write(newImage, "jpg", outputfile);

			byte[] fileContent = FileUtils.readFileToByteArray(outputfile);
			encodedString = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException ex) {
			Log.error("Can not create screenshot", ex.getMessage());
		}
		return encodedString;
	}
}
