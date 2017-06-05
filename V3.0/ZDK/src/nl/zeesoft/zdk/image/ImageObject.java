package nl.zeesoft.zdk.image;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * Abstract image object.
 * Ensure the panel has content using the getPanel and setPanel methods.
 * Call getBufferedImage to get the rendered image or writePngFile to write the image to a PNG file.
 */
public abstract class ImageObject {
	private JWindow 			window 		= null;
	private JPanel				panel		= null;
	private boolean				packed		= false;
	
	public ImageObject() {
		panel = new JPanel();
	}
	
	/**
	 * Sets the visible state of the image window.
	 * Can be used to test rendering.
	 * 
	 * @param visible Indicates the visible state of the image window
	 */
	public void setVisible(boolean visible) {
		if (visible && !packed) { 
			packed = true;
			getWindow().add(panel);
			getWindow().pack();
		}
		getWindow().setVisible(visible);
	}

	/**
	 * Writes the image to a file.
	 * 
	 * @param fileName The full file name of the file
	 * @return True if writing the file was successful
	 */
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
	
	/**
	 * Returns the buffered image.
	 * 
	 * @return The buffered image
	 */
	public BufferedImage getBufferedImage() {
		BufferedImage r = null;
		int w = panel.getWidth();
		int h = panel.getHeight();
		if (w>0 && h>0) {
			r = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = r.createGraphics();
			panel.print(g2);
			g2.dispose();
		}
		return r;
	}

	/**
	 * Sets the dimension of the panel.
	 * 
	 * @param width The width
	 * @param height The height
	 */
	protected void setPanelDimension(int width, int height) {
		panel.setSize(new Dimension(width,height));
		panel.setMinimumSize(new Dimension(width,height));
		panel.setPreferredSize(new Dimension(width,height));
		panel.setMaximumSize(new Dimension(width,height));
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

	private JWindow getWindow() {
		if (window==null) {
			window = new JWindow();
			if (panel!=null) {
				window.setBackground(panel.getBackground());
			}
		}
		return window;
	}
}
