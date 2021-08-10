package utilities.datadriven;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
	private static Properties prop = new Properties();
	
	private PropertiesReader() {}
	
	public static Properties loadPropertiesFile(String propertyFilePath) {
		try (FileInputStream iStr = new FileInputStream(propertyFilePath)){
			prop.load(iStr);
		} catch (IOException e) {
			return null;
		}
		return prop;
	}
}
