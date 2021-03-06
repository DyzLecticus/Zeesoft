package nl.zeesoft.zdbd.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdbd.api.css.MainCss;
import nl.zeesoft.zdbd.api.html.Bye;
import nl.zeesoft.zdbd.api.html.IndexHtml;
import nl.zeesoft.zdbd.api.html.NetworkConfiguration;
import nl.zeesoft.zdbd.api.html.form.AddArpeggiator;
import nl.zeesoft.zdbd.api.html.form.AddGenerator;
import nl.zeesoft.zdbd.api.html.form.ArpeggiatorEditor;
import nl.zeesoft.zdbd.api.html.form.ArpeggiatorList;
import nl.zeesoft.zdbd.api.html.form.ChordEditor;
import nl.zeesoft.zdbd.api.html.form.EchoEditor;
import nl.zeesoft.zdbd.api.html.form.GeneratorEditor;
import nl.zeesoft.zdbd.api.html.form.GeneratorList;
import nl.zeesoft.zdbd.api.html.form.InstrumentEditor;
import nl.zeesoft.zdbd.api.html.form.InstrumentList;
import nl.zeesoft.zdbd.api.html.form.LfoEditor;
import nl.zeesoft.zdbd.api.html.form.NetworkStatistics;
import nl.zeesoft.zdbd.api.html.form.NewTheme;
import nl.zeesoft.zdbd.api.html.form.SaveThemeAs;
import nl.zeesoft.zdbd.api.html.form.SequenceEditor;
import nl.zeesoft.zdbd.api.html.select.DeleteTheme;
import nl.zeesoft.zdbd.api.html.select.LoadTheme;
import nl.zeesoft.zdbd.api.html.select.SelectSoundPatch;
import nl.zeesoft.zdbd.api.javascript.ArpeggiatorsJs;
import nl.zeesoft.zdbd.api.javascript.BindingsJs;
import nl.zeesoft.zdbd.api.javascript.ChordsJs;
import nl.zeesoft.zdbd.api.javascript.GeneratorsJs;
import nl.zeesoft.zdbd.api.javascript.IndexJs;
import nl.zeesoft.zdbd.api.javascript.MainJs;
import nl.zeesoft.zdbd.api.javascript.MenuJs;
import nl.zeesoft.zdbd.api.javascript.ModalJs;
import nl.zeesoft.zdbd.api.javascript.NetworkJs;
import nl.zeesoft.zdbd.api.javascript.SequencerJs;
import nl.zeesoft.zdbd.api.javascript.StateJs;
import nl.zeesoft.zdbd.api.javascript.ThemeJs;
import nl.zeesoft.zdbd.midi.Arpeggiator;
import nl.zeesoft.zdbd.midi.EchoConfig;
import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.midi.MixState;
import nl.zeesoft.zdbd.midi.SoundPatch;
import nl.zeesoft.zdbd.midi.SoundPatchFactory;
import nl.zeesoft.zdbd.midi.SynthChannelConfig;
import nl.zeesoft.zdbd.midi.SynthConfig;
import nl.zeesoft.zdbd.midi.convertors.InstrumentConvertor;
import nl.zeesoft.zdbd.midi.lfo.ChannelLFO;
import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdbd.pattern.SequenceChord;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdbd.theme.ThemeController;
import nl.zeesoft.zdbd.theme.ThemeSequenceController;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class RequestHandler extends HttpRequestHandler {
	protected ThemeController			controller		= null;
	protected ControllerMonitor			monitor			= null;
	protected ThemeSequenceController	sequencer		= null;
	
	protected SortedMap<String,Str>	pathResponses	= new TreeMap<String,Str>();
	
	protected RequestHandler(HttpServerConfig config, ThemeController controller, ControllerMonitor monitor, ThemeSequenceController sequencer) {
		super(config);
		this.controller = controller;
		this.monitor = monitor;
		this.sequencer = sequencer;
		
		pathResponses.put("/", (new IndexHtml()).render());
		pathResponses.put("/index.html", (new IndexHtml()).render());

		pathResponses.put("/main.css", (new MainCss()).render());
		
		pathResponses.put("/index.js", (new IndexJs()).render());

		pathResponses.put("/main.js", (new MainJs()).render());
		pathResponses.put("/modal.js", (new ModalJs()).render());
		pathResponses.put("/state.js", (new StateJs()).render());
		pathResponses.put("/bindings.js", (new BindingsJs()).render());
		pathResponses.put("/menu.js", (new MenuJs()).render());
		pathResponses.put("/theme.js", (new ThemeJs()).render());
		pathResponses.put("/sequence.js", (new SequencerJs()).render());
		pathResponses.put("/chords.js", (new ChordsJs()).render());
		pathResponses.put("/sequencer.js", (new SequencerJs()).render());
		pathResponses.put("/network.js", (new NetworkJs()).render());
		pathResponses.put("/generators.js", (new GeneratorsJs()).render());
		pathResponses.put("/arpeggiators.js", (new ArpeggiatorsJs()).render());
		pathResponses.put("/soundpatch.js", (new ArpeggiatorsJs()).render());
	}

	@Override
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		if (request.path.equals("/favicon.ico")) {
			super.handleGetRequest(request, response);
		} else {
			Str body = pathResponses.get(request.path);
			if (body!=null) {
				response.code = HttpURLConnection.HTTP_OK;
				response.body = body;
				if (request.path.endsWith(".css")) {
					response.headers.addContentTypeHeader("text/css");
				}
			} else {
				if (request.path.equals("/quit.txt")) {
					Str resp = new Str("themeHasChanges:");
					resp.sb().append(controller.themeHasChanges());
					response.code = HttpURLConnection.HTTP_OK;
					response.body = resp;
				} else if (request.path.equals("/state.txt")) {
					response.code = HttpURLConnection.HTTP_OK;
					response.body = monitor.getStateResponse();
				} else if (request.path.equals("/theme.txt")) {
					if (checkInitialized(response)) {
						handleGetThemeRequest(response);
					}
				} else if (request.path.equals("/sequencer.txt")) {
					if (checkInitialized(response)) {
						handleGetSequencerRequest(response);
					}
				} else if (request.path.equals("/sequencerControl.txt")) {
					if (checkInitialized(response)) {
						int bpm = 120;
						float shufflePercentage = 0F;
						Rythm rythm = controller.getRythm();
						if (rythm!=null) {
							bpm = (int)rythm.beatsPerMinute;
							shufflePercentage = rythm.stepDelays[1];
						}
						String name = controller.getName();
						String midiRecording = "";
						String audioRecording = "";
						if (controller.recordingExists(true)) {
							midiRecording = name + ".mid";
						}
						if (controller.recordingExists(false)) {
							audioRecording = name + ".wav";
						}
						response.code = HttpURLConnection.HTTP_OK;
						response.body = sequencer.getSequencerControl(bpm,shufflePercentage,midiRecording,audioRecording).render();
					} else {
						response.code = HttpURLConnection.HTTP_OK;
						response.body = sequencer.getSequencerControl(120,0.0F,"","").render();
					}
				} else if (request.path.equals("/chordEditor.txt")) {
					if (checkInitialized(response)) {
						ChordEditor editor = new ChordEditor(controller.getTrainingSequence());
						response.code = HttpURLConnection.HTTP_OK;
						response.body = editor.render();
					}
				} else if (request.path.equals("/sequenceEditor.txt")) {
					if (checkInitialized(response)) {
						SequenceEditor editor = new SequenceEditor(controller.getTrainingSequence(),0);
						response.code = HttpURLConnection.HTTP_OK;
						response.body = editor.render();
					}
				} else if (request.path.equals("/network.txt")) {
					if (checkInitialized(response)) {
						handleGetNetworkRequest(response);
					}
				} else if (request.path.equals("/networkStatistics.txt")) {
					float accuracy = 0;
					NetworkIO lastIO = controller.getLastIO();
					if (lastIO!=null) {
						accuracy = lastIO.getAverageClassifierAccuracy(false);
					}
					NetworkStatistics statistics = new NetworkStatistics(controller.getNetworkStatistics(),accuracy);
					response.code = HttpURLConnection.HTTP_OK;
					response.body = statistics.render();
				} else if (request.path.equals("/generatorStatus.txt")) {
					if (checkInitialized(response)) {
						handleGetGeneratorStatusRequest(response);
					}
				} else if (request.path.equals("/generators.txt")) {
					if (checkInitialized(response)) {
						GeneratorList generators = new GeneratorList(controller.listGenerators());
						response.code = HttpURLConnection.HTTP_OK;
						response.body = generators.render();
					}
				} else if (request.path.equals("/arpeggiators.txt")) {
					if (checkInitialized(response)) {
						ArpeggiatorList generators = new ArpeggiatorList(controller.listArpeggiators());
						response.code = HttpURLConnection.HTTP_OK;
						response.body = generators.render();
					}
				} else if (request.path.equals("/soundpatch.txt")) {
					if (checkInitialized(response)) {
						InstrumentList instruments = new InstrumentList();
						response.code = HttpURLConnection.HTTP_OK;
						response.body = instruments.render();
					}
				} else if (request.path.startsWith("/Recordings/")) {
					if (checkInitialized(response)) {
						String path = controller.getRecordingPath(request.path.endsWith(".mid"));
						if (path.length()>0) {
							Path pth = Paths.get(path);
							try {
								byte[] data = Files.readAllBytes(pth);
								response.code = HttpURLConnection.HTTP_OK;
								response.bytes = data;
							} catch (IOException e) {
								e.printStackTrace();
								setInternalError(response, new Str("IO exception while reading file"));
							}
						} else {
							setNotFoundError(response,new Str("Not found"));
						}
					}
				} else {
					setNotFoundError(response,new Str("Not found"));
				}
			}
		}
		setDefaultHeaders(response);
	}
	
	protected void handleGetThemeRequest(HttpResponse response) {
		Str r = new Str();
		r.sb().append("name:");
		r.sb().append(controller.getName());
		
		int bpm = 120;
		Rythm rythm = controller.getRythm();
		if (rythm!=null) {
			bpm = (int) rythm.beatsPerMinute;
		}
		
		r.sb().append("\n");
		r.sb().append("beatsPerMinute:");
		r.sb().append(bpm);

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
	
	protected void handleGetSequencerRequest(HttpResponse response) {
		Str r = new Str();
		
		boolean running = false;
		long recordedTicks = 0;
		if (MidiSys.sequencer!=null) {
			running = MidiSys.sequencer.isRunning();
			recordedTicks = MidiSys.sequencer.getRecordedTicks();
		}
		
		r.sb().append("isRunning:");
		r.sb().append(running);

		r.sb().append("\n");
		r.sb().append("currentSequence:");
		r.sb().append(sequencer.getCurrentSequence());
		
		r.sb().append("\n");
		r.sb().append("nextSequence:");
		r.sb().append(sequencer.getNextSequence());
		
		MixState mix = sequencer.getCurrentMix();
		List<Integer> channels = new ArrayList<Integer>();
		channels.add(SynthConfig.DRUM_CHANNEL);
		channels.add(SynthConfig.BASS_CHANNEL_1);
		channels.add(SynthConfig.BASS_CHANNEL_2);
		channels.add(SynthConfig.STAB_CHANNEL);
		channels.add(SynthConfig.ARP_CHANNEL_1);
		channels.add(SynthConfig.ARP_CHANNEL_2);
		for (Integer channel: channels) {
			r.sb().append("\n");
			r.sb().append("mute-channel-");
			r.sb().append(channel);
			r.sb().append(":");
			r.sb().append(mix.muteChannels[channel]);
		}
		int index = 0;
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			if (index<mix.muteDrums.length) {
				r.sb().append("\n");
				r.sb().append("mute-drum-");
				r.sb().append(inst.name());
				r.sb().append(":");
				r.sb().append(mix.muteDrums[index]);
			} else {
				break;
			}
			index++;
		}

		r.sb().append("\n");
		r.sb().append("isRecording:");
		r.sb().append(sequencer.isRecording());
		
		r.sb().append("\n");
		r.sb().append("recordedTicks:");
		r.sb().append(recordedTicks);

		response.code = HttpURLConnection.HTTP_OK;
		response.body = r;
	}
	
	protected void handleGetNetworkRequest(HttpResponse response) {
		Str r = new Str();
		r.sb().append("isTraining:");
		r.sb().append(monitor.getText().equals(ThemeController.TRAINING_NETWORK));
		
		r.sb().append("\n");
		r.sb().append("needsTraining:");
		r.sb().append(controller.changedTrainingSequenceSinceTraining());
		
		PatternSequence sequence = controller.getTrainingSequence();
		r.sb().append("\n");
		r.sb().append("canTrain:");
		r.sb().append(sequence.getSequencedPatterns().size()>0 && monitor.getDonePercentage()==1);
		
		response.code = HttpURLConnection.HTTP_OK;
		response.body = r;
	}
	
	protected void handleGetGeneratorStatusRequest(HttpResponse response) {
		Str r = new Str();
		r.sb().append("isGenerating:");
		r.sb().append(monitor.getText().equals(ThemeController.GENERATING_SEQUENCES));
		
		boolean canGenerate = !monitor.getText().equals(ThemeController.TRAINING_NETWORK) && controller.getLastIO()!=null;		
		r.sb().append("\n");
		r.sb().append("canGenerate:");
		r.sb().append(canGenerate);
		
		response.code = HttpURLConnection.HTTP_OK;
		response.body = r;
	}

	@Override
	protected void handlePostRequest(HttpRequest request, HttpResponse response) {
		if (request.path.equals("/state.txt")) {
			handlePostStateRequest(request,response);
		} else if (request.path.equals("/modal.txt")) {
			handlePostModalRequest(request,response);
		} else if (request.path.equals("/sequencer.txt")) {
			handlePostSequencerRequest(request,response);
		} else if (request.path.equals("/chordEditor.txt")) {
			handlePostChordEditorRequest(request,response);
		} else if (request.path.equals("/sequenceEditor.txt")) {
			handlePostSequenceEditorRequest(request,response);
		} else if (request.path.equals("/network.txt")) {
			handlePostNetworkRequest(request,response);
		} else if (request.path.equals("/generators.txt")) {
			handlePostGeneratorsRequest(request,response);
		} else if (request.path.equals("/generator.txt")) {
			handlePostGeneratorRequest(request,response);
		} else if (request.path.equals("/arpeggiators.txt")) {
			handlePostArpeggiatorsRequest(request,response);
		} else if (request.path.equals("/arpeggiator.txt")) {
			handlePostArpeggiatorRequest(request,response);
		} else if (request.path.equals("/soundpatch.txt")) {
			handlePostSoundPatchRequest(request,response);
		} else {
			setNotFoundError(response,new Str("Not found"));
		}
		setDefaultHeaders(response);
	}

	protected void handlePostStateRequest(HttpRequest request, HttpResponse response) {
		if (request.body.toString().equals("QUIT")) {
			setPostOk(response);
			config.getCloseServerRunner().start();
		} else if (request.body.startsWith("LOAD:")) {
			String name = request.body.split(":").get(1).toString();
			List<String> names = controller.listThemes();
			if (name.length()==0 || !names.contains(name)) {
				Str err = new Str("The specified theme does not exist");
				setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			} else {
				if (!controller.isBusy()) {
					monitor.startChain(controller.loadTheme(name));
					setPostOk(response);
				} else {
					setBusyError(response,"Unable to load the theme right now. Please try again later");
				}
			}
		} else if (request.body.toString().equals("SAVE")) {
			if (!controller.isBusy()) {
				monitor.startChain(controller.saveTheme());
				setPostOk(response);
			} else {
				setBusyError(response,"Unable to save the theme right now. Please try again later");
			}
		} else if (request.body.startsWith("SAVE_AS:")) {
			Str name = request.body.split(":").get(1);
			if (checkThemeName(name,response)) {
				if (!controller.isBusy()) {
					monitor.startChain(controller.saveThemeAs(name.toString()));
					setPostOk(response);
				} else {
					setBusyError(response,"Unable to save the theme right now. Please try again later");
				}
			}
		} else if (request.body.startsWith("DELETE:")) {
			String name = request.body.split(":").get(1).toString();
			List<String> names = controller.listThemes();
			if (name.length()==0 || !names.contains(name)) {
				Str err = new Str("The specified theme does not exist");
				setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			} else {
				if (!controller.isBusy()) {
					monitor.startChain(controller.deleteTheme(name));
					setPostOk(response);
				} else {
					setBusyError(response,"Unable to delete the theme right now. Please try again later");
				}
			}
		} else if (request.body.startsWith("NEW:")) {
			List<Str> elems = request.body.split(":");
			Str name = elems.get(1);
			if (checkThemeName(name,response)) {
				Rythm rythm = new Rythm();
				if (elems.size()>2) {
					rythm.beatsPerMinute = parseBeatsPerMinute(elems.get(2));
				}
				if (elems.size()>3) {
					rythm.setShuffle(parseShufflePercentage(elems.get(3)));
				}
				if (!controller.isBusy()) {
					monitor.startChain(controller.newTheme(name.toString(), rythm));
					setPostOk(response);
				} else {
					setBusyError(response,"Unable to initialize a new theme right now. Please try again later");
				}
			}
		} else {
			setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
		}
	}
	
	protected void handlePostModalRequest(HttpRequest request, HttpResponse response) {
		String name = request.body.toString();
		if (name.equals("Bye")) {
			response.code = HttpURLConnection.HTTP_OK;
			response.body = (new Bye()).render();
		} else if (name.equals("LoadTheme")) {
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
			response.body = (new NewTheme("",120,0.0F)).render();
		} else if (name.equals("SelectSoundPatch")) {
			List<String> names = SoundPatchFactory.listSoundPatches();
			response.code = HttpURLConnection.HTTP_OK;
			response.body = (new SelectSoundPatch(names)).render();
		} else if (name.equals("NetworkConfiguration")) {
			response.code = HttpURLConnection.HTTP_OK;
			response.body = (new NetworkConfiguration()).render();
		} else {
			setNotFoundError(response,new Str("Not found"));
		}
	}

	protected void handlePostSequencerRequest(HttpRequest request, HttpResponse response) {
		if (request.body.toString().equals("START_SEQUENCE")) {
			if (MidiSys.isInitialized() && !MidiSys.sequencer.isRunning()) {
				String name = NetworkTrainer.TRAINING_SEQUENCE;
				if (checkSequenceName(name,response)) {
					sequencer.startSequence(name);
					setPostOk(response);
				}
			} else {
				setPostOk(response);
			}
		} else if (request.body.toString().equals("START")) {
			String name = sequencer.getCurrentSequence();
			if (MidiSys.isInitialized()) {
				if(!MidiSys.sequencer.isRunning()) {
					if (checkSequenceName(name,response)) {
						sequencer.start();
						setPostOk(response);
					}
				} else {
					setPostOk(response);
				}
			} else {
			}
		} else if (request.body.toString().equals("STOP")) {
			if (MidiSys.isInitialized()) {
				if (MidiSys.sequencer.isRunning()) {
					MidiSys.sequencer.stop();
				}
				setPostOk(response);
			}
		} else if (request.body.toString().equals("START_RECORDING")) {
			if (MidiSys.isInitialized()) {
				sequencer.startRecording();
				setPostOk(response);
			}
		} else if (request.body.toString().equals("STOP_RECORDING")) {
			if (MidiSys.isInitialized()) {
				sequencer.stopRecording();
				setPostOk(response);
			}
		} else if (
			request.body.toString().equals("EXPORT_AUDIO") ||
			request.body.toString().equals("EXPORT_MIDI")
			) {
			if (MidiSys.isInitialized()) {
				if (MidiSys.sequencer.getRecordedTicks()>0) {
					boolean midi = request.body.toString().equals("EXPORT_MIDI");
					monitor.startChain(controller.exportRecording(midi));
					setPostOk(response);
				} else {
					Str err = new Str("No recorded data to export");
					setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
				}
			}
		} else if (request.body.startsWith("SET_PROPERTY:")) {
			List<Str> elems = request.body.split(":");
			String name = elems.get(1).toString();
			boolean error = false;
			if (name.equals("beatsPerMinute")) {
				controller.setBeatsPerMinute(parseBeatsPerMinute(elems.get(2)));;
			} else if (name.equals("shufflePercentage")) {
				controller.setShuffle(parseShufflePercentage(elems.get(2)));;
			} else if (name.equals("currentSequence")) {
				sequencer.setCurrentSequence(elems.get(2).toString());
			} else if (name.equals("volumeGlobal")) {
				sequencer.setVolumeGlobal(parsePercentage(elems.get(2)));
			} else if (name.equals("volumeDrums")) {
				sequencer.setVolumeDrums(parsePercentage(elems.get(2)));
			} else if (name.equals("volumeBass")) {
				sequencer.setVolumeBass(parsePercentage(elems.get(2)));
			} else if (name.equals("volumeStab")) {
				sequencer.setVolumeStab(parsePercentage(elems.get(2)));
			} else if (name.equals("volumeArpeggiator")) {
				sequencer.setVolumeArpeggiator(parsePercentage(elems.get(2)));
			} else if (name.equals("nextSequence")) {
				sequencer.setNextSequence(elems.get(2).toString());
			} else if (name.equals("nextArpeggiator")) {
				Arpeggiator arp = controller.getArpeggiator(elems.get(2).toString());
				if (arp!=null) {
					sequencer.setNextArpeggiator(arp);
				} else {
					Str err = new Str("Arpeggiator not found: ");
					err.sb().append(elems.get(2).toString());
					setNotFoundError(response,err);
					error = true;
				}
			} else if (name.equals("hold")) {
				sequencer.setHold(parseBoolean(elems.get(2)));
			} else if (name.equals("selectRandom")) {
				sequencer.setSelectRandom(parseBoolean(elems.get(2)));
			} else if (name.equals("selectTrainingSequence")) {
				sequencer.setSelectTrainingSequence(parseBoolean(elems.get(2)));
			} else if (name.equals("regenerateOnPlay")) {
				sequencer.setRegenerateOnPlay(parseBoolean(elems.get(2)));
			} else {
				setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
				error = true;
			}
			if (!error) {
				setPostOk(response);
			}
		} else if (request.body.startsWith("TOGGLE_MUTE:")) {
			List<Str> elems = request.body.split(":");
			List<Str> idElems = elems.get(1).split("-");
			boolean current = idElems.get(0).toString().equals("mute");
			MixState state = null;
			if (current) {
				state = sequencer.getCurrentMix();
			} else {
				state = sequencer.getNextMix();
			}
 			if (elems.get(1).contains("-channel-")) {
				int channel = Integer.parseInt(idElems.get(2).toString());
				if (channel<state.muteChannels.length) {
					state.muteChannels[channel] = parseBoolean(elems.get(2));
				}
			} else if (elems.get(1).contains("-drum-")) {
				int index = 0;
				for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
					if (inst.name().equals(idElems.get(2).toString())) {
						break;
					}
					index++;
				}
				if (index<state.muteDrums.length) {
					state.muteDrums[index] = parseBoolean(elems.get(2));
				}
			}
			if (current) {
				sequencer.setCurrentMix(state);
			} else {
				sequencer.setNextMix(state);
			}
			setPostOk(response);
		} else {
			setNotFoundError(response,new Str("Not found"));
		}
	}

	protected void handlePostChordEditorRequest(HttpRequest request, HttpResponse response) {
		if (request.body.startsWith("SET_CHORD_STEP:")) {
			PatternSequence sequence = controller.getTrainingSequence();
			List<Str> elems = request.body.split(":");
			int chordStep = Integer.parseInt(elems.get(1).toString());
			int newChordStep = Integer.parseInt(elems.get(2).toString());
			if (chordStep>0 && newChordStep>0) {
				if (chordStep!=newChordStep) {
					SequenceChord chord = sequence.getChordForStep(chordStep,true);
					if (chord!=null) {
						SequenceChord existing = sequence.getChordForStep(newChordStep,true);
						if (existing==null) {
							chord.step = newChordStep;
							controller.setTrainingSequence(sequence);
							setPostOk(response);
						} else {
							Str err = new Str("Chord already exists for step: ");
							err.sb().append(newChordStep);
							setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
						}
					} else {
						Str err = new Str("Chord not found: ");
						err.sb().append(chordStep);
						setNotFoundError(response,err);
					}
				}
			} else {
				Str err = new Str("Changing the first chord step is not allowed");
				setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			}
		} else if (request.body.startsWith("SET_CHORD_BASE_NOTE:")) {
			PatternSequence sequence = controller.getTrainingSequence();
			List<Str> elems = request.body.split(":");
			int chordStep = Integer.parseInt(elems.get(1).toString());
			int baseNote = Integer.parseInt(elems.get(2).toString());
			SequenceChord chord = sequence.getChordForStep(chordStep,true);
			if (chord!=null) {
				chord.baseNote = baseNote;
				controller.setTrainingSequence(sequence);
				setPostOk(response);
			} else {
				Str err = new Str("Chord not found: ");
				err.sb().append(chordStep);
				setNotFoundError(response,err);
			}
		} else if (request.body.startsWith("SET_CHORD_INTERVAL:")) {
			PatternSequence sequence = controller.getTrainingSequence();
			List<Str> elems = request.body.split(":");
			int chordStep = Integer.parseInt(elems.get(1).toString());
			int interval = Integer.parseInt(elems.get(2).toString());
			int value = Integer.parseInt(elems.get(3).toString());
			if (interval < 0) {
				interval = 0;
			}
			if (interval > 3) {
				interval = 3;
			}
			if (value < 0) {
				value = 0;
			}
			if (value > 12) {
				value = 12;
			}
			SequenceChord chord = sequence.getChordForStep(chordStep,true);
			if (chord!=null) {
				chord.interval[interval] = value;
				controller.setTrainingSequence(sequence);
				setPostOk(response);
			} else {
				Str err = new Str("Chord not found: ");
				err.sb().append(chordStep);
				setNotFoundError(response,err);
			}
		} else if (request.body.startsWith("DELETE:")) {
			PatternSequence sequence = controller.getTrainingSequence();
			List<Str> elems = request.body.split(":");
			int chordStep = Integer.parseInt(elems.get(1).toString());
			SequenceChord chord = sequence.getChordForStep(chordStep,true);
			if (chord!=null) {
				sequence.chordChanges.remove(chord);
				controller.setTrainingSequence(sequence);
				setPostOk(response);
			} else {
				Str err = new Str("Chord not found: ");
				err.sb().append(chordStep);
				setNotFoundError(response,err);
			}
		} else if (request.body.toString().equals("ADD")) {
			PatternSequence sequence = controller.getTrainingSequence();
			SequenceChord newChord = new SequenceChord();
			for (SequenceChord chord: sequence.chordChanges) {
				if (newChord==null || chord.step>=newChord.step) {
					newChord = chord.copy();
					newChord.step++;
				}
			}
			sequence.chordChanges.add(newChord);
			controller.setTrainingSequence(sequence);
			setPostOk(response);
		} else {
			setNotFoundError(response,new Str("Not found"));
		}
	}

	protected void handlePostSequenceEditorRequest(HttpRequest request, HttpResponse response) {
		if (request.body.startsWith("SET_SEQUENCE_PATTERN:")) {
			PatternSequence sequence = controller.getTrainingSequence();
			List<Str> elems = request.body.split(":");
			int sequenceIndex = Integer.parseInt(elems.get(1).toString());
			int sequencePattern = Integer.parseInt(elems.get(2).toString());
			sequence.sequence[sequenceIndex] = sequencePattern;
			controller.setTrainingSequence(sequence);
			setPostOk(response);
		} else if (request.body.startsWith("COPY:")) {
			PatternSequence sequence = controller.getTrainingSequence();
			List<Str> elems = request.body.split(":");
			int patternIndexC = Integer.parseInt(elems.get(1).toString());
			int patternIndexP = Integer.parseInt(elems.get(2).toString());
			if (patternIndexC<sequence.sequence.length && patternIndexP<sequence.sequence.length) {
				sequence.addEmptyPatterns();
				InstrumentPattern patternC = sequence.patterns.get(patternIndexC);
				sequence.patterns.set(patternIndexP, patternC.copy());
				controller.setTrainingSequence(sequence);
				setPostOk(response);
			} else {
				setNotFoundError(response,new Str("Not found"));
			}
		} else if (request.body.startsWith("CLEAR:")) {
			PatternSequence sequence = controller.getTrainingSequence();
			List<Str> elems = request.body.split(":");
			int patternIndex = Integer.parseInt(elems.get(1).toString());
			if (patternIndex<sequence.sequence.length) {
				sequence.addEmptyPatterns();
				InstrumentPattern pattern = sequence.patterns.get(patternIndex);
				pattern.clear();
				controller.setTrainingSequence(sequence);
				setPostOk(response);
			} else {
				setNotFoundError(response,new Str("Not found"));
			}
		} else if (request.body.startsWith("SET_PATTERN_STEP_VALUE:")) {
			PatternSequence sequence = controller.getTrainingSequence();
			List<Str> elems = request.body.split(":");
			int patternIndex = Integer.parseInt(elems.get(1).toString());
			String instrumentName = elems.get(2).toString();
			int step = Integer.parseInt(elems.get(3).toString());
			int value = Integer.parseInt(elems.get(4).toString());
			if (patternIndex<sequence.sequence.length) {
				sequence.addEmptyPatterns();
				InstrumentPattern pattern = sequence.patterns.get(patternIndex);
				pattern.setStepValue(instrumentName, step, value);
				controller.setTrainingSequence(sequence);
				setPostOk(response);
			} else {
				setNotFoundError(response,new Str("Not found"));
			}
		} else {
			setNotFoundError(response,new Str("Not found"));
		}
	}

	protected void handlePostNetworkRequest(HttpRequest request, HttpResponse response) {
		if (request.body.toString().equals("TRAIN")) {
			if (!controller.isBusy()) {
				monitor.startChain(controller.trainNetwork());
				setPostOk(response);
			} else {
				setBusyError(response,"Unable to train the network right now. Please try again later.");
			}
		} else if (request.body.toString().equals("RESET")) {
			if (!controller.isBusy()) {
				monitor.startChain(controller.resetNetwork());
				setPostOk(response);
			} else {
				setBusyError(response,"Unable to reset the network right now. Please try again later.");
			}
		} else {
			setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
		}
	}

	protected void handlePostGeneratorsRequest(HttpRequest request, HttpResponse response) {
		if (request.body.toString().equals("GENERATE_ALL")) {
			if (!controller.isBusy()) {
				monitor.startChain(controller.generateSequences());
				setPostOk(response);
			} else {
				setBusyError(response,"Unable to generate sequences right now. Please try again later.");
			}
		} else if (request.body.startsWith("GENERATE:")) {
			String name = request.body.split(":").get(1).toString();
			if (checkGeneratorName(name,response)) {
				if (!controller.isBusy()) {
					monitor.startChain(controller.generateSequence(name));
					setPostOk(response);
				} else {
					setBusyError(response,"Unable to generate sequences right now. Please try again later.");
				}
			}
		} else if (request.body.startsWith("MOVE_UP:")) {
			String name = request.body.split(":").get(1).toString();
			if (checkGeneratorName(name,response)) {
				controller.moveGenerator(name,true);
				setPostOk(response);
			}
		} else if (request.body.startsWith("MOVE_DOWN:")) {
			String name = request.body.split(":").get(1).toString();
			if (checkGeneratorName(name,response)) {
				controller.moveGenerator(name,false);
				setPostOk(response);
			}
		} else if (request.body.startsWith("DELETE:")) {
			String name = request.body.split(":").get(1).toString();
			if (checkGeneratorName(name,response)) {
				controller.removeGenerator(name);
				setPostOk(response);
			}
		} else {
			setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
		}
	}
	
	protected void handlePostGeneratorRequest(HttpRequest request, HttpResponse response) {
		if (request.body.toString().equals("ADD")) {
			response.code = HttpURLConnection.HTTP_OK;
			response.body = (new AddGenerator()).render();
		} else if (request.body.startsWith("SAVE")) {
			String name = request.body.split("\n").get(1).split(":").get(1).toString();
			name = name.trim();
			if (name.length()>0) {
				Generator gen = controller.getGenerator(name);
				if (gen==null) {
					gen = new Generator();
					gen.name = name;
					List<Str> lines = request.body.split("\n");
					for (Str line: lines) {
						List<Str> kv = line.split(":");
						String prop = kv.get(0).toString();
						if (prop.equals("group1Distortion")) {
							gen.group1Distortion = parsePercentage(kv.get(1));
						} else if (prop.equals("group2Distortion")) {
							gen.group2Distortion = parsePercentage(kv.get(1));
						} else if (prop.equals("group3Distortion")) {
							gen.group3Distortion = parsePercentage(kv.get(1));
						} else if (prop.equals("randomChunkOffset")) {
							gen.randomChunkOffset = parseBoolean(kv.get(1));
						} else if (prop.equals("mixStart")) {
							gen.mixStart = parsePercentage(kv.get(1));
						} else if (prop.equals("mixEnd")) {
							gen.mixEnd = parsePercentage(kv.get(1));
						} else if (prop.equals("maintainBeat")) {
							gen.maintainBeat = parsePercentage(kv.get(1));
						} else if (prop.equals("maintainFeedback")) {
							gen.maintainFeedback = parseBoolean(kv.get(1));
						} else if (prop.startsWith("maintain-")) {
							gen.setMaintainInstrument(prop.substring(5),parseBoolean(kv.get(1)));
						}
					}
					controller.putGenerator(gen);
					setPostOk(response);
				} else {
					Str err = new Str("Generator already exists with name: ");
					err.sb().append(name);
					setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
				}
			} else {
				Str err = new Str("Generator name is mandatory");
				setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			}
		} else if (request.body.startsWith("EDIT:")) {
			String name = request.body.split(":").get(1).toString();
			if (checkGeneratorName(name,response)) {
				Generator generator = controller.getGenerator(name);
				List<Generator> list = controller.listGenerators();
				List<String> names = new ArrayList<String>();
				for (Generator gen: list) {
					names.add(gen.name);
				}
				String prevName = getPreviousName(name, names);
				String nextName = getNextName(name, names);
				response.code = HttpURLConnection.HTTP_OK;
				response.body = (new GeneratorEditor(generator,prevName,nextName)).render();
			}
		} else if (request.body.startsWith("SET_PROPERTY:")) {
			List<Str> elems = request.body.split(":");
			String name = request.body.split(":").get(1).toString();
			if (checkGeneratorName(name,response)) {
				String newName = "";
				Generator generator = controller.getGenerator(name);
				String propertyName = elems.get(2).toString();
				boolean error = false;
				if (propertyName.equals("name")) {
					newName = elems.get(3).toString();
				} else if (propertyName.equals("group1Distortion")) {
					generator.group1Distortion = parsePercentage(elems.get(3));
				} else if (propertyName.equals("group2Distortion")) {
					generator.group2Distortion = parsePercentage(elems.get(3));
				} else if (propertyName.equals("group3Distortion")) {
					generator.group3Distortion = parsePercentage(elems.get(3));
				} else if (propertyName.equals("randomChunkOffset")) {
					generator.randomChunkOffset = parseBoolean(elems.get(3));
				} else if (propertyName.equals("mixStart")) {
					generator.mixStart = parsePercentage(elems.get(3));
				} else if (propertyName.equals("mixEnd")) {
					generator.mixEnd = parsePercentage(elems.get(3));
				} else if (propertyName.equals("maintainBeat")) {
					generator.maintainBeat = parsePercentage(elems.get(3));
				} else if (propertyName.equals("maintainFeedback")) {
					generator.maintainFeedback = parseBoolean(elems.get(3));
				} else if (propertyName.startsWith("maintain-")) {
					generator.setMaintainInstrument(propertyName.substring(5), parseBoolean(elems.get(3)));
				} else {
					setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
					error = true;
				}
				if (!error) {
					if (newName.length()>0) {
						newName = newName.replace(":",";");
						newName = newName.trim();
						Str err = controller.renameGenerator(name, newName);
						if (err.length()>0) {
							setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
						}
					} else {
						controller.putGenerator(generator);
					}
					setPostOk(response);
				}
			}
		} else {
			setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
		}
	}

	protected void handlePostArpeggiatorsRequest(HttpRequest request, HttpResponse response) {
		if (request.body.startsWith("DELETE:")) {
			String name = request.body.split(":").get(1).toString();
			if (checkArpeggiatorName(name,response)) {
				controller.removeArpeggiator(name);
				setPostOk(response);
			}
		} else {
			setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
		}
	}
	
	protected void handlePostArpeggiatorRequest(HttpRequest request, HttpResponse response) {
		if (request.body.toString().equals("ADD")) {
			response.code = HttpURLConnection.HTTP_OK;
			response.body = (new AddArpeggiator()).render();
		} else if (request.body.startsWith("SAVE")) {
			String name = request.body.split("\n").get(1).split(":").get(1).toString();
			name = name.trim();
			if (name.length()>0) {
				Arpeggiator arp = controller.getArpeggiator(name);
				if (arp==null) {
					arp = new Arpeggiator();
					arp.name = name;
					List<Str> lines = request.body.split("\n");
					for (Str line: lines) {
						List<Str> kv = line.split(":");
						String prop = kv.get(0).toString();
						if (prop.equals("minDuration")) {
							arp.minDuration = Integer.parseInt(kv.get(1).toString());
						} else if (prop.equals("maxDuration")) {
							arp.maxDuration = Integer.parseInt(kv.get(1).toString());
						} else if (prop.equals("density")) {
							arp.density = parsePercentage(kv.get(1));
						} else if (prop.equals("maxOctave")) {
							arp.maxOctave = Integer.parseInt(kv.get(1).toString());
						} else if (prop.equals("maxInterval")) {
							arp.maxInterval = Integer.parseInt(kv.get(1).toString());
						} else if (prop.equals("maxSteps")) {
							arp.maxSteps = Integer.parseInt(kv.get(1).toString());
						}
					}
					controller.putArpeggiator(arp);
					setPostOk(response);
				} else {
					Str err = new Str("Arpeggiator already exists with name: ");
					err.sb().append(name);
					setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
				}
			} else {
				Str err = new Str("Arpeggiator name is mandatory");
				setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			}
		} else if (request.body.startsWith("EDIT:")) {
			String name = request.body.split(":").get(1).toString();
			if (checkArpeggiatorName(name,response)) {
				Arpeggiator arpeggiator = controller.getArpeggiator(name);
				List<Arpeggiator> list = controller.listArpeggiators();
				List<String> names = new ArrayList<String>();
				for (Arpeggiator arp: list) {
					names.add(arp.name);
				}
				String prevName = getPreviousName(name, names);
				String nextName = getNextName(name, names);
				response.code = HttpURLConnection.HTTP_OK;
				response.body = (new ArpeggiatorEditor(arpeggiator,prevName,nextName)).render();
			}
		} else if (request.body.startsWith("SET_PROPERTY:")) {
			List<Str> elems = request.body.split(":");
			String name = request.body.split(":").get(1).toString();
			if (checkArpeggiatorName(name,response)) {
				String newName = "";
				Arpeggiator generator = controller.getArpeggiator(name);
				String propertyName = elems.get(2).toString();
				boolean error = false;
				if (propertyName.equals("name")) {
					newName = elems.get(3).toString();
				} else if (propertyName.equals("minDuration")) {
					generator.minDuration = Integer.parseInt(elems.get(3).toString());
				} else if (propertyName.equals("maxDuration")) {
					generator.maxDuration = Integer.parseInt(elems.get(3).toString());
				} else if (propertyName.equals("density")) {
					generator.density = parsePercentage(elems.get(3));
				} else if (propertyName.equals("maxOctave")) {
					generator.maxOctave = Integer.parseInt(elems.get(3).toString());
				} else if (propertyName.equals("maxInterval")) {
					generator.maxInterval = Integer.parseInt(elems.get(3).toString());
				} else if (propertyName.equals("maxSteps")) {
					generator.maxSteps = Integer.parseInt(elems.get(3).toString());
				} else {
					setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
					error = true;
				}
				if (!error) {
					if (newName.length()>0) {
						newName = newName.replace(":",";");
						newName = newName.trim();
						Str err = controller.renameArpeggiator(name, newName);
						if (err.length()>0) {
							setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
						}
					} else {
						controller.putArpeggiator(generator);
					}
					setPostOk(response);
				}
			}
		} else {
			setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
		}
	}

	protected void handlePostSoundPatchRequest(HttpRequest request, HttpResponse response) {
		if (request.body.startsWith("LOAD:")) {
			String name = request.body.split(":").get(1).toString();
			SoundPatch patch = SoundPatchFactory.getNewSoundPatch(name);
			if (patch!=null) {
				controller.setSoundPatch(patch);
				setPostOk(response);
			} else {
				Str err = new Str("Sound patch not found with name: ");
				err.sb().append(name);
				setNotFoundError(response,err);
			}
		} else if (request.body.startsWith("EDIT:")) {
			String name = request.body.split(":").get(1).toString();
			if (checkInstrumentName(name,response)) {
				List<String> names = SoundPatch.getInstrumentNames();
				String prevName = getPreviousName(name, names);
				String nextName = getNextName(name, names);
				if (name.equals(SoundPatch.LFOS)) {
					List<ChannelLFO> lfos = controller.getLFOs();
					response.code = HttpURLConnection.HTTP_OK;
					response.body = (new LfoEditor(name,lfos,prevName,nextName)).render();
				} else if (name.equals(SoundPatch.ECHOS)) {
					List<EchoConfig> echos = controller.getEchos(false);
					response.code = HttpURLConnection.HTTP_OK;
					response.body = (new EchoEditor(name,echos,prevName,nextName)).render();
				} else {
					SynthChannelConfig layer1 = controller.getChannelConfig(name,0);
					SynthChannelConfig layer2 = controller.getChannelConfig(name,1);
					List<InstrumentConvertor> convertors = controller.getConvertors(name);
					response.code = HttpURLConnection.HTTP_OK;
					response.body = (new InstrumentEditor(name,layer1,layer2,convertors,prevName,nextName)).render();
				}
			}
		} else if (request.body.startsWith("SET_PROPERTY:")) {
			List<Str> elems = request.body.split(":");
			String name = request.body.split(":").get(1).toString();
			if (checkInstrumentName(name,response)) {
				List<Str> property = elems.get(2).split("-");
				String propertyName = property.get(0).toString();
				if (propertyName.equals("conv")) {
					int layer = 0;
					propertyName = property.get(1).toString();
					Object value = parseValue(elems.get(3),propertyName);
					if (value!=null) {
						if (property.size()==3) {
							layer = Integer.parseInt(property.get(2).toString());
						} else if (property.size()==4) {
							layer = Integer.parseInt(property.get(3).toString());
							name = property.get(2).toString();
						}
						controller.setConvertorLayerProperty(name, layer, propertyName, value);
						setPostOk(response);
					} else {
						setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
					}
				} else if (propertyName.equals("lfo")) {
					int index = Integer.parseInt(property.get(2).toString());
					propertyName = property.get(1).toString();
					Object value = null;
					if (propertyName.equals("active")) {
						value = Boolean.parseBoolean(elems.get(3).toString());
					} else if (propertyName.equals("channel")) {
						value = LfoEditor.getChannelForName(elems.get(3).toString());
					} else if (propertyName.equals("control")) {
						value = LfoEditor.getControlForName(elems.get(3).toString());
					} else if (propertyName.equals("type")) {
						value = elems.get(3).toString();
					} else if (propertyName.equals("cycleSteps")) {
						int val = Integer.parseInt(elems.get(3).toString());
						if (val<1) {
							val = 1;
						} else if (val>999) {
							val = 999;
						}
						value = val;
					} else if (propertyName.equals("change")) {
						float val = Float.parseFloat(elems.get(3).toString());
						if (val<-1.0F) {
							val = -1.0F;
						} else if (val>1.0F) {
							val = 1.0F;
						}
						value = val;
					}
					if (value!=null) {
						controller.setLFOProperty(index, propertyName, value);
						setPostOk(response);
					} else {
						setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
					}
				} else if (propertyName.equals("echo")) {
					int index = Integer.parseInt(property.get(2).toString());
					propertyName = property.get(1).toString();
					Object value = null;
					if (propertyName.equals("active")) {
						value = Boolean.parseBoolean(elems.get(3).toString());
					} else if (propertyName.equals("sourceChannel")) {
						value = LfoEditor.getChannelForName(elems.get(3).toString());
					} else if (propertyName.equals("delay")) {
						int val = Integer.parseInt(elems.get(3).toString());
						if (val<1) {
							val = 1;
						} else if (val>128) {
							val = 128;
						}
						value = val;
					} else if (propertyName.equals("pan")) {
						value = parseBit(elems.get(3));
					} else if (
						propertyName.equals("velocity") ||
						propertyName.equals("filter") ||
						propertyName.equals("reverb") ||
						propertyName.equals("chorus")
						) {
						float val = Float.parseFloat(elems.get(3).toString());
						if (val<0.0F) {
							val = 0.0F;
						} else if (val>10.0F) {
							val = 10.0F;
						}
						value = val;
					}
					if (value!=null) {
						controller.setEchoProperty(index, propertyName, value);
						setPostOk(response);
					} else {
						setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
					}
				} else {
					int layer = Integer.parseInt(property.get(1).toString());
					int value = parseBit(elems.get(3));
					List<int[]> changes = controller.setInstrumentProperty(name, layer, propertyName, value);
					if (changes.size()>0) {
						setPostOk(response);
					} else {
						setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
					}
				}
			}
		} else {
			setError(response,HttpURLConnection.HTTP_UNSUPPORTED_TYPE,new Str("Not supported"));
		}
	}
	
	protected boolean checkInitialized(HttpResponse response) {
		boolean r = true;
		if (controller.getName().length()==0 || 
			controller.getTrainingSequence()==null ||
			monitor.getText().equals(ThemeController.INITIALIZING)
			) {
			setBusyError(response,"Initializing application. Please try again later.");
			r = false;
		}
		return r;
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
	
	protected boolean checkGeneratorName(String name, HttpResponse response) {
		boolean r = true;
		if (controller.getGenerator(name)==null) {
			Str err = new Str("Specified generator not found");
			setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			r = false;
		}
		return r;
	}
	
	protected boolean checkArpeggiatorName(String name, HttpResponse response) {
		boolean r = true;
		if (controller.getArpeggiator(name)==null) {
			Str err = new Str("Specified arpeggiator not found");
			setError(response,HttpURLConnection.HTTP_BAD_REQUEST,err);
			r = false;
		}
		return r;
	}
	
	protected boolean checkInstrumentName(String name, HttpResponse response) {
		boolean r = true;
		if (!SoundPatch.getInstrumentNames().contains(name)) {
			Str err = new Str("Specified instrument not found");
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
		if (r < 10) {
			r = 10;
		}
		if (r > 240) {
			r = 240;
		}
		return r;
	}
	
	protected boolean parseBoolean(Str value) {
		return Boolean.parseBoolean(value.toLowerCase().toString());
	}
	
	protected float parsePercentage(Str perc) {
		float r = 0;
		try {
			r = Float.parseFloat(perc.toString());
		} catch(NumberFormatException ex) {
			r = 0;
		}
		if (r < 0) {
			r = 0;
		}
		if (r > 1) {
			r = 1;
		}
		return r;
	}
	
	protected float parseShufflePercentage(Str perc) {
		float r = parsePercentage(perc);
		if (r > Rythm.MAX_SHUFFLE) {
			r = Rythm.MAX_SHUFFLE;
		}
		return r;
	}
	
	protected Object parseValue(Str val, String propertyName) {
		Object r = null;
		float f = 0;
		int i = 0;
		try {
			f = Float.parseFloat(val.toString());
		} catch(NumberFormatException ex) {
			// Ignore
		}
		try {
			i = Integer.parseInt(val.toString());
		} catch(NumberFormatException ex) {
			// Ignore
		}
		if (propertyName.equals("baseOctave")) {
			if (i < 0) {
				i = 0;
			}
			if (i > 9) {
				i = 9;
			}
			r = i;
		} else if (propertyName.equals("midiNote") ||
			propertyName.equals("velocity") ||
			propertyName.equals("accentVelocity")
			) {
			if (i < 0) {
				i = 0;
			}
			if (i > 127) {
				i = 127;
			}
			r = i;
		} else if (propertyName.equals("hold") ||
			propertyName.equals("accentHold")
			) {
			if (f < 0) {
				f = 0.0F;
			}
			if (f > 8.0F) {
				f = 8.0F;
			}
			r = f;
		}
		return r;
	}
	
	protected int parseBit(Str bit) {
		int r = SynthChannelConfig.DEFAULT;
		try {
			r = Integer.parseInt(bit.toString());
		} catch(NumberFormatException ex) {
			r = SynthChannelConfig.DEFAULT;
		}
		if (r < 0) {
			r = 0;
		}
		if (r > 127) {
			r = 127;
		}
		return r;
	}
	
	protected void setPostOk(HttpResponse response) {
		response.code = HttpURLConnection.HTTP_OK;
		response.body = new Str("OK");
	}
	
	protected void setBusyError(HttpResponse response, String msg) {
		setError(response,HttpURLConnection.HTTP_UNAVAILABLE,new Str(msg));
	}
	
	protected static String getPreviousName(String name, List<String> names) {
		int pIdx = names.indexOf(name) - 1;
		if (pIdx<0) {
			pIdx = names.size() - 1; 
		}
		return names.get(pIdx);
	}
	
	protected static String getNextName(String name, List<String> names) {
		int nIdx = names.indexOf(name) + 1;
		if (nIdx>=names.size()) {
			nIdx = 0; 
		}
		return names.get(nIdx);
	}
}
