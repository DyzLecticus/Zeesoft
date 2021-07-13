package nl.zeesoft.zdk;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class TextIcon {
	public String		text		= "z";
	public int			size		= 32;
	public Color 		background	= Color.WHITE;
	
	private JPanel		panel		= null;
	
	public void renderPanel() {
		validateSize();
		JPanel iconPanel = getIconPanel();  
		if (background!=null) {
			iconPanel.setBackground(background);
		}
		iconPanel.setSize(size,size);
		panel = iconPanel;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public BufferedImage getBufferedImage() {
		BufferedImage r = null;
		if (panel!=null) {
			r = new BufferedImage(panel.getWidth(),panel.getHeight(),BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = r.createGraphics();
			panel.print(g2);
			g2.dispose();
		}
		return r;
	}
	
	private void validateSize() {
		size = (size / 16) * 16;
		if (size < 16) {
			size = 16;
		} else if (size>=64) {
			size = 64;
		}
	}
	
	@SuppressWarnings("serial")
	private JPanel getIconPanel() {
		return new JPanel() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(size,size);
			}
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawIcon(g);
			}
		};
	}
	
	private void drawIcon(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, getFontSize()));
		g2.setColor(Color.BLACK);
		g2.drawString(text,getXPos() + getShadow(),getYPos() + getShadow());
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawString(text,getXPos(),getYPos());
	}
	
	private int getXPos() {
		int r = 2;
		if (size/16==2) {
			r = 4;
		} else if (size/16==3) {
			r = 6;
		} else if (size/16==4) {
			r = 8;
		}
		return r;
	}
	
	private int getYPos() {
		int r = 14;
		if (size/16==2) {
			r = 28;
		} else if (size/16==3) {
			r = 44;
		} else if (size/16==4) {
			r = 56;
		}
		return r;
	}
	
	private int getFontSize() {
		int r = 22;
		if (size/16==2) {
			r = 44;
		} else if (size/16==3) {
			r = 68;
		} else if (size/16==4) {
			r = 92;
		}
		return r;
	}
	
	private int getShadow() {
		int r = 1;
		if (size/16==2) {
			r = 2;
		} else if (size/16==3) {
			r = 3;
		} else if (size/16==4) {
			r = 4;
		}
		return r;
	}
}
