package nl.zeesoft.zdbd.api;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdbd.api.css.MainCss;
import nl.zeesoft.zdbd.api.html.ByeHtml;
import nl.zeesoft.zdbd.api.html.IndexHtml;
import nl.zeesoft.zdbd.api.html.form.NewTheme;
import nl.zeesoft.zdbd.api.html.form.SaveThemeAs;
import nl.zeesoft.zdbd.api.html.form.SequencerControl;
import nl.zeesoft.zdbd.api.html.select.DeleteTheme;
import nl.zeesoft.zdbd.api.html.select.LoadTheme;
import nl.zeesoft.zdbd.api.javascript.MainJs;
import nl.zeesoft.zdbd.api.javascript.MenuJs;
import nl.zeesoft.zdbd.api.javascript.ModalJs;
import nl.zeesoft.zdbd.api.javascript.QuitJs;
import nl.zeesoft.zdbd.api.javascript.SequencerJs;
import nl.zeesoft.zdbd.api.javascript.StateJs;
import nl.zeesoft.zdbd.api.javascript.ThemeJs;
import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdbd.theme.ThemeController;
import nl.zeesoft.zdbd.theme.ThemeSequenceSelector;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class RequestHandler extends HttpRequestHandler {
	protected ThemeController			controller		= null;
	protected ControllerMonitor			monitor			= null;
	protected ThemeSequenceSelector		selector		= null;
	
	protected SortedMap<String,Str>	pathResponses	= new TreeMap<String,Str>();
	
	protected RequestHandler(HttpServerConfig config, ThemeController controller, ControllerMonitor monitor, ThemeSequenceSelector selector) {
		super(config);
		this.controller = controller;
		this.monitor = monitor;
		this.selector = selector;
		
		pathResponses.put("/", (new IndexHtml()).render());
		pathResponses.put("/index.html", (new IndexHtml()).render());
		pathResponses.put("/bye.html", (new ByeHtml()).render());
		
		pathResponses.put("/main.js", (new MainJs()).render());
		pathResponses.put("/modal.js", (new ModalJs()).render());
		pathResponses.put("/state.js", (new StateJs()).render());
		pathResponses.put("/menu.js", (new MenuJs()).render());
		pathResponses.put("/quit.js", (new QuitJs()).render());
		pathResponses.put("/theme.js", (new ThemeJs()).render());
		pathResponses.put("/sequencer.js", (new SequencerJs()).render());
		
		pathResponses.put("/main.css", (new MainCss()).render());
	}

	@Override
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		Str body = pathResponses.get(request.path);
		if (body!=null) {
			response.code = HttpURLConnection.HTTP_OK;
			response.body = body;
			if (request.path.endsWith(".css")) {
				response.headers.addContentTypeHeader("text/css");
			}
		} else {
			if (request.path.equals("/state.txt")) {
				response.code = HttpURLConnection.HTTP_OK;
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
			} else if (request.path.equals("/sequencer.txt")) {
				int bpm = 120;
				Rythm rythm = controller.getRythm();
				if (rythm!=null) {
					bpm = (int)rythm.beatsPerMinute;
				}
				SequencerControl control = new SequencerControl(
					bpm,
					controller.getSequenceNames(),
					selector.getCurrentSequence(),
					selector.getNextSequence(),
					selector.isHold(),
					selector.isSelectRandom(),
					selector.isSelectSelectTrainingSequence(),
					selector.isRegenerateOnPlay()
				);
				response.code = HttpURLConnection.HTTP_OK;
				response.body = control.render();
			} else {
				setNotFoundError(response,new Str("Not found"));
			}
		}
	}

	@Override
	protected void handlePostRequest(HttpRequest request, HttpResponse response) {
		if (request.path.equals("/state.txt")) {
			if (request.body.toString().equals("QUIT")) {
				response.code = HttpURLConnection.HTTP_OK;
				response.body = new Str("OK");
				System.exit(0);
			} else if (request.body.startsWith("LOAD:")) {
				String name = request.body.split(":").get(1).toString();
				List<String> names = controller.listThemes();
				if (name.length()==0 || !names.contains(name)) {
					Str err = new Str("The specified theme does not exist");
					setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
				} else {
					monitor.startChain(controller.loadTheme(name));
					response.code = HttpURLConnection.HTTP_OK;
					response.body = new Str("OK");
				}
			} else if (request.body.toString().equals("SAVE")) {
				monitor.startChain(controller.saveTheme());
				response.code = HttpURLConnection.HTTP_OK;
				response.body = new Str("OK");
			} else if (request.body.startsWith("SAVE_AS:")) {
				Str name = request.body.split(":").get(1);
				if (checkThemeName(name,response)) {
					monitor.startChain(controller.saveThemeAs(name.toString()));
					response.code = HttpURLConnection.HTTP_OK;
					response.body = new Str("OK");
				}
			} else if (request.body.startsWith("DELETE:")) {
				String name = request.body.split(":").get(1).toString();
				List<String> names = controller.listThemes();
				if (name.length()==0 || !names.contains(name)) {
					Str err = new Str("The specified theme does not exist");
					setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
				} else {
					monitor.startChain(controller.deleteTheme(name));
					response.code = HttpURLConnection.HTTP_OK;
					response.body = new Str("OK");
				}
			} else if (request.body.startsWith("NEW:")) {
				List<Str> elems = request.body.split(":");
				Str name = elems.get(1);
				if (checkThemeName(name,response)) {
					Rythm rythm = new Rythm();
					rythm.beatsPerMinute = parseBeatsPerMinute(elems.get(2));
					monitor.startChain(controller.newTheme(name.toString(), rythm));
					response.code = HttpURLConnection.HTTP_OK;
					response.body = new Str("OK");
				}
			} else {
				setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
			}
		} else if (request.path.equals("/modal.txt")) {
			String name = request.body.toString();
			if (name.equals("LoadTheme")) {
				List<String> names = controller.listThemes();
				response.code = HttpURLConnection.HTTP_OK;
				response.body = (new LoadTheme(names)).render();
			} else if (name.equals("SaveThemeAs")) {
				String value = controller.getName();
				response.code = HttpURLConnection.HTTP_OK;
				response.body = (new SaveThemeAs(value)).render();
			} else if (name.equals("DeleteTheme")) {
				List<String> names = controller.listThemes();
				response.code = HttpURLConnection.HTTP_OK;
				response.body = (new DeleteTheme(names)).render();
			} else if (name.equals("NewTheme")) {
				response.code = HttpURLConnection.HTTP_OK;
				response.body = (new NewTheme("",120)).render();
			} else {
				setNotFoundError(response,new Str("Not found"));
			}
		} else if (request.path.equals("/sequencer.txt")) {
			if (request.body.toString().equals("PLAY_SEQUENCE")) {
				String name = selector.getCurrentSequence();
				if (MidiSys.isInitialized() && !MidiSys.sequencer.isRunning() &&
					checkSequenceName(name,response)
					) {
					selector.startSequence(name);
				}
			} else if (request.body.toString().equals("PLAY_THEME")) {
				String name = selector.getCurrentSequence();
				if (MidiSys.isInitialized() && !MidiSys.sequencer.isRunning() &&
					checkSequenceName(name,response)
					) {
					selector.startTheme(name);
				}
			} else if (request.body.toString().equals("STOP")) {
				if (MidiSys.isInitialized() && MidiSys.sequencer.isRunning()) {
					MidiSys.sequencer.stop();
				}
			} else if (request.body.startsWith("SET_PROPERTY:")) {
				List<Str> elems = request.body.split(":");
				String name = elems.get(1).toString();
				if (name.equals("beatsPerMinute")) {
					controller.setBeatsPerMinute(parseBeatsPerMinute(elems.get(2)));;
				} else if (name.equals("currentSequence")) {
					selector.setCurrentSequence(elems.get(2).toString());
				} else if (name.equals("nextSequence")) {
					selector.setNextSequence(elems.get(2).toString());
				} else if (name.equals("hold")) {
					selector.setHold(parseBoolean(elems.get(2)));
				} else if (name.equals("selectRandom")) {
					selector.setSelectRandom(parseBoolean(elems.get(2)));
				} else if (name.equals("selectTrainingSequence")) {
					selector.setSelectTrainingSequence(parseBoolean(elems.get(2)));
				} else if (name.equals("regenerateOnPlay")) {
					selector.setRegenerateOnPlay(parseBoolean(elems.get(2)));
				} else {
					setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
				}
				response.code = HttpURLConnection.HTTP_OK;
				response.body = new Str("OK");
			}
		} else {
			setNotFoundError(response,new Str("Not found"));
		}
	}
	
	protected void handleGetThemeRequest(HttpRequest request, HttpResponse response) {
		Str r = new Str();
		r.sb().append("name:");
		r.sb().append(controller.getName());
		
		r.sb().append("\n");
		r.sb().append("beatsPerMinute:");
		r.sb().append(controller.getRythm().beatsPerMinute);

		PatternSequence sequence = controller.getTrainingSequence();

		r.sb().append("\n");
		r.sb().append("beatsPerPattern:");
		r.sb().append(sequence.rythm.beatsPerPattern);

		r.sb().append("\n");
		r.sb().append("stepsPerBeat:");
		r.sb().append(sequence.rythm.stepsPerBeat);
		
		response.code = HttpURLConnection.HTTP_OK;
		response.body = r;
	}
	
	protected boolean checkThemeName(Str name, HttpResponse response) {
		boolean r = true;
		name.replace(" ","_");
		if (name.length()>32) {
			name = name.substring(0,32);
		}
		if (name.length()==0 || !name.isAlphaNumeric(false,true)) {
			Str err = new Str("Theme name must be alphanumeric. Underscores are allowed.");
			setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			r = false;
		}
		return r;
	}
	
	protected boolean checkSequenceName(String name, HttpResponse response) {
		boolean r = true;
		if (name.length()==0) {
			Str err = new Str("Select a sequence to play");
			setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			r = false;
		} else if (!controller.getSequenceNames().contains(name)) {
			Str err = new Str("Selected sequence not found");
			setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			r = false;
		}
		return r;
	}
	
	protected float parseBeatsPerMinute(Str bpm) {
		float r = 120;
		try {
			r = Integer.parseInt(bpm.toString());
		} catch(NumberFormatException ex) {
			r = 120;
		}
		if (r < 6) {
			r = 6;
		}
		if (r > 240) {
			r = 240;
		}
		return r;
	}
	
	protected boolean parseBoolean(Str value) {
		return Boolean.parseBoolean(value.toLowerCase().toString());
	}
}
