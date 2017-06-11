package nl.zeesoft.zmmt.gui.panel;

import java.awt.Component;

import javax.swing.JTable;

import nl.zeesoft.zmmt.composition.Control;

@SuppressWarnings("serial")
public class ControlsGridCellRenderer extends NotesGridCellRenderer {
	public ControlsGridCellRenderer(NotesGridController controller) {
		super(controller);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		Component r = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value!=null && value instanceof Control) {
			/*
			Note note = (Note) value;
			Color color = Instrument.getColorForInstrument(note.instrument);
			JLabel label = new JLabel();
			label.setOpaque(true);
			if (note.step==(row + 1)) {
				label.setText(note.toString());
			}
			label.setBackground(color);
			label.setForeground(Color.BLACK);
			if (hasFocus) {
				label.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
			} else {
				Color borderColor = color;
				if (isSelected) {
					if ((row % controller.getStepsPerBar())==0) {
						borderColor = BAR_COLOR_SELECTED;
					} else if ((row % controller.getStepsPerBeat())==0) {
						borderColor = BEAT_COLOR_SELECTED;
					} else {
						borderColor = Grid.COLOR_SELECTED;
					}
				}
				label.setBorder(BorderFactory.createLineBorder(borderColor));
			}
			r = label;
			 */
		} else {
			r.setBackground(getDefaultColor(row,isSelected,hasFocus));
		}
		return r;
	}
}
