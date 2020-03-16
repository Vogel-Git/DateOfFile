package DateOfFile;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

class ComponentIcon implements Icon {
	private final Component cmp;

	protected ComponentIcon(Component cmp) {
		this.cmp = cmp;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		SwingUtilities.paintComponent(g, cmp, c.getParent(), x, y, getIconWidth(), getIconHeight());
	}

	@Override
	public int getIconWidth() {
		return cmp.getPreferredSize().width;
	}

	@Override
	public int getIconHeight() {
		return cmp.getPreferredSize().height;
	}
}