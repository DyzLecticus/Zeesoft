package nl.zeesoft.zdk.app.neural.handlers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.TextIcon;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public class FaviconIcoHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/favicon.ico";
	
	public FaviconIcoHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		response.setContentType("image/x-icon");
		if (request.method.equals(HttpRequest.GET)) {
			TextIcon icon = new TextIcon();
			icon.renderPanel();
			setResponseImage(response, icon.getBufferedImage());
		}
	}
	
	protected void setResponseImage(HttpResponse response, BufferedImage image) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image,"PNG",bos);
			response.body = bos.toByteArray();
		} catch (IOException e) {
			Logger.error(this, "IO exception", e);
			response.setInternalError();
		}
	}
}
