package nl.zeesoft.zodb.database.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;

public class GuiKeyListener extends EvtEventPublisher implements KeyListener {
	public static final String CTRL_SHIFT_D_PRESSED 	= "CTRL_SHIFHT_D_PRESSED";
	public static final String CTRL_D_PRESSED 			= "CTRL_D_PRESSED";
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isControlDown() && e.isShiftDown() && e.getKeyCode()==68) {
			publishEvent(new EvtEvent(CTRL_SHIFT_D_PRESSED,this,e));
		} else if (e.isControlDown() && !e.isShiftDown() && e.getKeyCode()==68) {
			publishEvent(new EvtEvent(CTRL_D_PRESSED,this,e));
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// Ignore
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// Ignore
	}
}
