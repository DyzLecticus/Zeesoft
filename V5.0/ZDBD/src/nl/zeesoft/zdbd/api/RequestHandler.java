package nl.zeesoft.zdbd.api;

import java.net.HttpURLConnection;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdbd.api.css.MainCss;
import nl.zeesoft.zdbd.api.html.ByeHtml;
import nl.zeesoft.zdbd.api.html.IndexHtml;
import nl.zeesoft.zdbd.api.javascript.MainJs;
import nl.zeesoft.zdbd.api.javascript.MenuJs;
import nl.zeesoft.zdbd.api.javascript.QuitJs;
import nl.zeesoft.zdbd.api.javascript.StateJs;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.theme.ThemeController;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.thread.CodeRunnerChain;

public class RequestHandler extends HttpRequestHandler {
	protected ThemeController		controller		= null;
	protected ControllerMonitor		monitor			= null;
	
	protected SortedMap<String,Str>	pathResponses	= new TreeMap<String,Str>();
	
	protected RequestHandler(HttpServerConfig config, ThemeController controller, ControllerMonitor monitor) {
		super(config);
		this.controller = controller;
		this.monitor = monitor;
		
		pathResponses.put("/", (new IndexHtml()).render());
		pathResponses.put("/index.html", (new IndexHtml()).render());
		pathResponses.put("/bye.html", (new ByeHtml()).render());
		
		pathResponses.put("/main.js", (new MainJs()).render());
		pathResponses.put("/state.js", (new StateJs()).render());
		pathResponses.put("/menu.js", (new MenuJs()).render());
		pathResponses.put("/quit.js", (new QuitJs()).render());
		
		pathResponses.put("/main.css", (new MainCss()).render());
	}

	@Override
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		Str body = pathResponses.get(request.path);
		if (body!=null) {
			response.code = 200;
			response.body = body;
			if (request.path.endsWith(".css")) {
				response.headers.addContentTypeHeader("text/css");
			}
		} else {
			if (request.path.equals("/state.txt")) {
				response.code = 200;
				response.body = monitor.getStateResponse();
			} else if (request.path.equals("/theme.txt")) {
				if (controller.getName().length()==0 || 
					controller.getTrainingSequence()==null ||
					monitor.getText().equals(ThemeController.INITIALIZING)
					) {
					setError(response,HttpURLConnection.HTTP_UNAVAILABLE,
						new Str("Initializing application. Please try again later.")
					);
				} else {
					handleGetThemeRequest(request,response);
				}
			} else {
				setError(response,HttpURLConnection.HTTP_NOT_FOUND,new Str("Not found"));
			}
		}
	}

	@Override
	protected void handlePostRequest(HttpRequest request, HttpResponse response) {
		if (request.path.equals("/state.txt")) {
			if (request.body.toString().equals("QUIT")) {
				response.code = 200;
				response.body = new Str("OK");
				System.exit(0);
			} else if (request.body.toString().equals("SAVE")) {
				CodeRunnerChain chain = controller.saveTheme();
				monitor.startChain(chain);
				response.code = 200;
				response.body = new Str("OK");
			} else {
				setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
			}
		} else {
			setError(response,HttpURLConnection.HTTP_NOT_FOUND,new Str("Not found"));
		}
	}
	
	protected void handleGetThemeRequest(HttpRequest request, HttpResponse response) {
		Str r = new Str();
		r.sb().append("name:");
		r.sb().append(controller.getName());
		
		PatternSequence sequence = controller.getTrainingSequence();

		r.sb().append("\n");
		r.sb().append("beatsPerMinute:");
		r.sb().append(sequence.rythm.beatsPerMinute);

		r.sb().append("\n");
		r.sb().append("beatsPerPattern:");
		r.sb().append(sequence.rythm.beatsPerPattern);

		r.sb().append("\n");
		r.sb().append("stepsPerBeat:");
		r.sb().append(sequence.rythm.stepsPerBeat);
		
		response.code = 200;
		response.body = r;
	}
}
