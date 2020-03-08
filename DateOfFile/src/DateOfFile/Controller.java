package DateOfFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JFileChooser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller implements ActionListener {
	private static final Logger LOG = LogManager.getLogger(DateOfFile.class);
	private DateOfFileModel dofModel;
	private Gui gui;

	public void startGui() throws IOException {
		dofModel = new DateOfFileModel();
		gui = new Gui(this);
		gui.setTitle("DateOfFile  " + dofModel.version.getVesion());
		gui.setVisible(true);
	}

	JFileChooser fc = new JFileChooser();

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("Select")) {
			LOG.info("Select");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				dofModel.setDir(fc.getSelectedFile());
				gui.getjFieldFileOrDir().setText(dofModel.getDir().getAbsolutePath());
				gui.getAnalyseButton().setEnabled(true);
				LOG.info(dofModel.getDir());
				gui.getResetButton().setEnabled(true);
				LOG.info("----------------------------------------------------------------------");
			}

		} else if (event.getActionCommand().equals("Reset")) {
			LOG.info("Reset");
			dofModel.setDir(null);
			gui.getjFieldFileOrDir().setText("");
			dofModel.setLevel(null);
			gui.getJcbLevel().setSelectedIndex(0);
			dofModel.setCorrectionList(null);
			gui.getFileTableModel().setDataOfTableAndUpdate(null);

			gui.getResetButton().setEnabled(false);
			gui.getAnalyseButton().setEnabled(false);
			gui.getProcessButton().setEnabled(false);

		} else if (event.getActionCommand().equals("Analyse")) {
			LOG.info("Analyse");
			dofModel.setCorrectionList(null);
			gui.getJcbLevel().setSelectedIndex(0);
			gui.getFileTableModel().setDataOfTableAndUpdate(null);

			gui.getResetButton().setEnabled(false);
			if (gui.getJcbLevel().getSelectedIndex() == 0) {
				dofModel.setLevel(null);
			} else {
				String stringLevel = gui.getJcbLevel().getSelectedItem().toString().trim();
				if (!stringLevel.isEmpty() && stringLevel.matches("[0-9]+")) {
					dofModel.setLevel(Integer.valueOf(stringLevel));
				} else {
					dofModel.setLevel(1);
				}
			}

			LOG.info("Analyse with level = " + dofModel.getLevel());
			ArrayList<File> allFiles = DateOfFileModel.data.getAllFilesInDir(dofModel.getDir(), dofModel.getDir(),
					dofModel.getLevel());
			TreeMap<String, ArrayList<File>> map = DateOfFileModel.data.sortFileMap(allFiles);
			try {
				ArrayList<MiniMeta> filesMiniMeta = DateOfFileModel.readDateTimeOfFiles(map);
				DateOfFileModel.printDateTimeOfFiles(filesMiniMeta);
				gui.getFileTableModel().setDataOfTableAndUpdate(filesMiniMeta);
				LOG.info("----------------------------------------------------------------------");

				ArrayList<MiniMeta> correctionList = DateOfFileModel.correctionList(filesMiniMeta);
				dofModel.setCorrectionList(correctionList);
				DateOfFileModel.printDateTimeOfFiles(correctionList);
				LOG.info("----------------------------------------------------------------------");
				if (!correctionList.isEmpty()) {
					gui.getProcessButton().setEnabled(true);
				}
			} catch (IOException exc) {
				exc.printStackTrace();
			}
			gui.getResetButton().setEnabled(true);

		} else if (event.getActionCommand().equals("Process")) {
			LOG.info("Process");
			gui.getResetButton().setEnabled(false);
			try {
				DateOfFileModel.correction(dofModel.getCorrectionList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			gui.getResetButton().setEnabled(true);
			LOG.info("-------------------------------- Done --------------------------------");
		} else if (event.getActionCommand().equals("Close")) {
			LOG.info("Close");
			gui.dispose();
		}

	}

	public DateOfFileModel getDofModel() {
		return dofModel;
	}

	public void setDofModel(DateOfFileModel dofModel) {
		this.dofModel = dofModel;
	}

}
