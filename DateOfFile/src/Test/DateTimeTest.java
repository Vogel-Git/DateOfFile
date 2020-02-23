package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import DateOfFile.DateTime;

class DateTimeTest {

	DateTime dateTime = new DateTime();
	File file = null;
	long startTime = 1550003333000l; // 2019-02-12T20:28:53+00:00

	@BeforeEach
	void setUp() {
		file = new File("../DateOfFile/src/Test/Resources/IMG_1.jpg");
		try {
			dateTime.modifyTime(file, startTime);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterEach
	void clean() {
		try {
			if (file != null) {
				dateTime.modifyTime(file, startTime);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * setUp() has changed creationTime & lastModifiedTime from file to
	 * 1550003333000 Milliseconds 2019-02-12T20:28:53+00:00 bat
	 * ExifDirectoryBase.TAG_DATETIME_ORIGINAL will not be changed
	 */
	@Test
	void test_getDateTimeOriginalExif() {
		long expectedMiliseconds = 1555493794000l; // 17.04.2019 09:36:34 (UTC) // Wed Apr 17 11:36:34 CEST 2019
		try {
			FileTime creation = (FileTime) Files.readAttributes(file.toPath(), "creationTime").get("creationTime");
			Date originalDate = dateTime.getDateTimeOriginalExif(file); // Aufnahmedetum EXIF
			if (originalDate.getTime() == expectedMiliseconds && creation.toMillis() == startTime) {
				assert (true);
			} else {
				assert (false);
			}
		} catch (ImageProcessingException | IOException e) {
			assert (false);
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	@Test
	void test_modifyTime___file_is_null_Exception() {
		file = null;
		long dateMillis = 0;

		try {
			dateTime.modifyTime(file, dateMillis);
			assert (false);
		} catch (NullPointerException | IOException e) {
			assert (true);
		}
	}

	/**
	 * setUp() has changed creationTime & lastModifiedTime from file to
	 * 1550003333000 Milliseconds 2019-02-12T20:28:53+00:00. creationTime &
	 * lastModifiedTime will by chanced to ExifDirectoryBase.TAG_DATETIME_ORIGINAL
	 *
	 * File creationTime before - 2019-02-12T20:28:53Z File creationTime after -
	 * 2019-04-17T09:36:34Z
	 */
	@Test
	void test_modifyTime_creationTime_lastModifiedTime() {
		try {
			System.out.println("------------ testModifyTime_creationTime_lastModifiedTime() ------------");
			Date originalDate = dateTime.getDateTimeOriginalExif(file); // Aufnahmedetum EXIF
			long dateMillis = originalDate.getTime(); // 2019-04-17T09:36:34Z

			System.out.println(
					String.format("%-10s%-1s", "Aefore - ", Files.readAttributes(file.toPath(), "creationTime")));
			dateTime.modifyTime(file, dateMillis);
			System.out.println(
					String.format("%-10s%-1s", "After  - ", Files.readAttributes(file.toPath(), "creationTime")));

			FileTime creation = (FileTime) Files.readAttributes(file.toPath(), "creationTime").get("creationTime");
			FileTime lastModified = (FileTime) Files.readAttributes(file.toPath(), "lastModifiedTime")
					.get("lastModifiedTime");
			long creationTime = creation.toMillis();
			long lastModifiedTime = lastModified.toMillis();

			if ((dateMillis == lastModifiedTime) && (dateMillis == creationTime)) {
				assert (true);
			} else {
				assert (false);
			}

		} catch (NullPointerException | IOException | ImageProcessingException e) {
			assert (false);
			e.printStackTrace();
		}
		System.out.println("------------------------------------------------------------------------");
	}

	/**
	 * ModifydMap will by created for file
	 */
	@Test
	void test_createModifydMap() {
		Map<File, ArrayList<FileTime>> map = dateTime.createModifydMap(file);
		ArrayList<FileTime> list = map.get(file);

		FileTime fileTime = list.get(0);
		long time = fileTime.to(TimeUnit.MILLISECONDS);

		if (map.keySet().contains(file) && time == startTime) {
			assert (true);
		} else {
			assert (false);
		}
	}

	@Test
	void test_dateWithOffset() {
		System.out.println("------------ testDateWithOffset() ------------");
		File file = new File("../DateOfFile/src/Test/Resources/IMG_1.jpg");
		FileTime creationTime = null;
		FileTime lastModified = null;
		try {
			creationTime = (FileTime) Files.readAttributes(file.toPath(), "creationTime").get("creationTime");
			lastModified = (FileTime) Files.readAttributes(file.toPath(), "lastModifiedTime").get("lastModifiedTime");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(String.format("%-30s%-1s", "Fiel Name", file));
		System.out.println(String.format("%-30s%-1s", "CreationTime", creationTime));
		System.out.println(String.format("%-30s%-1s", "LastModified", lastModified));
		System.out.println(String.format("%-30s%-1s", "CreationTime Millis", creationTime.toMillis()));
		System.out.println(String.format("%-30s%-1s", "LastModified Millis", lastModified.toMillis()));
		System.out.println("------------");

		Date date = new Date(creationTime.toMillis());
		Calendar dateWithOffset = dateTime.dateWithOffset(date);
		System.out.println(String.format("%-30s%-1s", "Date date", date));
		System.out.println(String.format("%-30s%-1s", "Calendar dateWithOffset", dateWithOffset.getTime()));
		System.out.println("------------");

		System.out.println(String.format("%-30s%-1s", "datet Millis", date.getTime()));
		System.out.println(String.format("%-30s%-1s", "dateWithOffset Millis", dateWithOffset.getTimeInMillis()));

		assertEquals(creationTime, lastModified);
		assertEquals(creationTime.toMillis(), lastModified.toMillis());

		assertNotEquals(date, dateWithOffset.getTime());
		assertNotEquals(date.getTime(), dateWithOffset.getTimeInMillis());
		System.out.println("------------------------------------------------------------------------");
	}

	@Test
	void test_getDateTimeOriginalExif_vs_readAttributeCreationTime() {
		System.out.println(
				"----------------------------- TimeOriginal_VS_readAttributeCreationTime -----------------------------");
		file = new File("C:/Users/Vogel/Desktop/Pojekt_DateOfFile/Test/spazi.mp4");
		try {
			Date originalDateTime = dateTime.getDateTimeOriginalExif(file); // Aufnahmedetum
			if (originalDateTime != null) {
				long originalDateTimeMillis = originalDateTime.getTime();
				System.out.println(String.format("%-40s%-2s%-1s", "ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL ", "=",
						originalDateTime));
				System.out.println(String.format("%-40s%-2s%-1s", "ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL ", "=",
						originalDateTimeMillis));
			} else {
				System.out.println(
						String.format("%-45s%-2s%-1s", "ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL ", "=", null));
			}

			FileTime creationTime = dateTime.readAttributeCreationTime(file);
			if (creationTime != null) {
				System.out.println(String.format("%-45s%-2s%-1s", "creationTime ", "=", creationTime));
				long creationTimeMillis = creationTime.toMillis();
				System.out.println(String.format("%-45s%-2s%-1s", "creationTimeMillis", "=", creationTimeMillis));
			} else {
				System.out.println("creationTimeMillis = Null");
				System.out.println(String.format("%-45s%-2s%-1s", "creationTimeMillis", "=", null));
			}

			if (originalDateTime != null || creationTime != null) {
				assert (true);
			} else {
				assert (false);
			}

		} catch (NullPointerException | IOException | ImageProcessingException e) {
			assert (false);
			e.printStackTrace();
		}
		System.out.println("------------------------------------------------------------------------");
	}

	@Test
	void testGetDate() {
		System.out.println("----------------------------- spazi.mp4i -----------------------------");
		file = new File("C:/Users/Vogel/Desktop/Pojekt_DateOfFile/Test/spazi.mp4");
		Metadata metadata;
		try {
			metadata = ImageMetadataReader.readMetadata(file);

			for (Directory directory : metadata.getDirectories()) {
				for (Tag tag : directory.getTags()) {
					if ("Creation Time".equals(tag.getTagName())) {
						String creationTime = tag.getDescription();
						System.out.println(String.format("%-30s%-2s%-35s%-1s",
								directory.getName() + " - " + tag.getTagName(), "=", creationTime, "String"));

						DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
						try {
							Date dateCreationTime = dateFormat.parse(creationTime);
							System.out.println(
									String.format("%-30s%-2s%-35s%-1s", "dateCreationTime ", "=", dateCreationTime,
											"Date"));

							long dateMiliseconds = dateCreationTime.getTime();
							System.out.println(
									String.format("%-30s%-2s%-35s%-1s", "Miliseconds ", "=", dateMiliseconds, "long"));

						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if ("File Modified Date".equals(tag.getTagName())) {
						System.out.println(String.format("%-30s%-2s%-35s%-1s",
								directory.getName() + " - " + tag.getTagName(), "=", tag.getDescription(), "String"));

					}

					System.out.format("[%s] - %s = %s", directory.getName(), tag.getTagName(),
							tag.getDescription() + "\n");

				}
				if (directory.hasErrors()) {
					for (String error : directory.getErrors()) {
						System.err.format("ERROR: %s", error + "\n");
					}
				}
			}
		} catch (ImageProcessingException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("---------------------------------------------------------------------------------------");
	}

	@Test
	void testReadVideoAttributeCreationTime_MP4() {
		// 10.08.2019 12:50:12 - 1565445012000 (Ortszeit)
		// 10.08.2019 12:50:12 - 1565434221000

		long expectedMiliseconds = 1565434221000l;
		long dateMiliseconds = 0;
		file = new File("../DateOfFile/src/Test/Resources/VID_20190810_135021.mp4");

		Date date = dateTime.readVideoAttributeCreationTime(file);
		dateMiliseconds = date.getTime();
		if (expectedMiliseconds == dateMiliseconds) {
			assert (true);
		} else {
			assert (false);
		}
	}

	@Test
	void testReadVideoAttributeCreationTime_MOV() {
		// 02.08.2019 20:11:12 - 1564769472000 (Ortszeit)
		// 02.08.2019 20:11:12 - 1564765876000

		long expectedMiliseconds = 1564765876000l;
		long dateMiliseconds = 0;
		file = new File("../DateOfFile/src/Test/Resources/IMG_20190802_201116.MOV");

		Date date = dateTime.readVideoAttributeCreationTime(file);
		dateMiliseconds = date.getTime();
		if (expectedMiliseconds == dateMiliseconds) {
			assert (true);
		} else {
			assert (false);
		}
	}

	@Test
	void test() {
		assert (true);
		// fail("Not yet implemented");
	}

	@Test
	void test_2() {

		int a = 1;
		if (a == 1) {
			assert (true);
		} else {
			assert (false);
		}

		// fail("Not yet implemented");
	}

}
