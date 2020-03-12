package DateOfFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

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

		Container content = getContentPane();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Selection Panel
		JPanel selectionPanel = new JPanel();
		JPanel titelPanel = new JPanel();
		JPanel elementsPanel = new JPanel();

		JLabel info = new JLabel("Select Picture Files or Dir to set original Date");
		fcButton = new JButton("Select");
		fcButton.setEnabled(true);
		JLabel levelLable = new JLabel("Level: ");
		String[] cbLeve = new String[] { "All", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		jcbLevel = new JComboBox<>(cbLeve);

		jFieldFileOrDir = new JTextField(30);
		jFieldFileOrDir.setEditable(false);
		selectionPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		selectionPanel.add(titelPanel, c);
		c.gridx = 0;
		c.gridy = 1;
		selectionPanel.add(elementsPanel, c);
		titelPanel.add(info, c);
		elementsPanel.add(fcButton, c);
		elementsPanel.add(levelLable, c);
		elementsPanel.add(jcbLevel, c);
		elementsPanel.add(jFieldFileOrDir, c);

		// Table
		fileTableModel = new DateOfFileTableModel();
		fileTable = new JTable(fileTableModel);
		fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		fileTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		fileTable.getColumnModel().getColumn(1).setPreferredWidth(40);
		fileTable.getColumnModel().getColumn(2).setPreferredWidth(250);
		fileTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		fileTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		fileTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane sp = new JScrollPane(fileTable);
		sp.setBorder(new LineBorder(Color.black, 5));

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
		content.add(new JScrollPane(selectionPanel), BorderLayout.PAGE_START);
		content.add(sp, BorderLayout.CENTER);
		content.add(new JScrollPane(buttonPanel), BorderLayout.PAGE_END);

		// Listeners
		fcButton.addActionListener(listener);
		resetButton.addActionListener(listener);
		analyseButton.addActionListener(listener);
		processButton.addActionListener(listener);
		closeButton.addActionListener(listener);

		this.pack();
		setSize(700, 450);
		this.setVisible(true);

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
