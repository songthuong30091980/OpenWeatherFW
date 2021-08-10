package utilities.datadriven;

import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import configuration.ConfigConstant;

/**
 * To read JSON files and assign it to framework variables
 *
 */
public class ReadJson {

	// public static JSONObject testdata;
	public static JSONObject testdataJson;

	public static String getTestData(String key) {
		String result = "";
		try {
			JSONObject testdata = (JSONObject) new JSONParser().parse(new FileReader(ConfigConstant.CURRENT_DIR
					+ "\\src\\test\\java\\data\\TestData.json"));
			JSONObject obj = (JSONObject) testdata;
			result = obj.get(key).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getTestData(String root, String key) {
		String result = "";
		try {
			JSONObject testdata = (JSONObject) new JSONParser().parse(new FileReader(ConfigConstant.CURRENT_DIR
					+ "\\src\\test\\java\\data\\TestData.json"));
			JSONObject obj = (JSONObject) testdata;
			JSONObject child = (JSONObject) obj.get(root);
			result = child.get(key).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

}
