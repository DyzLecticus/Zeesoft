package nl.zeesoft.zmmt.gui.panel;

import java.awt.event.KeyEvent;
import java.util.SortedMap;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.player.InstrumentPlayerKeyListener;

public class ControlsGridKeyListener extends InstrumentPlayerKeyListener {
	private PanelPatterns patternPanel = null;
	
	public ControlsGridKeyListener(Controller controller,SortedMap<String,Integer> keyCodeNoteNumbers,PanelPatterns patternPanel) {
		super(controller,keyCodeNoteNumbers);
		this.patternPanel = patternPanel;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		super.keyPressed(evt);
		if (!evt.isControlDown() && !evt.isShiftDown() && !evt.isAltDown()) {
			if (evt.getKeyCode()==KeyEvent.VK_DELETE) {
				patternPanel.removeSelectedControls();
			} else if (evt.getKeyCode()==KeyEvent.VK_INSERT || evt.getKeyCode()==KeyEvent.VK_SPACE) {
				patternPanel.insertSpace();
			}
		}
	}
}
