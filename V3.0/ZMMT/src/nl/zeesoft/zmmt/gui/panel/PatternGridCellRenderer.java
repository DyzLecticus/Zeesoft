package nl.zeesoft.zmmt.gui.panel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class PatternGridCellRenderer extends DefaultTableCellRenderer {
	private static final Color	BEAT_COLOR_SELECTED 	= new Color(104,104,104);
	private static final Color	BEAT_COLOR_NORMAL 		= new Color(224,224,224);
	private static final Color	COLOR_SELECTED			= new Color(136,136,136);
	private static final Color	COLOR_NORMAL			= new Color(255,255,255);
	
	private PatternGridController controller = null;

	public PatternGridCellRenderer(PatternGridController controller) {
		this.controller = controller;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		Component r = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if ((row % controller.getStepsPerBeat())==0) {
			if (isSelected) {
				r.setBackground(BEAT_COLOR_SELECTED);
			} else {
				r.setBackground(BEAT_COLOR_NORMAL);
			}
		} else {
			if (isSelected) {
				r.setBackground(COLOR_SELECTED);
			} else {
				r.setBackground(COLOR_NORMAL);
			}
		}
		return r;
	}
}
