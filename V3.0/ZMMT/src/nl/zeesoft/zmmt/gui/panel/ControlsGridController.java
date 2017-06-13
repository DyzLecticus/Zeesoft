package nl.zeesoft.zmmt.gui.panel;

import nl.zeesoft.zmmt.synthesizer.Instrument;

@SuppressWarnings("serial")
public class ControlsGridController extends NotesGridController {
	private int 	selectedControl		= 0;
	
	public boolean setSelectedControl(int selectedControl) {
		boolean changed = false;
		if (this.selectedControl!=selectedControl) {
			this.selectedControl=selectedControl;
			changed = true;
		}
		return changed;
	}
	
	@Override
	public int getColumnCount() {
		return Instrument.INSTRUMENT_SHORTS.length;
	}

	@Override
	public String getColumnName(int col) {
		return Instrument.INSTRUMENT_SHORTS[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object r = null;
		if (getWorkingPattern()!=null) {
			r = getWorkingPattern().getInstrumentControl(Instrument.INSTRUMENTS[col],selectedControl,(row + 1));
		}
		return r;
	}
}
