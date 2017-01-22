package nl.zeesoft.zadf.gui.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import nl.zeesoft.zadf.gui.GuiPanel;

public class PnlDetail extends GuiPanel {
	
	private JScrollPane				propertyScrollPanel		= null;
	
	public PnlDetail(String name,int row,int column) {
		super(name,row,column);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			propertyScrollPanel = new JScrollPane(getPanel());
			propertyScrollPanel.setMinimumSize(new Dimension(0,0));
			propertyScrollPanel.setPreferredSize(new Dimension(0,0));
			propertyScrollPanel.getVerticalScrollBar().setUnitIncrement(10);
			setComponent(propertyScrollPanel);
		}
	}

	public void setJPanel(JPanel panel) {
		getPanel().removeAll();
		if (panel!=null) {
			getPanel().setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.ipadx = 2;
			gbc.ipady = 2;
			getPanel().setBorder(new EmptyBorder(0, 2, 0, 2));
			getPanel().add(panel,gbc);
		}
		getPanel().revalidate();
		propertyScrollPanel.repaint();
	}
}
