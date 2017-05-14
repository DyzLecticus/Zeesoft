package nl.zeesoft.zmmt.gui.panel;

import java.awt.event.KeyEvent;
import java.util.SortedMap;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.player.InstrumentPlayerKeyListener;

public class PatternGridKeyListener extends InstrumentPlayerKeyListener {
	private PanelPatterns patternPanel = null;
	
	public PatternGridKeyListener(Controller controller,SortedMap<String,Integer> keyCodeNoteNumbers) {
		super(controller,keyCodeNoteNumbers);
	}

	public void setPatternPanel(PanelPatterns patternPanel) {
		this.patternPanel = patternPanel;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		super.keyPressed(evt);
		if (!evt.isControlDown() && !evt.isShiftDown() && !evt.isAltDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_DELETE) {
				patternPanel.deleteSelectedNotes();
			}
		}
		if (evt.isShiftDown() && !evt.isControlDown() && !evt.isAltDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_INSERT) {
				patternPanel.toggleInsertMode();
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
