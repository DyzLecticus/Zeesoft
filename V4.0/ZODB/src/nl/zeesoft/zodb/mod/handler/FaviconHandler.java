package nl.zeesoft.zodb.mod.handler;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.image.ImageIcon;
import nl.zeesoft.zodb.Config;

public class FaviconHandler extends HandlerObject {
	private ImageIcon icon = null;
	
	public FaviconHandler(Config config) {
		super(config,null,"/favicon.ico");
		setContentType("image/png");
		ZDKFactory factory = new ZDKFactory();
		icon = factory.getZeesoftIcon32();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeaders(response);
		try {
			OutputStream out = response.getOutputStream();
			ImageIO.write(icon.getBufferedImage(),"png",out);
		} catch (IOException e) {
			getConfiguration().error(this,"I/O exception",e);
		}
	}
}
