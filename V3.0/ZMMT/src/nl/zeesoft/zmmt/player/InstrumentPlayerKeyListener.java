package nl.zeesoft.zmmt.player;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.SortedMap;

import nl.zeesoft.zmmt.gui.Controller;

public class InstrumentPlayerKeyListener implements KeyListener {
	private Controller					controller			= null;
	private SortedMap<String,Integer>	keyCodeNoteNumbers	= null;
	
	public InstrumentPlayerKeyListener(Controller controller,SortedMap<String,Integer> keyCodeNoteNumbers) {
		this.controller = controller;
		this.keyCodeNoteNumbers = keyCodeNoteNumbers;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		if (!evt.isControlDown() && !evt.isAltDown()) {
			int note = getNoteForKey(evt);
			if (note>=0) {
				boolean accent = evt.isShiftDown();
				if (!accent) {
					accent = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
				}
				playNote(note,accent);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		int note = getNoteForKey(evt);
		if (note>=0) {
			stopNote(note);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// Ignore
	}
	
	protected void playNote(int note, boolean accent) {
		controller.playNote(note,accent);
	}

	protected void stopNote(int note) {
		controller.stopNote(note);
	}

	private int getNoteForKey(KeyEvent evt) {
		int keyCode = evt.getKeyCode();
		String key = KeyEvent.getKeyText(keyCode);
		int note = -1;
		Integer noteNumber = keyCodeNoteNumbers.get(key);
		if (noteNumber!=null) {
			note = noteNumber;
		}
		return note;
	}
}
