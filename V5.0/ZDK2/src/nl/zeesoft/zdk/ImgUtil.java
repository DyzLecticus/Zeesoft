package nl.zeesoft.zdk;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImgUtil {
	public static BufferedImage toBufferedImage(JPanel panel) {
		BufferedImage r = new BufferedImage(panel.getWidth(),panel.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = r.createGraphics();
		panel.print(g2);
		g2.dispose();
		return r;
	}
	
	public static byte[] toByteArray(Object caller, JPanel panel, boolean mockException) {
		byte[] r = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			if (mockException) {
				throw(new IOException());
			}
			ImageIO.write(toBufferedImage(panel),"PNG",bos);
			r = bos.toByteArray();
		} catch (IOException e) {
			Logger.error(caller, "IO exception", e);
		}
		return r;
	}
}
