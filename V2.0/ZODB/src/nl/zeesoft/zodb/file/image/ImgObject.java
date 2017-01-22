package nl.zeesoft.zodb.file.image;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JWindow;

import nl.zeesoft.zodb.Messenger;

public abstract class ImgObject {
	private JWindow 			window 		= null;
	private JPanel				panel		= null;

	private boolean				packed		= false;
	private boolean				rendered	= false;
	
	public ImgObject() {
		panel = new JPanel();
	}
	
	public void setVisible(boolean visible) {
		if (visible && !packed) { 
			pack();
		}
		getWindow().setVisible(visible);
	}

	public void pack() {
		if (!packed) {
			packed = true;
			getWindow().add(panel);
			getWindow().pack();
		}
	}

	public void render() {
		if (!rendered) {
			rendered = true;
		}
	}

	public boolean writePngFile(String fileName) {
		boolean success = false;
		RenderedImage img = getBufferedImage();
		try {
			ImageIO.write(img, "PNG", new File(fileName));
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	public BufferedImage getBufferedImage() {
		if (!rendered) {
			render();
		}
	    BufferedImage r = null;
	    int w = panel.getWidth();
	    int h = panel.getHeight();
	    if (w==0 || h==0) {
	    	Messenger.getInstance().error(this,"Panel size not suitable for image generation: width: " + w + ", height: " + h);
	    } else {
	    	r = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    	Graphics2D g2 = r.createGraphics();
	        panel.print(g2);
	        g2.dispose();
	    }
	    return r;
	}

	protected void setDimension(int width, int height) {
		setPanelDimension(width,height);
	}

	protected void setPanelDimension(int width, int height) {
		panel.setSize(new Dimension(width,height));
		panel.setMinimumSize(new Dimension(width,height));
		panel.setPreferredSize(new Dimension(width,height));
		panel.setMaximumSize(new Dimension(width,height));
	}

	/**
	 * @return the window
	 */
	private JWindow getWindow() {
		if (window==null) {
			window = new JWindow();
			if (panel!=null) {
				window.setBackground(panel.getBackground());
			}
		}
		return window;
	}

	/**
	 * @return the panel
	 */
	protected JPanel getPanel() {
		return panel;
	}

	/**
	 * @param panel the panel to set
	 */
	protected void setPanel(JPanel panel) {
		this.panel = panel;
	}
}
