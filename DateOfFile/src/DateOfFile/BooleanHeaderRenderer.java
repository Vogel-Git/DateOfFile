package DateOfFile;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * A TableCellRenderer that selects all or none of a Boolean column.
 *
 * @param targetColumn the Boolean column to manage
 */
class BooleanHeaderRenderer extends JCheckBox implements TableCellRenderer {

	private JCheckBox check = new JCheckBox("Selection", null, true);

	private final JLabel label = new JLabel("");

	private JTable table;
	private TableModel tableModel;
	private JTableHeader header;
	private TableColumnModel tcm;
	private int targetColumn;
	private int viewColumn;

	public BooleanHeaderRenderer(JTable table, int targetColumn) {
		super();
		this.table = table;
		tableModel = table.getModel();
		if (tableModel.getColumnClass(targetColumn) != Boolean.class) {
			throw new IllegalArgumentException("Boolean column required.");
		}
		this.targetColumn = targetColumn;
		header = table.getTableHeader();
		tcm = table.getColumnModel();
		this.applyUI();
		this.addItemListener(new ItemHandler());
		header.addMouseListener(new MouseHandler());
		tableModel.addTableModelListener(new ModelHandler());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		check.setOpaque(true);
		check.setFont(table.getFont());
		TableCellRenderer r = table.getTableHeader().getDefaultRenderer();
		JLabel l = (JLabel) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setIcon(new ComponentIcon(check));
		l.setIcon(new ComponentIcon(label));
		l.setText(null);
		l.setAlignmentX((float) 0.5);
		return l;
	}

	private class ItemHandler implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			boolean state = e.getStateChange() == ItemEvent.SELECTED;
			table.getColumnModel().getColumn(0).setHeaderValue(state);
			for (int r = 0; r < table.getRowCount(); r++) {
				table.setValueAt(state, r, viewColumn);
			}
		}
	}

	@Override
	public void updateUI() {
		super.updateUI();
		applyUI();
	}

	private void applyUI() {
		this.setFont(UIManager.getFont("TableHeader.font"));
		this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		this.setBackground(UIManager.getColor("TableHeader.background"));
		this.setForeground(UIManager.getColor("TableHeader.foreground"));
		this.setOpaque(false);
	}

	private class MouseHandler extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			int modelColumn = tcm.getColumn(viewColumn).getModelIndex();
			if (modelColumn == targetColumn) {
				TableCellRenderer r = table.getTableHeader().getDefaultRenderer();
				JLabel l = (JLabel) r.getTableCellRendererComponent(table, "Selection", isSelected(), hasFocus(), -1,
						0);
				check.doClick();
				label.setIcon(new ComponentIcon(check));
				l.setIcon(new ComponentIcon(label));
				doClick();
			}
		}
	}

	private class ModelHandler implements TableModelListener {

		@Override
		public void tableChanged(TableModelEvent e) {
			if (needsToggle()) {
				doClick();
				header.repaint();
			}
		}
	}

	// Return true if this toggle needs to match the model.
	private boolean needsToggle() {
		boolean allTrue = true;
		boolean allFalse = true;
		for (int r = 0; r < tableModel.getRowCount(); r++) {
			boolean b = (Boolean) tableModel.getValueAt(r, targetColumn);
			allTrue &= b;
			allFalse &= !b;
		}
		return allTrue && !isSelected() || allFalse && isSelected();
	}
}