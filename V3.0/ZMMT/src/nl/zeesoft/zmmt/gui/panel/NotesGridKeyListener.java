package nl.zeesoft.zmmt.gui.panel;

import java.awt.event.KeyEvent;
import java.util.SortedMap;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.player.InstrumentPlayerKeyListener;

public class NotesGridKeyListener extends InstrumentPlayerKeyListener {
	private PanelPatterns patternPanel = null;
	
	public NotesGridKeyListener(Controller controller,SortedMap<String,Integer> keyCodeNoteNumbers,PanelPatterns patternPanel) {
		super(controller,keyCodeNoteNumbers);
		this.patternPanel = patternPanel;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		super.keyPressed(evt);
		if (!evt.isControlDown() && !evt.isShiftDown() && !evt.isAltDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_DELETE) {
				patternPanel.removeSelectedNotes();
			} else if (evt.getKeyCode()==KeyEvent.VK_INSERT || evt.getKeyCode()==KeyEvent.VK_SPACE) {
				patternPanel.insertSpace();
			}
		}
		if (evt.isAltDown() && evt.isShiftDown() && !evt.isControlDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_UP) {
				patternPanel.shiftSelectedNotesNote(12);
			} else if (evt.getKeyCode()==KeyEvent.VK_DOWN) {
				patternPanel.shiftSelectedNotesNote(-12);
			} else if (evt.getKeyCode()==KeyEvent.VK_RIGHT) {
				patternPanel.shiftSelectedNotesVelocityPercentage(10);
			} else if (evt.getKeyCode()==KeyEvent.VK_LEFT) {
				patternPanel.shiftSelectedNotesVelocityPercentage(-10);
			}
		}
		if (evt.isAltDown() && !evt.isControlDown() && !evt.isShiftDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_UP) {
				patternPanel.shiftSelectedNotesNote(1);
			} else if (evt.getKeyCode()==KeyEvent.VK_DOWN) {
				patternPanel.shiftSelectedNotesNote(-1);
			} else if (evt.getKeyCode()==KeyEvent.VK_RIGHT) {
				patternPanel.shiftSelectedNotesVelocityPercentage(1);
			} else if (evt.getKeyCode()==KeyEvent.VK_LEFT) {
				patternPanel.shiftSelectedNotesVelocityPercentage(-1);
			} else if (evt.getKeyCode()==KeyEvent.VK_1) {
				patternPanel.setSelectedNotesInstrument(0);
			} else if (evt.getKeyCode()==KeyEvent.VK_2) {
				patternPanel.setSelectedNotesInstrument(1);
			} else if (evt.getKeyCode()==KeyEvent.VK_3) {
				patternPanel.setSelectedNotesInstrument(2);
			} else if (evt.getKeyCode()==KeyEvent.VK_4) {
				patternPanel.setSelectedNotesInstrument(3);
			} else if (evt.getKeyCode()==KeyEvent.VK_5) {
				patternPanel.setSelectedNotesInstrument(4);
			} else if (evt.getKeyCode()==KeyEvent.VK_6) {
				patternPanel.setSelectedNotesInstrument(5);
			} else if (evt.getKeyCode()==KeyEvent.VK_7) {
				patternPanel.setSelectedNotesInstrument(6);
			} else if (evt.getKeyCode()==KeyEvent.VK_8) {
				patternPanel.setSelectedNotesInstrument(7);
			} else if (evt.getKeyCode()==KeyEvent.VK_9) {
				patternPanel.setSelectedNotesInstrument(8);
			} else if (evt.getKeyCode()==KeyEvent.VK_0) {
				patternPanel.setSelectedNotesInstrument(9);
			} else if (evt.getKeyCode()==KeyEvent.VK_A) {
				patternPanel.toggelSelectedNotesAccent();
			}
		}
	}
	
	@Override
	protected void playNote(int note, boolean accent) {
		super.playNote(note, accent);
		patternPanel.playNote(note, accent);
	}

	@Override
	protected void stopNote(int note) {
		super.stopNote(note);
		patternPanel.stopNote(note);
	}
}
