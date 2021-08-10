package configuration;

public class ConfigConstant {
	private ConfigConstant() {}
	public static final String DRIVER_FOLDER_PATH = System.getenv("DRIVER_FOLDER_PATH");
	public static final String PATH_USER_HOME = System.getProperty("user.home");
	public static final String CURRENT_DIR = System.getProperty("user.dir");
	public static final String LINE_DOWN = System.getProperty("line.separator");
	public static final String PATH_SEPARATOR = System.getProperty("path.separator");
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	public static final String DRIVER_FOLDER = System.getProperty("user.dir") + FILE_SEPARATOR + "drivers";
}
