package nl.zeesoft.zeetracker.gui.panel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import nl.zeesoft.zmmt.composition.Control;
import nl.zeesoft.zmmt.composition.Note;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.synthesizer.Instrument;

@SuppressWarnings("serial")
public class SequenceGridCellRenderer extends DefaultTableCellRenderer {
	private SequenceGridController	controller	= null;

	public SequenceGridCellRenderer(SequenceGridController controller) {
		this.controller = controller;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		Component r = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		Color color = Grid.COLOR_NORMAL;
		if (isSelected) {
			color = Grid.COLOR_SELECTED;
		}
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(color));
		if (column==0 && value!=null && value instanceof Integer) {
			setText(String.format("%02d",((Integer) value)));
		} else if (column>0 && column<=Instrument.INSTRUMENTS.length) {
			String instrument = Instrument.INSTRUMENTS[(column - 1)];
			Pattern ptn = controller.getPatternForIndex(row);
			if (ptn!=null) {
				boolean hasInstrument = false;
				for (Note note: ptn.getNotes()) {
					if (note.instrument.equals(instrument)) {
						hasInstrument = true;
						break;
					}
				}
				if (hasInstrument) {
					JLabel label = new JLabel();
					label.setOpaque(true);
					label.setBackground(Instrument.getColorForInstrument(instrument));
					label.setForeground(getForeground());
					if (isSelected) {
						label.setBorder(BorderFactory.createLineBorder(color));
					}
					r = label;
				}
			}
		} else if (column>Instrument.INSTRUMENTS.length) {
			Pattern ptn = controller.getPatternForIndex(row);
			if (ptn!=null) {
				int control = 0;
				if (column==(Instrument.INSTRUMENTS.length + 1)) {
					control = Control.EXPRESSION;
				} else if (column==(Instrument.INSTRUMENTS.length + 2)) {
					control = Control.MODULATION;
				} else if (column==(Instrument.INSTRUMENTS.length + 3)) {
					control = Control.FILTER;
				}
				
				boolean hasControl = false;
				for (Control ctrl: ptn.getControls()) {
					if (ctrl.control==control) {
						hasControl = true;
						break;
					}
				}
				if (hasControl) {
					JLabel label = new JLabel();
					label.setOpaque(true);
					label.setFocusable(false);
					label.setBackground(Color.LIGHT_GRAY);
					label.setForeground(getForeground());
					if (isSelected) {
						label.setBorder(BorderFactory.createLineBorder(color));
					}
					r = label;
				}
			}
		}
		return r;
	}
}
