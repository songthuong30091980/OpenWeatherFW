package utilities.common;

import java.awt.Color;
import java.util.ArrayList;

public class ColorUtils {

	private ArrayList<ColorName> initColorList() {
		ArrayList<ColorName> colorList = new ArrayList<ColorName>();
		colorList.add(new ColorName(ColorType.GREEN, 34, 174, 80));
		colorList.add(new ColorName(ColorType.RED, 252, 29, 0));
		colorList.add(new ColorName(ColorType.BLUE, 0, 0, 205));
		return colorList;
	}

	public String getRGBFromColorName(String colorName) {
		ArrayList<ColorName> colorList = initColorList();
		for (ColorName c : colorList) {
			if (c.getColorName().equals(colorName)) {
				return String.format("rgb(%s, %s, %s)", c.getR(), c.getG(), c.getB());
			}
		}
		return null;
	}

	public String getColorNameFromRgb(int r, int g, int b) {
		ArrayList<ColorName> colorList = initColorList();
		ColorName closestMatch = null;
		int minMSE = Integer.MAX_VALUE;
		int mse;
		for (ColorName c : colorList) {
			mse = c.computeMSE(r, g, b);
			if (mse < minMSE) {
				minMSE = mse;
				closestMatch = c;
			}
		}

		if (closestMatch != null) {
			return closestMatch.getColorName();
		} else {
			return "No matched color name.";
		}
	}

	public String getColorNameFromHex(int hexColor) {
		int r = (hexColor & 0xFF0000) >> 16;
		int g = (hexColor & 0xFF00) >> 8;
		int b = (hexColor & 0xFF);
		return getColorNameFromRgb(r, g, b);
	}

	public int colorToHex(Color c) {
		return Integer.decode("0x" + Integer.toHexString(c.getRGB()).substring(2));
	}

	public String getColorNameFromColor(Color color) {
		return getColorNameFromRgb(color.getRed(), color.getGreen(), color.getBlue());
	}

	public static String getColorRGBFromColor(Color color) {
		return String.format("rgb(%s, %s, %s)", color.getRed(), color.getGreen(), color.getBlue());
	}

	public class ColorName {
		private int r;
		private int g;
		private int b;
		private String colorTypeName;

		public ColorName(ColorType colorTypeName, int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.colorTypeName = colorTypeName.toString();
		}

		public int computeMSE(int pixR, int pixG, int pixB) {
			return (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b) * (pixB - b)) / 3);
		}

		public int getR() {
			return r;
		}

		public int getG() {
			return g;
		}

		public int getB() {
			return b;
		}

		public String getColorName() {
			return colorTypeName;
		}
	}

	public enum ColorType {
		GREEN, RED, BLUE;
	}
}
