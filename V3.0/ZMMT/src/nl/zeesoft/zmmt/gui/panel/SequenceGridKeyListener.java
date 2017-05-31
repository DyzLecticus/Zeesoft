package nl.zeesoft.zmmt.gui.panel;

import java.awt.event.KeyEvent;
import java.util.SortedMap;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.player.InstrumentPlayerKeyListener;

public class SequenceGridKeyListener extends InstrumentPlayerKeyListener {
	private PanelSequence sequencePanel = null;
	
	public SequenceGridKeyListener(Controller controller,SortedMap<String,Integer> keyCodeNoteNumbers) {
		super(controller,keyCodeNoteNumbers);
	}

	public void setSequencePanel(PanelSequence sequencePanel) {
		this.sequencePanel = sequencePanel;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		super.keyPressed(evt);
		if (!evt.isControlDown() && !evt.isShiftDown() && !evt.isAltDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_INSERT) {
				sequencePanel.insertPatterns();
			} else if (evt.getKeyCode()==KeyEvent.VK_DELETE) {
				sequencePanel.removeSelectedPatterns();
			}
		}
		if (evt.isAltDown() && evt.isShiftDown() && !evt.isControlDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_UP) {
				sequencePanel.shiftSeletectPatterns(10);
			} else if (evt.getKeyCode()==KeyEvent.VK_DOWN) {
				sequencePanel.shiftSeletectPatterns(-10);
			}
		}
		if (evt.isAltDown() && !evt.isControlDown() && !evt.isShiftDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_UP) {
				sequencePanel.shiftSeletectPatterns(1);
			} else if (evt.getKeyCode()==KeyEvent.VK_DOWN) {
				sequencePanel.shiftSeletectPatterns(-1);
			}
		}
	}
}
