package nl.zeesoft.zmmt.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import nl.zeesoft.zmmt.syntesizer.Instrument;

public class ControllerKeyListener implements KeyListener {
	private Controller 	controller	= null;

	public ControllerKeyListener(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		if (evt.isControlDown() && !evt.isAltDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_1) {
				controller.selectInstrument(Instrument.INSTRUMENTS[0],this);
			} else if (evt.getKeyCode()==KeyEvent.VK_2) {
				controller.selectInstrument(Instrument.INSTRUMENTS[1],this);
			} else if (evt.getKeyCode()==KeyEvent.VK_3) {
				controller.selectInstrument(Instrument.INSTRUMENTS[2],this);
			} else if (evt.getKeyCode()==KeyEvent.VK_4) {
				controller.selectInstrument(Instrument.INSTRUMENTS[3],this);
			} else if (evt.getKeyCode()==KeyEvent.VK_5) {
				controller.selectInstrument(Instrument.INSTRUMENTS[4],this);
			} else if (evt.getKeyCode()==KeyEvent.VK_6) {
				controller.selectInstrument(Instrument.INSTRUMENTS[5],this);
			} else if (evt.getKeyCode()==KeyEvent.VK_7) {
				controller.selectInstrument(Instrument.INSTRUMENTS[6],this);
			} else if (evt.getKeyCode()==KeyEvent.VK_8) {
				controller.selectInstrument(Instrument.INSTRUMENTS[7],this);
			} else if (evt.getKeyCode()==KeyEvent.VK_9) {
				controller.selectInstrument(Instrument.INSTRUMENTS[8],this);
			} else if (evt.getKeyCode()==KeyEvent.VK_0) {
				controller.selectInstrument(Instrument.INSTRUMENTS[9],this);
			}
		} else {
			if (!evt.isShiftDown() && !evt.isAltDown()) {
				if (evt.getKeyCode()==KeyEvent.VK_F1) {
					controller.switchTo(FrameMain.COMPOSITION);
				} else if (evt.getKeyCode()==KeyEvent.VK_F2) {
					controller.switchTo(FrameMain.INSTRUMENTS);
				} else if (evt.getKeyCode()==KeyEvent.VK_F3) {
					controller.switchTo(FrameMain.PATTERNS);
				}
			}
		}
		if (evt.getModifiersEx()==KeyEvent.CTRL_DOWN_MASK) {
            if (evt.getKeyCode() == KeyEvent.VK_L) {
				controller.loadComposition();
            } else if (evt.getKeyCode() == KeyEvent.VK_U) {
				controller.undoCompositionChanges();
            } else if (evt.getKeyCode() == KeyEvent.VK_S) {
				controller.saveComposition();
            } else if (evt.getKeyCode() == KeyEvent.VK_N) {
				controller.newComposition();
            }					
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// Ignore
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		// Ignore
	}
	
	protected Controller getController() {
		return controller;
	}
}
