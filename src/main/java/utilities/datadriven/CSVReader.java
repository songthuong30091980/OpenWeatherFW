package utilities.datadriven;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CSVReader {
	private CSVReader() {
	}

	public static String[][] getCSVData(String csvPath, String cvsSplitBy, int maxRow) {
		String[][] returnData = new String[maxRow][maxRow];
		if (Files.exists(Paths.get(csvPath))) {
			try {
				List<String> allLines = Files.readAllLines(Paths.get(csvPath));
				for (int j = 0; j < allLines.size(); j++) {
					String[] trimArray = allLines.get(j).split(cvsSplitBy);
					returnData[j] = trimArray;
				}
			} catch (Exception ex) {
				throw new AssertionError("Can not read CSV file!!!", ex);
			}
		} else {
			throw new AssertionError("[" + csvPath + "] does not exist!!! Please make sure file path is correct.");
		}
		return returnData;
	}
}
