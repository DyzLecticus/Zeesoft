package nl.zeesoft.zodb.file.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class ImgIconLabel extends ImgObject {

	public ImgIconLabel(String text, int size) {
		initialize(text,size,null);
	}
	
	public ImgIconLabel(String text, int size, Color background) {
		initialize(text,size,background);
	}

	private void initialize(String text, int size, Color background) {
		final String txt = text.toLowerCase();
		final int siz = size;

		int xPos = 2;
		int yPos = 6;
		int fontSize = 10;
		int shadow = 1;
		if (size>=16) {
			xPos = 2;
			yPos = 14;
			fontSize = 22;
			shadow = 1;
		}
		if (size>=32) {
			xPos = 4;
			yPos = 28;
			fontSize = 44;
			shadow = 2;
		}
		if (size>=64) {
			xPos = 8;
			yPos = 56;
			fontSize = 92;
			shadow = 4;
		}
		final Font font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
		final int x = xPos;
		final int y = yPos;
		final int s = shadow;
		
		@SuppressWarnings("serial")
		JPanel labelPanel = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
		        return new Dimension(siz,siz);
		    }
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;

				g2.setFont(font);
				
				g2.setColor(Color.BLACK);
				g2.drawString(txt,x + s,y + s);
				
				g2.setColor(Color.LIGHT_GRAY);
				g2.drawString(txt,x,y);
			}
		};
		if (background!=null) {
			labelPanel.setBackground(background);
		}
		labelPanel.setSize(size,size);
		setPanel(labelPanel);
	}
}
