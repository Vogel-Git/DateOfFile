package DateOfFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Version {

	String getVesion() throws IOException {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Resource/Version.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String v = br.lines().parallel().collect(Collectors.joining("\n"));
		br.close();
		inputStream.close();
		return v;
	}
}
