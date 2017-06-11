package nl.zeesoft.zmmt.gui.panel;

import nl.zeesoft.zmmt.synthesizer.Instrument;

@SuppressWarnings("serial")
public class ControlsGridController extends NotesGridController {
	private String	selectedInstrument	= "";
	private int 	selectedControl		= 0;
	
	public boolean setSelectedInstrumentAndControl(String selectedInstrument,int selectedControl) {
		boolean changed = false;
		if (!this.selectedInstrument.equals(selectedInstrument) ||
			this.selectedControl != selectedControl
			) {
			this.selectedInstrument = selectedInstrument;
			this.selectedControl = selectedControl;
		}
		return changed;
	}
	
	@Override
	public int getColumnCount() {
		return (Instrument.INSTRUMENT_SHORTS.length - 1);
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
			// TODO: Get control
			//r = workingPattern.getNote((col + 1),(row + 1),1);
		}
		return r;
	}
}
