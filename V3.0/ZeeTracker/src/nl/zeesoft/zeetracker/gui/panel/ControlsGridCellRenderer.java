package nl.zeesoft.zeetracker.gui.panel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import nl.zeesoft.zmmt.composition.Control;
import nl.zeesoft.zmmt.synthesizer.Instrument;

@SuppressWarnings("serial")
public class ControlsGridCellRenderer extends NotesGridCellRenderer {
	public ControlsGridCellRenderer(NotesGridController controller) {
		super(controller);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		Component r = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value!=null && value instanceof Control) {
			Control ctrl = (Control) value;
			Color color = Instrument.getColorForInstrument(ctrl.instrument);
			r = getLabelForPatternElement(ctrl.toString(),color,row,isSelected,hasFocus);
		} else {
			r.setBackground(getDefaultColor(row,isSelected,hasFocus));
		}
		return r;
	}
}
