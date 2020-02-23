package DateOfFile;

import java.io.File;
import java.nio.file.Path;

public class MiniMeta {

	File file;
	Path path;
	long creationTime;
	long recordingTime; // Exif Aufnahme
	long lastAccessTime;
	long lastModifiedTime;
	long offset;
	String typ; // rest, Media, video;

	long oldLastAccessTime;
	long oldLastModifiedTime;
	long oldCreationTime;
	long oldRecordingTime; // Exif

	public MiniMeta() {

	}

	public MiniMeta(File inputFile) {
		file = inputFile;
		path = file.toPath();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public long getRecordingTime() {
		return recordingTime;
	}

	public void setRecordingTime(long recordingTime) {
		this.recordingTime = recordingTime;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

}
