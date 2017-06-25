package nl.zeesoft.zmmt.gui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MixerStrip extends JPanel {
	private static final int	WIDTH	= 8;
	private static final int	HEIGHT	= 218;
	
	private Color				color	= null;
	private int					value	= 0;

	public MixerStrip(Color color) {
		this.color = color;
		this.setOpaque(true);
		this.setBackground(Color.BLACK);
		this.setMinimumSize(new Dimension(WIDTH,HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH,HEIGHT));
	}
	
	public void setValue(int value) {
		if (this.value!=value) {
			this.value = value;
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int colorNum = -1;
		if (value>0) {
			colorNum = (((value * 100) / 127) / 10) - 1;
		}
		for (int i = 0; i < 10; i++) {
			Graphics2D g2 = (Graphics2D) g;
			int y = (HEIGHT + 2) - (22 * (i + 1));
			if (i<=colorNum) {
				g2.setPaint(color);
			} else {
				g2.setPaint(Color.GRAY);
			}
			g2.fillRect(0,y,10,20);
		}
	}

}
