package nl.zeesoft.zmmt.gui.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SequenceGridController extends AbstractTableModel {
	private List<Integer>	sequence	= new ArrayList<Integer>();

	public boolean setSequence(List<Integer> seq) {
		boolean changed = false;
		if (sequencesAreDifferent(sequence,seq)) {
			this.sequence = seq;
		}
		return changed;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return sequence.size();
	}

	@Override
	public String getColumnName(int col) {
		String r = "";
		if (col==0) {
			r = "Sequence";
		}
		return r;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object r = null;
		if (col==0 && sequence.size()>row) {
			r = sequence.get(row);
		}
		return r;
	}
	
	private boolean sequencesAreDifferent(List<Integer> seq1,List<Integer> seq2) {
		boolean r = false;
		int i = 0;
		for (Integer s1: seq1) {
			int p1 = (int) s1;
			int p2 = (int) seq2.get(i);
			if (p1!=p2) {
				r = true;
				break;
			}
			i++;
		}
		return r;
	}
}
