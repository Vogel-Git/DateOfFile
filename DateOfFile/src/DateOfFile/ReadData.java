package DateOfFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadData {

	private static final Logger LOG = LogManager.getLogger(DateOfFile.class);

	public final String MEDIA = "Media";
	public final String VIDEO = "Video";
	public final String REST = "Rest";

	private final Set<String> mediaExtensions = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "ico",
			"webp", "pcx", "ai", "eps", "nef", "crw", "cr2", "orf", "arw", "raf", "srw", "x3f", "rw2", "rwl", "tif",
			"tiff", "psd", "dng", "3g2", "pbm", "pnm", "pgm"));

	private final Set<String> videoExtensions = new HashSet<>(Arrays.asList("mp4", "mov", "3gp", "m4v"));

	private ArrayList<File> fileList = new ArrayList<>();

	/**
	 * List recursive all files in source directory
	 *
	 * @param dir
	 * @param sourceDir
	 * @return
	 */
	public ArrayList<File> getAllFilesInDir(File dir, File sourceDir) {
		if (dir.isDirectory()) {
			if (dir.listFiles() != null) {
				for (File file : dir.listFiles()) {
					if (file.isDirectory()) {
						getAllFilesInDir(file, sourceDir);
					} else {
						fileList.add(file);
					}
				}
			}
		} else if (dir.isFile()) {
			fileList.add(dir);
		}

		return fileList;
	}

	/**
	 * List recursive all files in current directory depending on level
	 *
	 * @param dir
	 * @param sourceDir
	 * @param level
	 * @return
	 */
	public ArrayList<File> getAllFilesInDir(File dir, File sourceDir, Integer level) {
		if (level == null || level == 0) {
			return getAllFilesInDir(dir, sourceDir);
		}
		if (level > 0) {
			if (dir.isDirectory()) {
				if (dir.listFiles() != null) {
					for (File file : dir.listFiles()) {
						int currentLevel = getDeepness(sourceDir, file);
						if (file.isDirectory() && currentLevel < level) {
							getAllFilesInDir(file, sourceDir, level);
						} else {
							if (!file.isDirectory()) {
								fileList.add(file);
							}
						}
					}
				}
			} else if (dir.isFile()) {
				fileList.add(dir);
			}
			return fileList;
		}
		return fileList;
	}

	/**
	 * Gets deepness from current directory relative to source Directory
	 *
	 * @param sourceDir
	 * @param currentDir
	 * @return 0 for same directory -1 for error
	 */
	public int getDeepness(File sourceDir, File currentDir) {
		int counter = 0;
		if (!currentDir.isDirectory() || !sourceDir.isDirectory()) {
			return -1;
		}

		String sourcePath = sourceDir.getAbsolutePath();
		String currentDirpath = currentDir.getAbsolutePath();

		if (currentDirpath.contains(sourcePath)) {
			String rest = currentDirpath.replace(sourcePath, "");
			while (rest.indexOf("\\") >= 0) {
				counter++;
				rest = rest.substring(rest.indexOf("\\") + 1, rest.length());
			}

		} else {
			return -1;
		}
		return counter;
	}

	/**
	 * Get input as string from InputStreamReader
	 *
	 * @param isr InputStreamReader
	 * @return
	 * @throws IOException
	 */
	public String getInput(InputStreamReader isr) throws IOException {
		String input = null;
		BufferedReader br = new BufferedReader(isr);
		input = br.readLine();
		return input;
	}

	/**
	 * Get File or Directory from InputStreamReader
	 *
	 * @param isr
	 * @return file or directory
	 * @throws IOException
	 */
	public File getInputFileOrDir(InputStreamReader isr) throws IOException {
		File file = null;
		String input = getInput(isr);

		file = new File(input);
		if (file.isFile() || file.isDirectory()) {
			return file;
		} else {
			LOG.error("The input value is not file or dir: " + input);
			throw new IllegalArgumentException("The input value is not file or dir");
		}
	}

	/**
	 * Get number of sub directories to process as integer from InputStreamReader
	 *
	 * @param isr
	 * @return
	 * @throws IOException
	 */
	public Integer getInputlevel(InputStreamReader isr) throws IOException {
		Integer level = null;
		String input = null;
		BufferedReader br = new BufferedReader(isr);
		input = br.readLine();

		if (input.toLowerCase().contentEquals("all")) {
			level = null;
		} else {
			level = Integer.valueOf(input);
			if (level < 0) {
				LOG.error("The input value is not positive Integer: " + input);
				throw new NumberFormatException("The input value is not positive Integer: " + input);
			}
		}
		LOG.info("level = " + level);
		return level;
	}

	/**
	 * Get the File extension
	 *
	 * @param file
	 * @return
	 */
	public String getFileExtension(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return name.substring(lastIndexOf + 1).toLowerCase();
	}

	/**
	 * Files will be entered into map <String, ArrayList<File> according to the key
	 * MEDIA or REST
	 *
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public TreeMap<String, ArrayList<File>> sortFileMap(ArrayList<File> files) throws IOException {
		TreeMap<String, ArrayList<File>> mapOfFile = new TreeMap<>();
		String key = "";
		for (File file : files) {
			String ext = getFileExtension(file);
			if (videoExtensions.contains(ext.toLowerCase())) {
				key = VIDEO;
			} else if (mediaExtensions.contains(ext.toLowerCase())) {
				key = MEDIA;
			} else {
				key = REST;
			}
			ArrayList<File> target = mapOfFile.get(key);
			if (target == null) {
				target = new ArrayList<>();
			}
			target.add(file);
			mapOfFile.put(key, target);
		}
		return mapOfFile;
	}

	/**
	 * Get ArrayList<File> of files by key from MAP <String, ArrayList<File>>
	 *
	 * @param map
	 * @param key
	 * @return
	 */
	public ArrayList<File> getFilesByKey(TreeMap<String, ArrayList<File>> map, String key) {
		return map.get(key);
	}

}
