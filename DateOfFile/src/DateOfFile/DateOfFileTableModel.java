package DateOfFile;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class DateOfFileTableModel extends AbstractTableModel {

	private ArrayList<MiniMeta> listMiniMeta = new ArrayList<>();
	private Vector<TableModelListener> listeners = new Vector<>();

	private final String[] columnNames = new String[] { "Selection", "Type", "FileName", "CreationTime", "Offset",
			"LastModifiedTime" };
	private final Class[] columnClass = new Class[] { Boolean.class, String.class, String.class, Long.class,
			Long.class, Long.class };

	@Override
	public int getRowCount() {
		return listMiniMeta.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClass[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		MiniMeta mm = listMiniMeta.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return mm.isSelection() ? Boolean.TRUE : Boolean.FALSE;
		case 1:
			return mm.getType();
		case 2:
			return mm.getFileName();
		case 3:
			return Long.valueOf(mm.getCreationTime());
		case 4:
			return Long.valueOf(mm.getOffset());
		case 5:
			return Long.valueOf(mm.getLastModifiedTime());
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		System.out.println("setValueAt");

		MiniMeta mm = listMiniMeta.get(rowIndex);
		switch (columnIndex) {
		case 0:
			mm.isSelection();
		case 1:
			mm.getType();
		case 2:
			mm.getFileName();
		case 3:
			mm.getCreationTime();
		case 4:
			Long.valueOf(mm.getOffset());
		case 5:
			Long.valueOf(mm.getLastModifiedTime());
		}

//		// notify model listeners of cell change
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public ArrayList<MiniMeta> getListMiniMeta() {
		return listMiniMeta;
	}

	public void setListMiniMeta(ArrayList<MiniMeta> listMiniMeta) {
		this.listMiniMeta = listMiniMeta;
	}

	public void addMiniMeta(MiniMeta mm) {
//		int index = listMiniMeta.size();
		listMiniMeta.add(mm);
		// Jetzt werden alle Listeners benachrichtigt

		// Zuerst ein Event, "neue Row an der Stelle index" herstellen
//		TableModelEvent e = new TableModelEvent(this, index, index, TableModelEvent.ALL_COLUMNS,
//				TableModelEvent.INSERT);
		TableModelEvent e = new TableModelEvent(this);

		// Nun das Event verschicken
		for (TableModelListener listener : listeners) {
			listener.tableChanged(e);
		}
	}

	public void setDataOfTableAndUpdate(ArrayList<MiniMeta> listMiniMeta) {
		if (listMiniMeta == null) {
			getListMiniMeta().clear();
		} else {
			setListMiniMeta(listMiniMeta);
		}
		// Jetzt werden alle Listeners benachrichtigt

		// Zuerst ein Event, "neue Row an der Stelle index" herstellen
		TableModelEvent e = new TableModelEvent(this);
		// Nun das Event verschicken
		for (TableModelListener listener : listeners) {
			listener.tableChanged(e);
		}
	}

}
