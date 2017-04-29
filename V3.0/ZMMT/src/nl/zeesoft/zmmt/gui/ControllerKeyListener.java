package nl.zeesoft.zmmt.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControllerKeyListener implements KeyListener {
	private Controller 	controller	= null;
	private boolean		shiftDown	= false;

	public ControllerKeyListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		if (evt.getKeyCode()==KeyEvent.VK_SHIFT) {
			shiftDown = true;
		} else {
			int note = getNoteForKey(evt);
			if (note>=0) {
				controller.playNote(note,shiftDown);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		if (evt.getKeyCode()==KeyEvent.VK_SHIFT) {
			shiftDown = false;
		} else {
			int note = getNoteForKey(evt);
			if (note>=0) {
				controller.stopNote(note);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		// Ignore
	}
	
	private int getNoteForKey(KeyEvent evt) {
		int keyCode = evt.getKeyCode();
		String key = KeyEvent.getKeyText(keyCode);
		int note = -1;
		Integer noteNumber = controller.getSettings().getKeyCodeMidiNotes().get(key);
		if (noteNumber!=null) {
			note = noteNumber;
		}
		return note;
	}
}
