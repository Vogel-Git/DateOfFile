package DateOfFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.drew.imaging.ImageProcessingException;

public class DateOfFile {
	private static final Logger LOG = LogManager.getLogger(DateOfFile.class);
	// File dir = new
	// File("C:\Users\Vogel\Desktop\Pojekt_DateOfFile\Test\IMG_20180807_135235.jpg");
	// File dir = new
	// File("C:\Users\Vogel\Desktop\Pojekt_DateOfFile\Test\20180224_195745.mp4");
	// File dir = new
	// File("C:\Users\Vogel\Desktop\Pojekt_DateOfFile\Test\IMG_20190802_201116.MOV");
	// ArrayList<File> allFiles = data.getAllFilesInDir(dir, dir);

	static DateOfFile dateOfFile = new DateOfFile();
	static DateTime dateTime = new DateTime();
	static ReadData data = new ReadData();
	static Version version = new Version();
	static Boolean offset = false;

	/**
	 *
	 * @param args
	 * @throws ImageProcessingException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ImageProcessingException, IOException {
		File dir = null;
		Integer level = null;

		LOG.info("---------------------- " + "DateOfFile " + version.getVesion() + " ----------------------");
		InputStreamReader isr = new InputStreamReader(System.in);
		LOG.info("Input File or Dir :	");
		dir = data.getInputFileOrDir(isr);
		LOG.info(dir);
		if (dir.isDirectory()) {
			LOG.info("Input number of subdirectories to be processed as positiv integer :	");
			level = data.getInputlevel(isr);
		}
		LOG.info("----------------------------------------------------------------------");

		ArrayList<File> allFiles = data.getAllFilesInDir(dir, dir, level);
		TreeMap<String, ArrayList<File>> map = data.sortFileMap(allFiles);
		ArrayList<MiniMeta> filesMiniMeta = readDateTimeOfFiles(map);
		printDateTimeOfFiles(filesMiniMeta);
		ArrayList<MiniMeta> correctionList = correctionList(filesMiniMeta);
		correction(correctionList);

		LOG.info("-------------------------------- Done --------------------------------");

	}

	/**
	 * Read date data from files
	 *
	 * @param map
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 */
	static ArrayList<MiniMeta> readDateTimeOfFiles(TreeMap<String, ArrayList<File>> map)
			throws ImageProcessingException, IOException {
		map.remove(data.REST);
		ArrayList<MiniMeta> filesMiniMeta = new ArrayList<>();
		for (Entry<String, ArrayList<File>> e : map.entrySet()) {
			String key = e.getKey();
			ArrayList<File> files = e.getValue();

			Date originalDate = null;
			Calendar originalDateWithOffset = null;

			for (File file : files) {
				MiniMeta fmm = new MiniMeta();
				fmm.setFile(file);
				if (key.equals(data.MEDIA)) {
					fmm.setTyp(data.MEDIA);
					// AufnahmeDatum:// ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL
					originalDate = dateTime.getDateTimeOriginalExif(file);
				}
				if (key.equals(data.VIDEO)) {
					fmm.setTyp(data.VIDEO);
					// TODO format prueffen 1
					originalDate = dateTime.readVideoAttributeCreationTime(file);
				}

				BasicFileAttributes fatr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				// Map<String, Object> lastAccessTime = Files.readAttributes(file.toPath(),
				// "lastAccessTime");
				if (originalDate != null) {
					// TODO pasiert bei .jeg nicht beie JPG ?
					fmm.setRecordingTime(originalDate.getTime());
					originalDateWithOffset = dateTime.dateWithOffset(originalDate);
					fmm.setOffset(originalDateWithOffset.getTimeInMillis());
				}

				fmm.setCreationTime(fatr.creationTime().toMillis());
				fmm.setLastAccessTime(fatr.lastAccessTime().toMillis());
				fmm.setLastModifiedTime(fatr.lastModifiedTime().toMillis());

				filesMiniMeta.add(fmm);
			}
		}
		return filesMiniMeta;
	}

