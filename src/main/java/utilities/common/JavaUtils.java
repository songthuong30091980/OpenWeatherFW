package utilities.common;

import java.io.File;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import configuration.ConfigConstant;
import utilities.loggers.Log;

public class JavaUtils {
	private JavaUtils() {}
	public static String stackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append(ConfigConstant.LINE_DOWN);
		}
		return sb.toString();
	}

	public static int randomIndexNumber(int maxNumber) {
		SecureRandom rand = new SecureRandom();
		return rand.nextInt(maxNumber);
	}

	public static String getDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		return dtf.format(LocalDateTime.now());
	}

	public static String getTimeStamps() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmSS");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static void createDirectoryPath(String path) {
		File testDirectory = new File(path);
		if (!testDirectory.exists()) {
			try {
				testDirectory.mkdir();
				Log.info("Directory: " + path + " is created!");
			} catch (Exception ex) {
				Log.error("Failed to create directory: " + path, JavaUtils.stackTraceToString(ex));
			}
		}
	}

}
