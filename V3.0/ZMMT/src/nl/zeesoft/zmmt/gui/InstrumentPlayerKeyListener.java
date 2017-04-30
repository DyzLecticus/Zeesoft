package nl.zeesoft.zmmt.gui;

import java.awt.event.KeyEvent;

public class InstrumentPlayerKeyListener extends ControllerKeyListener {
	public InstrumentPlayerKeyListener(Controller controller) {
		super(controller);
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		super.keyPressed(evt);
		int note = getNoteForKey(evt);
		if (note>=0) {
			getController().playNote(note,evt.isShiftDown());
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		super.keyReleased(evt);
		int note = getNoteForKey(evt);
		if (note>=0) {
			getController().stopNote(note);
		}
	}
	
	private int getNoteForKey(KeyEvent evt) {
		int keyCode = evt.getKeyCode();
		String key = KeyEvent.getKeyText(keyCode);
		int note = -1;
		Integer noteNumber = getController().getSettings().getKeyCodeMidiNotes().get(key);
		if (noteNumber!=null) {
			note = noteNumber;
		}
		return note;
	}
}
