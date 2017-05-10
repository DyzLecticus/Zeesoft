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
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[0]);
			} else if (evt.getKeyCode()==KeyEvent.VK_2) {
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[1]);
			} else if (evt.getKeyCode()==KeyEvent.VK_3) {
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[2]);
			} else if (evt.getKeyCode()==KeyEvent.VK_4) {
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[3]);
			} else if (evt.getKeyCode()==KeyEvent.VK_5) {
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[4]);
			} else if (evt.getKeyCode()==KeyEvent.VK_6) {
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[5]);
			} else if (evt.getKeyCode()==KeyEvent.VK_7) {
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[6]);
			} else if (evt.getKeyCode()==KeyEvent.VK_8) {
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[7]);
			} else if (evt.getKeyCode()==KeyEvent.VK_9) {
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[8]);
			} else if (evt.getKeyCode()==KeyEvent.VK_0) {
				controller.getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[9]);
			}
		} else {
			if (!evt.isShiftDown() && !evt.isAltDown()) {
				if (evt.getKeyCode()==KeyEvent.VK_F1) {
					controller.getStateManager().setSelectedTab(this,FrameMain.COMPOSITION);
				} else if (evt.getKeyCode()==KeyEvent.VK_F2) {
					controller.getStateManager().setSelectedTab(this,FrameMain.INSTRUMENTS);
				} else if (evt.getKeyCode()==KeyEvent.VK_F3) {
					controller.getStateManager().setSelectedTab(this,FrameMain.PATTERNS);
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
