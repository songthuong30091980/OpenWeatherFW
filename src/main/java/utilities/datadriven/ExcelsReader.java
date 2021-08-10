package utilities.datadriven;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Platform;

import configuration.ConfigConstant;
import utilities.selenium.ExecutionPlatform;

public class ExcelsReader {
	private ExcelsReader() {}
	// Location of Test data excel file
	private static String testDataExcelPath = null;

	// Excel WorkBook
	private static XSSFWorkbook excelWBook;

	// Excel Sheet
	private static XSSFSheet excelWSheet;

	// Excel cell
	private static XSSFCell cell;

	// Excel row
	private static XSSFRow row;

	// Row Number
	private static int rowNumber;

	// Column Number
	private static int columnNumber;

	// Setter and Getters of row and columns
	public static void setRowNumber(int pRowNumber) {
		rowNumber = pRowNumber;
	}

	public static int getRowNumber() {
		return rowNumber;
	}

	public static void setColumnNumber(int pColumnNumber) {
		columnNumber = pColumnNumber;
	}

	public static int getColumnNumber() {
		return columnNumber;
	}

	// This method has two parameters: "Test data excel file name" and "Excel sheet
	// name"
	// It creates FileInputStream and set excel file and excel sheet to excelWBook
	// and excelWSheet variables.
	public static void setExcelFileSheet(String sheetName, String testDataExcelFileName) {
		// MAC or Windows Selection for excel path
		if (ExecutionPlatform.getCurrentPlatform().equals(Platform.MAC)) {
			testDataExcelPath = ConfigConstant.CURRENT_DIR + "//resources//testdata//";
		} else if (ExecutionPlatform.getCurrentPlatform().equals(Platform.WINDOWS)) {
			testDataExcelPath = ConfigConstant.CURRENT_DIR + "\\resources\\testdata\\";
		}
		try {
			// Open the Excel file
			FileInputStream excelFile = new FileInputStream(testDataExcelPath + testDataExcelFileName);
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheetName);
		} catch (Exception e) {
			try {
				throw (e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// This method reads the test data from the Excel cell.
	// We are passing row number and column number as parameters.
	public static String getCellData(int rowNum, int colNum) {
		try {
			cell = excelWSheet.getRow(rowNum).getCell(colNum);
			DataFormatter formatter = new DataFormatter();
			return formatter.formatCellValue(cell);
		} catch (Exception e) {
			throw (e);
		}
	}

	// This method takes row number as a parameter and returns the data of given row
	// number.
	public static XSSFRow getRowData(int rowNum) {
		try {
			row = excelWSheet.getRow(rowNum);
			return row;
		} catch (Exception e) {
			throw (e);
		}
	}

	// This method gets excel file, row and column number and set a value to the
	// that cell.
	public static void setCellData(String value, String testDataExcelFileName, int rowNum, int colNum) {
		try {
			row = excelWSheet.getRow(rowNum);
			cell = row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(value);
			} else {
				cell.setCellValue(value);
			}
			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(testDataExcelPath + testDataExcelFileName);
			excelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			try {
				throw (e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
