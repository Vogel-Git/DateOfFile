package DateOfFile;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Gui extends JFrame {

	private JComboBox<String> jcbLevel;
	private JTextField jFieldFileOrDir;

	private JTable fileTable = new JTable();
	private DateOfFileTableModel fileTableModel;
	private JButton fcButton;
	private JButton resetButton;
	private JButton analyseButton;
	private JButton processButton;
	private JButton closeButton;

	public Gui(ActionListener listener) {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Select Picture Files or Dir to set original Date");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

		// Main Panel
		JPanel mainPanel = new JPanel();
		JPanel selectionPanel = new JPanel();
		JPanel tablePanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2, 1));

		mainPanel.add(selectionPanel);
		mainPanel.add(tablePanel);

		// Selection Panel
		fcButton = new JButton("Select");
		fcButton.setEnabled(true);
		JLabel levelLable = new JLabel("Level: ");
		String[] cbLeve = new String[] { "All", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		jcbLevel = new JComboBox<>(cbLeve);

		jFieldFileOrDir = new JTextField(30);
		jFieldFileOrDir.setEditable(false);
		selectionPanel.add(fcButton);
		selectionPanel.add(levelLable);
		selectionPanel.add(jcbLevel);
		selectionPanel.add(jFieldFileOrDir);

		// Table
		fileTableModel = new DateOfFileTableModel();
		fileTable = new JTable(fileTableModel);
		JScrollPane jsp = new JScrollPane(fileTable);
		tablePanel.add(jsp);

		// Buttons Panel
		JPanel buttonPanel = new JPanel();
		resetButton = new JButton("Reset");
		resetButton.setEnabled(false);
		analyseButton = new JButton("Analyse");
		analyseButton.setEnabled(false);
		processButton = new JButton("Process");
		processButton.setEnabled(false);
		closeButton = new JButton("Close");
		closeButton.setEnabled(true);
		buttonPanel.add(resetButton);
		buttonPanel.add(analyseButton);
		buttonPanel.add(processButton);
		buttonPanel.add(closeButton);

		// Top-level Layout
		add(titleLabel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		// Listeners
		fcButton.addActionListener(listener);
		resetButton.addActionListener(listener);
		analyseButton.addActionListener(listener);
		processButton.addActionListener(listener);
		closeButton.addActionListener(listener);

//		pack();
		setSize(550, 450);

	}

	public void showMassage(String massage) {
		System.out.println(massage);
	}

	public JComboBox<String> getJcbLevel() {
		return jcbLevel;
	}

	public void setJcbLevel(JComboBox<String> jcbLevel) {
		this.jcbLevel = jcbLevel;
	}

	public JTextField getjFieldFileOrDir() {
		return jFieldFileOrDir;
	}

	public void setjFieldFileOrDir(JTextField jFieldFileOrDir) {
		this.jFieldFileOrDir = jFieldFileOrDir;
	}

	public JTable getFileTable() {
		return fileTable;
	}

	public void setFileTable(JTable fileTable) {
		this.fileTable = fileTable;
	}

	public DateOfFileTableModel getFileTableModel() {
		return fileTableModel;
	}

	public void setFileTableModel(DateOfFileTableModel fileTableModel) {
		this.fileTableModel = fileTableModel;
	}

	public JButton getFcButton() {
		return fcButton;
	}

	public void setFcButton(JButton fcButton) {
		this.fcButton = fcButton;
	}

	public JButton getResetButton() {
		return resetButton;
	}

	public void setResetButton(JButton resetButton) {
		this.resetButton = resetButton;
	}

	public JButton getAnalyseButton() {
		return analyseButton;
	}

	public void setAnalyseButton(JButton analyseButton) {
		this.analyseButton = analyseButton;
	}

	public JButton getProcessButton() {
		return processButton;
	}

	public void setProcessButton(JButton procssButton) {
		processButton = procssButton;
	}

	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}

}
