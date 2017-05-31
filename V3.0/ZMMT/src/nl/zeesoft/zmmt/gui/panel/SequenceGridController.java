package nl.zeesoft.zmmt.gui.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import nl.zeesoft.zmmt.synthesizer.Instrument;

@SuppressWarnings("serial")
public class SequenceGridController extends AbstractTableModel {
	private List<Integer>	workingSequence		= new ArrayList<Integer>();
	private int				playingIndex		= -1;

	@Override
	public int getColumnCount() {
		return Instrument.INSTRUMENTS.length;
	}

	@Override
	public int getRowCount() {
		return workingSequence.size();
	}

	@Override
	public String getColumnName(int col) {
		String r = "";
		if (col==0) {
			r = "PTN";
		} else {
			r = Instrument.INSTRUMENT_SHORTS[col - 1];
			if (r.length()<3) {
				r = r + " ";
			}
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
		if (col==0 && workingSequence.size()>row) {
			r = workingSequence.get(row);
		}
		return r;
	}
	
	protected boolean setWorkingSequence(List<Integer> seq) {
		boolean changed = false;
		if (sequencesAreDifferent(workingSequence,seq)) {
			changed = true;
			this.workingSequence = new ArrayList<Integer>(seq);
		}
		return changed;
	}

	protected int getPlayingIndex() {
		return playingIndex;
	}

	protected void setPlayingIndex(int playingIndex) {
		this.playingIndex = playingIndex;
	}

	private boolean sequencesAreDifferent(List<Integer> seq1,List<Integer> seq2) {
		boolean r = false;
		int i = 0;
		if (seq1!=seq2) {
			if (seq1.size()!=seq2.size()) {
				r = true;
			} else {
				for (Integer s1: seq1) {
					int p1 = (int) s1;
					int p2 = (int) seq2.get(i);
					if (p1!=p2) {
						r = true;
						break;
					}
					i++;
				}
			}
		}
		return r;
	}
}
