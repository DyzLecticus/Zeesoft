package nl.zeesoft.zmmt.player;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.SortedMap;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.ControllerKeyListener;

public class InstrumentPlayerKeyListener extends ControllerKeyListener {
	private SortedMap<String,Integer>	keyCodeNoteNumbers	= null;
	
	public InstrumentPlayerKeyListener(Controller controller,SortedMap<String,Integer> keyCodeNoteNumbers) {
		super(controller);
		this.keyCodeNoteNumbers = keyCodeNoteNumbers;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		super.keyPressed(evt);
		if (!evt.isControlDown()) {
			int note = getNoteForKey(evt);
			if (note>=0) {
				boolean accent = evt.isShiftDown();
				if (!accent) {
					accent = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
				}
				getController().playNote(note,accent);
			}
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
		Integer noteNumber = keyCodeNoteNumbers.get(key);
		if (noteNumber!=null) {
			note = noteNumber;
		}
		return note;
	}
}
