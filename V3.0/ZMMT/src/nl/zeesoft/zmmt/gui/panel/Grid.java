package nl.zeesoft.zmmt.gui.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import javax.swing.JTable;

import nl.zeesoft.zmmt.composition.Composition;

@SuppressWarnings("serial")
public class Grid extends JTable {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int row = getPlayingRow();
		if (row>=0) {
			Graphics2D g2 = (Graphics2D) g;
			Stroke oldStroke = g2.getStroke();
			g2.setStroke(new BasicStroke(2));
			Rectangle r = getPlayingRectangle(row);
			g2.setPaint(Color.BLACK);
			g2.drawRect(r.x,r.y,r.width,r.height);
			g2.setStroke(oldStroke);
		}
	}

	protected int getPlayingRow() {
		int r = -1;
		if (getModel() instanceof PatternGridController) {
			PatternGridController controller = (PatternGridController) getModel();
			r = (controller.getPlayingStep() - 1);
		} else if (getModel() instanceof SequenceGridController) {
			SequenceGridController controller = (SequenceGridController) getModel();
			r = controller.getPlayingIndex();
		}
		return r;
	}
	
	protected Rectangle getPlayingRectangle(int row) {
		Rectangle r = new Rectangle();
		Rectangle cell = getCellRect(row,0,false);
		r.setLocation(cell.getLocation());
		r.setLocation(cell.x,(cell.y - 1));
		r.setSize((((cell.width + 1) * Composition.TRACKS) - 1),(cell.height + 1));
		return r;
	}
}
