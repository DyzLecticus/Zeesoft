package nl.zeesoft.zdk.http;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;

public class HttpRequestHandler {
	protected HttpServerConfig	config	= null;
	protected List<CodeRunner>	runners	= new ArrayList<CodeRunner>();
	
	protected HttpRequestHandler(HttpServerConfig config) {
		this.config = config.copy();
	}
	
	protected void handleHeadRequest(HttpRequest request, HttpResponse response) {
		Str error = FileIO.checkFile(request.getPath());
		if (error.length()>0) {
			setNotFoundError(response,error);
		}
	}
	
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		Str data = new Str();
		Str error = data.fromFile(request.getPath());
		if (error.length()>0) {
			setNotFoundError(response,error);
		} else {
			response.body = data;
		}
	}
	
	protected void handlePostRequest(HttpRequest request, HttpResponse response) {
		String path = request.getPath();
		Str error = FileIO.checkDirectory(parseDirectoryFromFile(path));
		if (error.length()>0) {
			setError(response,HttpURLConnection.HTTP_PRECON_FAILED,error);
		} else {
			Str data = new Str(request.body);
			error = data.toFile(path);
			if (error.length()>0) {
				setError(response,HttpURLConnection.HTTP_PRECON_FAILED,error);
			}
		}
	}
	
	protected void handlePutRequest(HttpRequest request, HttpResponse response) {
		handlePostRequest(request,response);
	}
	
	protected void handleDeleteRequest(HttpRequest request, HttpResponse response) {
		Str error = FileIO.deleteFile(request.getPath());
		if (error.length()>0) {
			setNotFoundError(response,error);
		}
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
	
	protected void setNotFoundError(HttpResponse response, Str error) {
		setError(response,HttpURLConnection.HTTP_NOT_FOUND,error);
	}
	
	protected void setError(HttpResponse response, int code, Str error) {
		response.code = code;
		response.message = error.toString();
		response.body = error;
	}
	
	protected String parseDirectoryFromFile(String path) {
		String[] split = path.split("/");
		if (split.length>1) {
			path = path.substring(0,path.length() - split[split.length - 1].length());
		}
		return path;
	}
}
