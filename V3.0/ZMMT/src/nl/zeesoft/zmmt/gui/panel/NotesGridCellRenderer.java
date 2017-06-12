package nl.zeesoft.zmmt.gui.panel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import nl.zeesoft.zmmt.composition.Note;
import nl.zeesoft.zmmt.synthesizer.Instrument;

@SuppressWarnings("serial")
public class NotesGridCellRenderer extends DefaultTableCellRenderer {
	protected static final Color	BAR_COLOR_SELECTED 		= new Color(80,80,80);
	protected static final Color	BAR_COLOR_NORMAL 		= new Color(180,180,180);
	protected static final Color	BEAT_COLOR_SELECTED 	= new Color(104,104,104);
	protected static final Color	BEAT_COLOR_NORMAL 		= new Color(224,224,224);
	
	private NotesGridController		controller				= null;

	public NotesGridCellRenderer(NotesGridController controller) {
		this.controller = controller;
	}
	
	protected NotesGridController getController() {
		return controller;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		Component r = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value!=null && value instanceof Note) {
			Note note = (Note) value;
			Color color = Instrument.getColorForInstrument(note.instrument);
			String text = "";
			if (note.step==(row + 1)) {
				text = note.toString();
			}
			r = getLabelForPatternElement(text,color,row,isSelected,hasFocus);
		} else {
			r.setBackground(getDefaultColor(row,isSelected,hasFocus));
		}
		return r;
	}
	
	protected Color getDefaultColor(int row,boolean isSelected,boolean hasFocus) {
		Color color = null;
		if ((row % controller.getStepsPerBar())==0) {
			if (isSelected) {
				color = BAR_COLOR_SELECTED;
			} else {
				color = BAR_COLOR_NORMAL;
			}
		} else if ((row % controller.getStepsPerBeat())==0) {
			if (isSelected) {
				color = BEAT_COLOR_SELECTED;
			} else {
				color = BEAT_COLOR_NORMAL;
			}
		} else {
			if (isSelected) {
				color = Grid.COLOR_SELECTED;
			} else {
				color = Grid.COLOR_NORMAL;
			}
		}
		return color;
	}

	protected JLabel getLabelForPatternElement(String text,Color color,int row,boolean isSelected,boolean hasFocus) {
		JLabel label = new JLabel();
		label.setOpaque(true);
		label.setText(text);
		label.setBackground(color);
		label.setForeground(Color.BLACK);
		if (hasFocus) {
			label.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
		} else {
			Color borderColor = color;
			if (isSelected) {
				if ((row % getController().getStepsPerBar())==0) {
					borderColor = BAR_COLOR_SELECTED;
				} else if ((row % getController().getStepsPerBeat())==0) {
					borderColor = BEAT_COLOR_SELECTED;
				} else {
					borderColor = Grid.COLOR_SELECTED;
				}
			}
			label.setBorder(BorderFactory.createLineBorder(borderColor));
		}
		return label;
	}
}
