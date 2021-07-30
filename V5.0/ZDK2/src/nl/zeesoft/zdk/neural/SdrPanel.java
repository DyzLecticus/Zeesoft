package nl.zeesoft.zdk.neural;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import nl.zeesoft.zdk.ImgUtil;
import nl.zeesoft.zdk.matrix.Size;

public class SdrPanel {
	private JPanel	panel		= null;
	private Size	size		= null;
	
	public Color 	background	= Color.WHITE;
	public int		border		= 1;
	public int		scale		= 4;
	public Sdr		sdr			= null;
	
	public SdrPanel() {
		
	}
	
	public SdrPanel(Sdr sdr) {
		this.sdr = sdr;
	}
	
	public void render() {
		size = new Size(sdr.length);
		panel = getSdrPanel();  
		panel.setSize(getPanelSize(size.x),getPanelSize(size.y));
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public byte[] getByteArray(boolean mockException) {
		byte[] r = null;
		if (panel!=null) {
			r = ImgUtil.toByteArray(this, panel, mockException);
		}
		return r;
	}
	
	@SuppressWarnings("serial")
	private JPanel getSdrPanel() {
		return new JPanel() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(getPanelSize(size.x),getPanelSize(size.y));
			}
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawSdr(g);
			}
		};
	}
	
	private void drawSdr(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int px = border;
		int py = border;
		int i = 0;
		for (int y = 0; y < size.y; y++) {
			for (int x = 0; x < size.x; x++) {
				g2.setColor(Color.WHITE);
				if (sdr.onBits.contains(i)) {
					g2.setColor(Color.BLUE);
				}
				g2.fillRect(px, py, scale, scale);
				i++;
				px += scale + border;
			}
			py += scale + border;
			px = border;
		}
	}
	
	private int getPanelSize(int size) {
		return (size * scale) + ((size - 1) * border) + border * 2;
	}
}
