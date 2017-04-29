package nl.zeesoft.zmmt.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControllerKeyListener implements KeyListener {
	private String[]	keys		= new String[17];
	private String[]	orKeys		= new String[24];
	private Controller 	controller	= null;
	private boolean		shiftDown	= false;

	public ControllerKeyListener(Controller controller) {
		this.controller = controller;
		keys[0] = "Q";
		keys[1] = "2";
		keys[2] = "W";
		keys[3] = "3";
		keys[4] = "E";
		keys[5] = "R";
		keys[6] = "5";
		keys[7] = "T";
		keys[8] = "6";
		keys[9] = "Y";
		keys[10] = "7";
		keys[11] = "U";
		keys[12] = "I";
		keys[13] = "9";
		keys[14] = "O";
		keys[15] = "0";
		keys[16] = "P";
		orKeys[12] = "Z";
		orKeys[13] = "S";
		orKeys[14] = "X";
		orKeys[15] = "D";
		orKeys[16] = "C";
		orKeys[17] = "V";
		orKeys[18] = "G";
		orKeys[19] = "B";
		orKeys[20] = "H";
		orKeys[21] = "N";
		orKeys[22] = "J";
		orKeys[23] = "M";
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
		for (int i = 0; i < orKeys.length; i++) {
			if (i < keys.length && keys[i].equals(key)) {
				note = i;
				break;
			} else if (orKeys[i]!=null && orKeys[i].equals(key)) {
				note = i;
				break;
			}
		}
		return note;
	}
}
