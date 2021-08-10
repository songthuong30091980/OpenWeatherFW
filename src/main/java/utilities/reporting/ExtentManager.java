package utilities.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentLoggerReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import configuration.ConfigConstant;
import configuration.ConfigReader;
import utilities.common.JavaUtils;
import utilities.loggers.Log;

public class ExtentManager {
	private ExtentManager() {
	}

	private static ExtentReports tlExtent;
	private static ExtentLoggerReporter extentLogger;
	private static ExtentSparkReporter extentSparkReport;
	private static String reportPath = ConfigReader.getExtentReportPath();

	public static void createInstance(String testSuiteName, String browserName) {
		String reportTitle = null;
		if (testSuiteName.equals("Default suite") || testSuiteName.equals("Surefire suite")) {
			testSuiteName = "LocalRunReport";
			reportTitle = "Automation Test Run Report";
		}
		String reportFile = setReportFileLocation(testSuiteName, browserName);

		ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(reportFile);
		extentHtmlReporter.config().setReportName(reportTitle);
		extentHtmlReporter.config().setTheme(Theme.STANDARD);
		extentHtmlReporter.config().enableTimeline(true);
		extentHtmlReporter.config().setAutoCreateRelativePathMedia(true);
		extentHtmlReporter.config().setCSS("css-string");
		extentHtmlReporter.config().setEncoding("utf-8");
		extentHtmlReporter.config().setJS("js-string");
		extentHtmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
		tlExtent = new ExtentReports();
		tlExtent.attachReporter(extentHtmlReporter, extentSparkReport, extentLogger);
	}

	public static ExtentReports getExtentReport() {
		return tlExtent;
	}

	private static String setReportFileLocation(String testSuiteName, String browserName) {
		String reportFileLocation = null;
		String currentDateTime = JavaUtils.getDateTime();
		JavaUtils.createDirectoryPath(reportPath);
		extentLogger = new ExtentLoggerReporter(
				reportPath + ConfigConstant.FILE_SEPARATOR + "log" + ConfigConstant.FILE_SEPARATOR);
		extentSparkReport = new ExtentSparkReporter(
				reportPath + ConfigConstant.FILE_SEPARATOR + "sparkreport" + ConfigConstant.FILE_SEPARATOR);
		reportFileLocation = reportPath + ConfigConstant.FILE_SEPARATOR + testSuiteName + "_" + browserName + "_"
				+ currentDateTime + ".html";
		Log.info("ExtentReport Path: " + reportFileLocation + "\n");

		return reportFileLocation;
	}

}
