package Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.drew.imaging.ImageProcessingException;

import DateOfFile.DateTime;
import DateOfFile.ReadData;

public class DefinedTest {

	ReadData data = new ReadData();
	DateTime dateTime = new DateTime();

//	Aufnahmedatum:  	23.04.2019 16:34		Timestamp: 1556030094000
//	Erstelldatum:		23.04.2019 18:34:54		Timestamp: 1556037294000
//	Änderungsdatum:		23.04.2019 18:34:54
//	Letzter Zugriff:	13.07.2019 23:15:20 	Timestamp: 1563052520000


	@Test
	void testGetAllFilesFromDir() {
		File dir = new File("C:/Users/Vogel/Desktop/Pojekt_DateOfFile/TEST_DEFINIERT/IMG_8392.JPG");
		ArrayList<File> allfiles = data.getAllFilesInDir(dir, dir);
		File file = allfiles.get(0);

		try {
			// AufnahmeDatum: ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL
			Date originalDate = dateTime.getDateTimeOriginalExif(file);
			System.out.println("originalDate = " + originalDate);

			Map<String, Object> attributes_creationTime = Files.readAttributes(file.toPath(), "creationTime");
			FileTime creationTime = (FileTime) attributes_creationTime.get("creationTime");
			System.out.println("creationTime = " + creationTime);

			Map<String, Object> attributes_lastModified = Files.readAttributes(file.toPath(), "lastModifiedTime");
			FileTime lastModifiedTime = (FileTime) attributes_lastModified.get("lastModifiedTime");
			System.out.println("lastModifiedTime = " + lastModifiedTime);

			dateTime.modifyTime(file, originalDate.getTime());

			// AufnahmeDatum: ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL
			originalDate = dateTime.getDateTimeOriginalExif(file);
			System.out.println("originalDate = " + originalDate);

			attributes_creationTime = Files.readAttributes(file.toPath(), "creationTime");
			creationTime = (FileTime) attributes_creationTime.get("creationTime");
			System.out.println("creationTime = " + creationTime);

			attributes_lastModified = Files.readAttributes(file.toPath(), "lastModifiedTime");
			lastModifiedTime = (FileTime) attributes_lastModified.get("lastModifiedTime");
			System.out.println("lastModifiedTime = " + lastModifiedTime);
		} catch (ImageProcessingException | IOException e) {
			e.printStackTrace();
		}
		assert (allfiles.size() == 1);
	}

}

//	originalDate 		= Tue Apr 	23 18:34:54 CEST 2019	| 23-04-19 18:34:54 |	EXIF
//	creationTime 		= 2019-07-13T21:15:20.027679Z	 	|	Kopiert  eglas
//	lastModifiedTime 	= 2019-04-23T16:34:54Z			 	| 23.04.19 16:34:54	|	Z it UTC

//	originalDate 		= Tue Apr 23 18:34:54 CEST 2019		| 23-04-19 18:34:54 |	EXIF
//	creationTime		= 2019-04-23T16:34:54Z
//	lastModifiedTime 	= 2019-04-23T16:34:54Z				| 23.04.19 16:34:54	|	Z it UTC
