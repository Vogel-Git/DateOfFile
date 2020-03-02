package DateOfFile;

import java.io.File;
import java.io.IOException;
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

public class DateOfFileModel {

	private static final Logger LOG = LogManager.getLogger(DateOfFile.class);

	Version version;
	File dir;
	Integer level;
	ArrayList<MiniMeta> correctionList;

	static ReadData data;
	static DateTime dateTime;
	static Boolean offset;

	public DateOfFileModel() {
		version = new Version();
		data = new ReadData();
		dateTime = new DateTime();
		offset = false;
	}

	/**
	 * Read date data from files
	 *
	 * @param map
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 */
	static ArrayList<MiniMeta> readDateTimeOfFiles(TreeMap<String, ArrayList<File>> map) throws IOException {
		map.remove(data.REST);
		ArrayList<MiniMeta> filesMiniMeta = new ArrayList<>();
		for (Entry<String, ArrayList<File>> entry : map.entrySet()) {
			String key = entry.getKey();
			ArrayList<File> files = entry.getValue();

			Date originalDate = null;
			Calendar originalDateWithOffset = null;

			for (File file : files) {
				MiniMeta fmm = new MiniMeta();
				fmm.setFile(file);
				fmm.setFileName(file.getName());
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
				fmm.setSelection(true);

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

	static void correction(ArrayList<MiniMeta> correctionList) throws IOException {
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

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public ArrayList<MiniMeta> getCorrectionList() {
		return correctionList;
	}

	public void setCorrectionList(ArrayList<MiniMeta> correctionList) {
		this.correctionList = correctionList;
	}

	public static ReadData getData() {
		return data;
	}

	public static void setData(ReadData data) {
		DateOfFileModel.data = data;
	}

	public static DateTime getDateTime() {
		return dateTime;
	}

	public static void setDateTime(DateTime dateTime) {
		DateOfFileModel.dateTime = dateTime;
	}

	public static Boolean getOffset() {
		return offset;
	}

	public static void setOffset(Boolean offset) {
		DateOfFileModel.offset = offset;
	}

}
