package DateOfFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class DateTime {
	final String CREATION_TIME = "Creation Time";

	/**
	 *
	 * @param file
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 */
	public Date getDateTimeOriginalExif(File file) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		if (exif != null) {
			Date date = exif.getDate(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
			return date;
		}
		return null;
	}

	/**
	 *
	 * @param file
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 */
	public Calendar getCalendarDateTimeOriginal(File file) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		// FileSystemDirectory fsd =
		// metadata.getFirstDirectoryOfType(FileSystemDirectory.class);

		if (exif != null) {
			Date date = exif.getDate(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.getTimeZone();

			LocalDateTime timeNow = LocalDateTime.now();
			LocalDateTime offset = LocalDateTime.now(ZoneOffset.UTC);
			System.out.println("Offset = " + offset);
			timeNow.atOffset(ZoneOffset.UTC);

			// Date date_t_z = cal.setTimeZone(TimeZone.this);

			return cal;
		}
		return null;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public Date readVideoAttributeCreationTime(File file) {
		Metadata metadata;
		Date dateCreationTime = null;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
			for (Directory directory : metadata.getDirectories()) {
				for (Tag tag : directory.getTags()) {
					if (CREATION_TIME.equals(tag.getTagName())) {
						String creationTime = tag.getDescription();
						String ext = getExtension(file.getName());
						DateFormat dateFormat;
						if (ext.contentEquals("MOV")) {
							SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss XXX yyyy",
									Locale.GERMAN);
							dateCreationTime = parser.parse(creationTime);
							break;
						} else {
							dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
							dateCreationTime = dateFormat.parse(creationTime);
							break;
						}
					}
				}
				if (directory.hasErrors()) {
					for (String error : directory.getErrors()) {
						System.err.format("ERROR: %s", error + "\n");

					}
				}
				break;
			}
		} catch (ImageProcessingException | IOException | ParseException e) {
			e.printStackTrace();
		}
		return dateCreationTime;
	}

//	TODO Prio 0 Testen
	public Calendar dateWithOffset(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long OffsetRaw = cal.getTimeZone().getRawOffset();
		long OffsetDST = cal.getTimeZone().getDSTSavings(); // Summer / Wider
		long dateMillis = cal.getTimeInMillis() + (OffsetRaw + OffsetDST) * -1;
		cal.setTimeInMillis(dateMillis);
		return cal;
	}

	/**
	 * ??? Just for Test used
	 *
	 * @param file
	 * @return
	 */
	// TODO format prueffen 2
	public FileTime readAttributeCreationTime(File file) {
		Map<String, Object> attributes_creationTime = new HashMap<>();
		try {
			attributes_creationTime = Files.readAttributes(file.toPath(), "creationTime");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// creationTime - EXIFAufnahmedatum
		FileTime creationTime = (FileTime) attributes_creationTime.get("creationTime");
		return creationTime;
	}

	/***
	 *
	 * @param file
	 * @param dateMillis
	 * @throws IOException
	 */
	public void modifyTime(File file, long dateMillis) throws IOException {
		Files.setAttribute(file.toPath(), "creationTime", FileTime.from(Instant.ofEpochMilli(dateMillis)));
		Files.setAttribute(file.toPath(), "lastModifiedTime", FileTime.from(Instant.ofEpochMilli(dateMillis)));

		//////////////
		// System.out.println("-- Some BasicFileAttributes --");
		// System.out.printf("creationTime = %s%n", attributes.creationTime());
		// System.out.printf("lastAccessTime = %s%n", attributes.lastAccessTime());
		// System.out.printf("lastModifiedTime = %s%n", attributes.lastModifiedTime());
		// System.out.printf("size = %s%n", attributes.size());
		// System.out.printf("directory = %s%n", attributes.isDirectory());
		//////////////
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public Map<File, ArrayList<FileTime>> createModifydMap(File file) {

		Map<String, Object> attributes_creationTime = new HashMap<>();
		Map<String, Object> attributes_lastModified = new HashMap<>();
		try {
			attributes_creationTime = Files.readAttributes(file.toPath(), "creationTime");
			attributes_lastModified = Files.readAttributes(file.toPath(), "lastModifiedTime");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<FileTime> timeList = new ArrayList<>();

		// creationTime - EXIFAufnahmedatum
		FileTime creationTime = (FileTime) attributes_creationTime.get("creationTime");
		FileTime lastModifiedTime = (FileTime) attributes_lastModified.get("lastModifiedTime");

		timeList.add(0, creationTime);
		timeList.add(1, lastModifiedTime);

		HashMap<File, ArrayList<FileTime>> mapFileTimes = new HashMap<>();

		mapFileTimes.put(file, timeList);

		return mapFileTimes;
	}

	public Optional<String> getExtensionByStringHandling(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

	public String getExtension(String filename) {
		int index = filename.indexOf(".");
		return filename.substring(index + 1);
	}
}
