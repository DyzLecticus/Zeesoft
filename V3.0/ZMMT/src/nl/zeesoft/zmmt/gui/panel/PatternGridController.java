package nl.zeesoft.zmmt.gui.panel;

import javax.swing.table.AbstractTableModel;

import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.syntesizer.InstrumentConfiguration;

@SuppressWarnings("serial")
public class PatternGridController extends AbstractTableModel {
	private int						defaultPatternBars	= 4;
	private int						stepsPerBar			= 16;
	private InstrumentConfiguration	instrument			= null;
	private Pattern					pattern				= null;

	public void setDefaultPatternBars(int defaultPatternBars) {
		this.defaultPatternBars = defaultPatternBars;
	}

	public void setStepsPerBar(int stepsPerBar) {
		this.stepsPerBar = stepsPerBar;
	}
	
	public void setInstrument(InstrumentConfiguration instrument) {
		this.instrument = instrument;
	}
	
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public int getColumnCount() {
		int r = 1;
		if (instrument!=null) {
			r = + instrument.getTracks();
		}
		return r;
	}

	@Override
	public int getRowCount() {
		int r = defaultPatternBars;
		if (pattern!=null) {
			r = pattern.getBars();
		}
		r = (r * stepsPerBar);
		return r;
	}

	@Override
	public String getColumnName(int col) {
		String r = "";
		if (col==0) {
			r = "Step";
		} else {
			r = 	"Track " + col;
		}
		return r;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		Class<?> r = super.getColumnClass(col);
		if (col==0) {
			r = Integer.class;
		}
		return r;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object r = null;
		if (columnIndex==0) {
			r = new Integer(rowIndex + 1);
		} else if (pattern!=null) {
			// TODO: Implement
		}
		return r;
	}
}
