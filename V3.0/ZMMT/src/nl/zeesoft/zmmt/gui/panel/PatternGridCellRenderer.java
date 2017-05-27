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
public class PatternGridCellRenderer extends DefaultTableCellRenderer {
	private static final Color	PLAYING_BORDER_COLOR	= Color.BLACK;
	private static final Color	BAR_COLOR_SELECTED 		= new Color(80,80,80);
	private static final Color	BAR_COLOR_NORMAL 		= new Color(180,180,180);
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
		if (value!=null && value instanceof Note) {
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
						borderColor = COLOR_SELECTED;
					}
				}
				if (row == controller.getPlayingStep()) {
					borderColor = PLAYING_BORDER_COLOR;
				}
				label.setBorder(BorderFactory.createLineBorder(borderColor));
			}
			r = label;
		} else {
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
					color = COLOR_SELECTED;
				} else {
					color = COLOR_NORMAL;
				}
			}
			if (row == controller.getPlayingStep()) {
				JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(color);
				label.setBorder(BorderFactory.createLineBorder(PLAYING_BORDER_COLOR));
				r = label;
			} else {
				r.setBackground(color);
			}
		}
		return r;
	}
}
