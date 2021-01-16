package nl.zeesoft.zdk.http;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.imageio.ImageIO;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.image.ImageIcon;
import nl.zeesoft.zdk.thread.CodeRunner;

public class HttpRequestHandler {
	protected HttpServerConfig	config	= null;
	protected List<CodeRunner>	runners	= new ArrayList<CodeRunner>();
	
	protected HttpRequestHandler(HttpServerConfig config) {
		this.config = config.copy();
	}
	
	protected void handleHeadRequest(HttpRequest request, HttpResponse response) {
		Str error = FileIO.checkFile(request.getFilePath());
		if (error.length()>0) {
			setNotFoundError(response,error);
		}
		setDefaultHeaders(response);
	}
	
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		Str data = new Str();
		if (request.path.equals("/favicon.ico")) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				ImageIO.write(getFavIcon(),"PNG",bos);
				response.bytes = bos.toByteArray();
			} catch (IOException e) {
				setInternalError(response,new Str("Failed to convert favicon image to byte array"));
			}
		} else {
			Str error = data.fromFile(request.getFilePath());
			if (error.length()>0) {
				setNotFoundError(response,error);
			} else {
				response.body = data;
			}
		}
		setDefaultHeaders(response);
	}
	
	protected void handlePostRequest(HttpRequest request, HttpResponse response) {
		String path = request.getFilePath();
		Str error = FileIO.checkDirectory(FileIO.getDirName(path));
		if (error.length()>0) {
			setError(response,HttpURLConnection.HTTP_PRECON_FAILED,error);
		} else {
			Str data = new Str(request.body);
			error = data.toFile(path);
			if (error.length()>0) {
				setError(response,HttpURLConnection.HTTP_PRECON_FAILED,error);
			}
		}
		setDefaultHeaders(response);
	}
	
	protected void handlePutRequest(HttpRequest request, HttpResponse response) {
		handlePostRequest(request,response);
		setDefaultHeaders(response);
	}
	
	protected void handleDeleteRequest(HttpRequest request, HttpResponse response) {
		Str error = FileIO.deleteFile(request.getFilePath());
		if (error.length()>0) {
			setNotFoundError(response,error);
		}
		setDefaultHeaders(response);
	}
	
	protected void handleConnectRequest(HttpRequest request, HttpResponse response) {
		handleGetRequest(request, response);
	}
	
	protected final void handleRequest(HttpRequest request, HttpResponse response) {
		if (request.method.equals("HEAD") && config.isAllowHead()) {
			handleHeadRequest(request,response);
		} else if (request.method.equals("GET") && config.isAllowGet()) {
			handleGetRequest(request,response);
		} else if (request.method.equals("POST") && config.isAllowPost()) {
			handlePostRequest(request,response);
		} else if (request.method.equals("PUT") && config.isAllowPut()) {
			handlePutRequest(request,response);
		} else if (request.method.equals("DELETE") && config.isAllowDelete()) {
			handleDeleteRequest(request,response);
		} else if (request.method.equals("CONNECT") && config.isAllowConnect()) {
			handleConnectRequest(request,response);
		} else {
			Str error = new Str("Method not allowed: ");
			error.sb().append(request.method);
			setError(response,HttpURLConnection.HTTP_BAD_METHOD,error);
		}
	}
	
	protected BufferedImage getFavIcon() {
		ImageIcon icon = ImageIcon.getZeesoftIcon(32);
		return icon.getBufferedImage();
	}
	
	protected void setNotFoundError(HttpResponse response, Str error) {
		setError(response,HttpURLConnection.HTTP_NOT_FOUND,error);
	}
	
	protected void setInternalError(HttpResponse response, Str error) {
		setError(response,HttpURLConnection.HTTP_INTERNAL_ERROR,error);
	}
	
	protected void setError(HttpResponse response, int code, Str error) {
		response.code = code;
		response.message = error.toString();
		response.body = error;
	}
	
	protected void setDefaultHeaders(HttpResponse response) {
		setDefaultHeaders(response, null);
	}

	protected void setDefaultHeaders(HttpResponse response, Date modified) {
		response.headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		response.headers.add("Pragma", "no-cache");
		response.headers.add("Last-Modified", getLastModifiedDateString(modified));
	}
	
	protected final String getLastModifiedDateString() {
		return getLastModifiedDateString(null);
	}
	
	protected final String getLastModifiedDateString(Date modified) {
		if (modified==null) {
			modified = new Date();
		}
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(modified);
	}
}