	static void printDateTimeOfFiles(ArrayList<MiniMeta> filesMiniMeta) {
		if (filesMiniMeta.isEmpty()) {
			LOG.info("File not found");
		}
		for (MiniMeta fmm : filesMiniMeta) {

			System.out.println(
					String.format("%-15s%-40s%-30s", "File", fmm.getFile().getParent(), fmm.getFile().getName()));

			System.out.print(String.format("%-30s", "Name"));
			System.out.print(String.format("%-25s", "CecordingTime"));
			System.out.print(String.format("%-25s", "CreationTime"));
			System.out.print(String.format("%-25s", "LastAccessTime"));
			System.out.print(String.format("%-25s", "LastModifiedTime"));
			System.out.print(String.format("%-25s", "Offset"));
			System.out.println("");

			System.out.print(String.format("%-30s", fmm.file.getName()));
			System.out.print(String.format("%-25s", fmm.recordingTime));
			System.out.print(String.format("%-25s", fmm.creationTime));
			System.out.print(String.format("%-25s", fmm.lastAccessTime));
			System.out.print(String.format("%-25s", fmm.lastModifiedTime));
			System.out.print(String.format("%-25s", fmm.offset));
			System.out.println("");

			System.out.print(String.format("%-30s", fmm.file.getName()));
			System.out.print(String.format("%-25s", fmm.oldRecordingTime));
			System.out.print(String.format("%-25s", fmm.oldCreationTime));
			System.out.print(String.format("%-25s", fmm.oldLastAccessTime));
			System.out.print(String.format("%-25s", fmm.oldLastModifiedTime));
			System.out.print(String.format("%-25s", fmm.offset));
			System.out.println("");

			System.out.print(String.format("%-30s", fmm.file.getName()));
			System.out.print(String.format("%-25s",
					LocalDateTime.ofInstant(Instant.ofEpochMilli(fmm.recordingTime), ZoneId.systemDefault())));
			System.out.print(String.format("%-25s",
					LocalDateTime.ofInstant(Instant.ofEpochMilli(fmm.creationTime), ZoneId.systemDefault())));
			System.out.print(String.format("%-25s",
					LocalDateTime.ofInstant(Instant.ofEpochMilli(fmm.lastAccessTime), ZoneId.systemDefault())));
			System.out.print(String.format("%-25s",
					LocalDateTime.ofInstant(Instant.ofEpochMilli(fmm.lastModifiedTime), ZoneId.systemDefault())));
			System.out.print(String.format("%-25s",
					LocalDateTime.ofInstant(Instant.ofEpochMilli(fmm.offset), ZoneId.systemDefault())));
			System.out.println("");
			System.out.println("--------");

		}
	}

	static ArrayList<MiniMeta> correctionList(ArrayList<MiniMeta> filesMiniMeta) {
		ArrayList<MiniMeta> correctionList = new ArrayList<>();
		if (filesMiniMeta.isEmpty()) {
			LOG.info("File not found");
		} else {
			for (MiniMeta fmm : filesMiniMeta) {
				if (fmm.getCreationTime() != fmm.getRecordingTime()) {
					correctionList.add(fmm);
				}
			}
		}
		return correctionList;
	}

	private static void correction(ArrayList<MiniMeta> correctionList) throws IOException {
		for (MiniMeta ftc : correctionList) {
			if (correctionList.isEmpty()) {
				LOG.info("File not found");
			} else {
				Long time = null;
				if (ftc.getRecordingTime() != 0) {
					time = ftc.getRecordingTime();
				} else if (ftc.getCreationTime() != 0) {
					time = ftc.getCreationTime();
				}

				if (offset) {
					time = ftc.getOffset();
				}

				if (ftc.getLastModifiedTime() != 0 && time > ftc.getLastModifiedTime()) {
					dateTime.modifyTime(ftc.getFile(), ftc.getLastModifiedTime());
				} else if (time != null && time != 0) {
					dateTime.modifyTime(ftc.getFile(), time);
				}
			}
		}
	}



}
