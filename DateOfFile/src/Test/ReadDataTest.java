package Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import DateOfFile.ReadData;

class ReadDataTest {
	ReadData data = new ReadData();

	@Test
	void test() {
//		fail("Not yet implemented");
	}

	@Test
	void testGetAllFilesFromDir() {
		File dir = new File("../DateOfFile/src/Test/Resources/");
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir);
		assert (!allfiles.isEmpty());
		assert (allfiles.size() == 9);
	}

	@Test
	void testGetAllFilesFromFile() {
		File dir = new File("../DateOfFile/src/Test/Resources/IMG_1.jpg");
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir);
		assert (!allfiles.isEmpty());
		assert (allfiles.size() == 1);
	}

	@Test
	void testGetInputDir() throws IOException {
		File dir = new File("../DateOfFile/src/Test/Resources/");
		String initialString = "../DateOfFile/src/Test/Resources/";
		InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
		InputStreamReader isr = new InputStreamReader(targetStream);
		File returnDir = data.getInputFileOrDir(isr);
		assert (returnDir.equals(dir));
	}

	@Test
	void testGetInputFile() throws IOException {
		File file = new File("../DateOfFile/src/Test/Resources/IMG_1.jpg");
		String initialString = "../DateOfFile/src/Test/Resources/IMG_1.jpg";
		InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
		InputStreamReader isr = new InputStreamReader(targetStream);
		File returnFile = data.getInputFileOrDir(isr);
		assert (returnFile.equals(file));
	}

	@Test
	void testGetInputDirAndLevel_null() {
		File dir = new File("../DateOfFile/src/Test/Resources/");
		Integer leverl = null;
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir, leverl);
		assert (!allfiles.isEmpty());
		assert (allfiles.size() == 9);
	}

	@Test
	void testGetInputDirAndLevel_0() {
		File dir = new File("../DateOfFile/src/Test/Resources/");
		Integer leverl = 0;
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir, leverl);
		assert (!allfiles.isEmpty());
		assert (allfiles.size() == 9);
	}

	@Test
	void testGetInputDirAndLevel_1() {
		File dir = new File("../DateOfFile/src/Test/Resources/");
		Integer leverl = 1;
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir, leverl);
		assert (!allfiles.isEmpty());
		assert (allfiles.size() == 5);
	}

	@Test
	void testGetInputDirAndLevel_2() {
		File dir = new File("../DateOfFile/src/Test/Resources/");
		Integer leverl = 2;
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir, leverl);
		assert (!allfiles.isEmpty());
		assert (allfiles.size() == 7);
	}

	@Test
	void testGetInputDirAndLevel_3() {
		File dir = new File("../DateOfFile/src/Test/Resources/");
		Integer leverl = 3;
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir, leverl);
		assert (!allfiles.isEmpty());
		assert (allfiles.size() == 9);
	}

	@Test
	void testGetInputDirAndLevel_4() {
		File dir = new File("../DateOfFile/src/Test/Resources/");
		Integer leverl = 4;
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir, leverl);
		assert (!allfiles.isEmpty());
		assert (allfiles.size() == 9);
	}

	@Test
	void testGetDeepness_0() {
		File sourceDir = new File("../DateOfFile/src/Test/Resources/");
		File currentDir = new File("../DateOfFile/src/Test/Resources/");
		int deepness = data.getDeepness(sourceDir, currentDir);
		assert (deepness == 0);
	}

	@Test
	void testGetDeepness_1() {
		File sourceDir = new File("../DateOfFile/src/Test/Resources/");
		File currentDir = new File("../DateOfFile/src/Test/Resources/O1");
		int deepness = data.getDeepness(sourceDir, currentDir);
		assert (deepness == 1);
	}

	@Test
	void testGetDeepness_2() {
		File sourceDir = new File("../DateOfFile/src/Test/Resources/");
		File currentDir = new File("../Date/Test/Resources/O1");
		int deepness = data.getDeepness(sourceDir, currentDir);
		assert (deepness == -1);
	}

	@Test
	void testGetDeepness_3() {
		File sourceDir = new File("../DateOfFile/src/Test/Resources/");
		File currentDir = new File("../src/Test/Resources/O1");
		int deepness = data.getDeepness(sourceDir, currentDir);
		assert (deepness == -1);
	}

	@Test
	void testGetDeepness_4() {
		File sourceDir = new File("../DateOfFile/src/Test/Test/Resources/O1");
		File currentDir = new File("../DateOfFile/src/Test/Resources/");
		int deepness = data.getDeepness(sourceDir, currentDir);
		assert (deepness == -1);
	}

	@Test
	void testSortFileMap() {
		File dir = new File("../DateOfFile/src/Test/Resources/");
		Integer leverl = 4;
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir, leverl);
		TreeMap<String, ArrayList<File>> map = new TreeMap<>();
		map = data.sortFileMap(allfiles);
		ArrayList<File> listMedia = data.getFilesByKey(map, data.MEDIA);
		ArrayList<File> listVideo = data.getFilesByKey(map, data.VIDEO);
		ArrayList<File> listRest = data.getFilesByKey(map, data.REST);
		assert (listMedia.size() == 6);
		assert (listVideo.size() == 2);
		assert (listRest.size() == 1);
	}

	@Test
	void testGetFileExtension() {
		HashMap<String, File> fileMap = new HashMap<>();
		fileMap.put("txt", new File("kaput_1.txt"));
		fileMap.put("tif", new File("kaput_2.tif"));
		fileMap.put("", new File("kaput_3"));
		for (String key : fileMap.keySet()) {
			File f = fileMap.get(key);
			String fType = data.getFileExtension(f);
			assert (fType.equals(key));
		}
	}

	@Test
	void trstGetInputFileOrDir_Dir() {
		InputStreamReader isr = getInputStreamReader("../DateOfFile/src/Test/Resources");
		try {
			File file = data.getInputFileOrDir(isr);
			if (file.isDirectory()) {
				assert (true);
			}
		} catch (IOException e) {
			assert (false);
			e.printStackTrace();
		}
	}

	@Test
	void trstGetInputFileOrDir_File() {
		InputStreamReader isr = getInputStreamReader("../DateOfFile/src/Test/Resources/IMG_1.jpg");
		try {
			File file = data.getInputFileOrDir(isr);
			if (file.isFile()) {
				assert (true);
			}
		} catch (IOException e) {
			assert (false);
			e.printStackTrace();
		}
	}

	@Test
	void trstGetInputlevel_all() {
		InputStreamReader isr = getInputStreamReader("all");

		try {
			Integer level = data.getInputlevel(isr);
			if (level == null) {
				assert (true);
			} else {
				assert (false);
			}
		} catch (IOException e) {
			assert (false);
			e.printStackTrace();
		}
	}

	@Test
	void trstGetInputlevel_0() {
		InputStreamReader isr = getInputStreamReader("0");

		try {
			Integer level = data.getInputlevel(isr);
			if (level == 0) {
				assert (true);
			} else {
				assert (false);
			}
		} catch (IOException e) {
			assert (false);
			e.printStackTrace();
		}
	}

	@Test
	void trstGetInputlevel_5() {
		InputStreamReader isr = getInputStreamReader("5");

		try {
			Integer level = data.getInputlevel(isr);
			if (level == 5) {
				assert (true);
			} else {
				assert (false);
			}
		} catch (IOException e) {
			assert (false);
			e.printStackTrace();
		}
	}

	@Test
	void trstGetInputlevel_negetiv_Integer() {
		InputStreamReader isr = getInputStreamReader("-4");
		try {
			data.getInputlevel(isr);
		} catch (IOException e) {
			assert (false);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			assert (true);
		}
	}

	@Test
	void trstGetInputlevel_someString() {
		InputStreamReader isr = getInputStreamReader("someString");
		try {
			data.getInputlevel(isr);
		} catch (IOException e) {
			assert (false);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			assert (true);
		}
	}

	// -------------------------------------------------------------------------------//

	private InputStreamReader getInputStreamReader(String isString) {
		byte[] in = isString.getBytes();
		InputStream is = new ByteArrayInputStream(in);
		InputStreamReader isr = new InputStreamReader(is);
		return isr;
	}
}
