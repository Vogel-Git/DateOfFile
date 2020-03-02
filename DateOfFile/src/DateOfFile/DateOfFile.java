package DateOfFile;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.drew.imaging.ImageProcessingException;

public class DateOfFile {
	private static final Logger LOG = LogManager.getLogger(DateOfFile.class);

	/**
	 *
	 * @param args
	 * @throws ImageProcessingException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ImageProcessingException, IOException {
		Controller controller = new Controller();
		controller.startGui();
		LOG.info("---------------------- " + "DateOfFile " + controller.getDofModel().version.getVesion()
				+ " ----------------------");
	}

}
