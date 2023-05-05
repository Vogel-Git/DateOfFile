package DateOfFile;

import java.io.File;
import java.util.Optional;

public class UtilityClass {

	private UtilityClass() {
	}

	/**
	 * Get the File extension
	 *
	 * @param file The current File
	 * @return
	 */
	public static String getFileExtension(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return name.substring(lastIndexOf + 1).toLowerCase();
	}

	public static Optional<String> getExtensionByStringHandling(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

	public static String getExtension(String filename) {
		int index = filename.indexOf(".");
		return filename.substring(index + 1);
	}

}
