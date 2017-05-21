package nl.zeesoft.zmmt.gui.panel;

import javax.swing.table.AbstractTableModel;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Pattern;

@SuppressWarnings("serial")
public class PatternGridController extends AbstractTableModel {
	private int					barsPerPattern	= 4;
	private int					beatsPerBar		= 4;
	private int					stepsPerBeat	= 8;

	private Pattern				workingPattern			= null;

	public boolean setLayout(int barsPerPattern,int beatsPerBar,int stepsPerBeat) {
		boolean changed = false;
		if (this.barsPerPattern!=barsPerPattern) {
			this.barsPerPattern = barsPerPattern;
			changed = true;
		}
		if (this.beatsPerBar!=beatsPerBar) {
			this.beatsPerBar = beatsPerBar;
			changed = true;
		}
		if (this.stepsPerBeat!=stepsPerBeat) {
			this.stepsPerBeat = stepsPerBeat;
			changed = true;
		}
		return changed;
	}
	
	public void setWorkingPattern(Pattern workingPattern) {
		this.workingPattern = workingPattern;
	}

	protected int getStepsPerBeat() {
		return stepsPerBeat;
	}
	
	@Override
	public int getColumnCount() {
		return Composition.TRACKS;
	}

	@Override
	public int getRowCount() {
		int r = barsPerPattern;
		if (workingPattern!=null && workingPattern.getBars()>0) {
			r = workingPattern.getBars();
		}
		r = (r * (beatsPerBar * stepsPerBeat));
		return r;
	}

	@Override
	public String getColumnName(int col) {
		return "Track " + String.format("%02d",(col + 1));
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object r = null;
		if (workingPattern!=null) {
			r = workingPattern.getNote((col + 1),(row + 1),1);
		}
		return r;
	}
}
