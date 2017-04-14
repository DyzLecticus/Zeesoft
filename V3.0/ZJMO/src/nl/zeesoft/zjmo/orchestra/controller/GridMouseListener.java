package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GridMouseListener implements MouseListener {
	private OrchestraController		controller		= null;
	
	public GridMouseListener(OrchestraController controller) {
		this.controller = controller;
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		if (evt.getClickCount()>1) {
			controller.updateMemberIfSelected();
		}
	}

	@Override
	public void mouseEntered(MouseEvent evt) {
		// Ignore
	}

	@Override
	public void mouseExited(MouseEvent evt) {
		// Ignore
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		// Ignore
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		// Ignore
	}
}
