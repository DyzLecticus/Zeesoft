package nl.zeesoft.zso.composition.sequencer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.client.ConductorConnector;
import nl.zeesoft.zjmo.orchestra.protocol.PublishRequest;
import nl.zeesoft.zso.composition.Composition;
import nl.zeesoft.zso.orchestra.SampleOrchestra;
import nl.zeesoft.zso.orchestra.controller.SampleOrchestraController;

public class Sequencer extends Locker implements ActionListener {
	public static final String			LOAD_COMPOSITION	= "LOAD_COMPOSITION";
	public static final String			GAIN_INCREASE		= "GAIN_INCREASE";
	public static final String			GAIN_DECREASE		= "GAIN_DECREASE";
	public static final String			VOLUME_INCREASE		= "VOLUME_INCREASE";
	public static final String			VOLUME_DECREASE		= "VOLUME_DECREASE";
	public static final String			START				= "START";
	public static final String			STOP				= "STOP";
	
	private Orchestra					orchestra			= null;
	private SampleOrchestraController	controller			= null;

	private ConductorConnector			connector			= null;

	private	CompositionPlayer			compositionPlayer	= null;

	private Composition					composition			= null;
	
	public Sequencer(Messenger msgr,WorkerUnion uni,Orchestra orch,SampleOrchestraController controller) {
		super(msgr);
		this.orchestra = orch;
		this.controller = controller;
		connector = new ConductorConnector(msgr,uni,false);
		connector.initialize(orch,null);
		compositionPlayer = new CompositionPlayer(msgr,uni,this);
	}

	public void open() {
		connector.open();
	}

	public void close() {
		connector.close();
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String err = "";
		if (evt.getActionCommand().equals(LOAD_COMPOSITION)) {
			if (controller.getFileChooser()==null) {
				controller.showErrorMessage("Composition file selector is busy initializing");
			} else {
				File file = controller.chooseFile(true,"Load");
				if (file!=null) {
					JsFile json = new JsFile();
					err = json.fromFile(file.getAbsolutePath());
					if (err.length()==0) {
						Composition comp = new Composition();
						comp.fromJson(json);
						if (comp.getSteps().size()>0) {
							controller.setComposition(comp);
						} else {
							err = "Composition does not contain any steps";
						}
					}
				}
			}
		} else if (evt.getActionCommand().equals(GAIN_INCREASE)) {
			err = setGain(3.0F);
		} else if (evt.getActionCommand().equals(GAIN_DECREASE)) {
			err = setGain(-3.0F);
		} else if (evt.getActionCommand().equals(VOLUME_INCREASE)) {
			err = setVolume(1.0F);
		} else if (evt.getActionCommand().equals(VOLUME_DECREASE)) {
			err = setVolume(0.5F);
		} else if (evt.getActionCommand().equals(START)) {
			start();
		} else if (evt.getActionCommand().equals(STOP)) {
			stop();
		}
		if (err.length()>0) {
			controller.showErrorMessage(err);
		}
	}
	
	public Orchestra getOrchestra() {
		return orchestra;
	}
	
	public Composition getComposition() {
		Composition r = null;
		lockMe(this);
		r = composition;
		unlockMe(this);
		return r;
	}

	public void setComposition(Composition composition) {
		lockMe(this);
		this.composition = composition;
		unlockMe(this);
	}

	protected void start() {
		stop();
		compositionPlayer.start();
	}

	protected void stop() {
		if (compositionPlayer.isWorking()) {
			compositionPlayer.stop();
		}
	}
	
	protected String setGain(float gain) {
		return setControlValue("gain",gain);
	}
		
	protected String setVolume(float volume) {
		return setControlValue("volume",volume);
	}
		
	protected String setControlValue(String type,float value) {
		String err = "";
		JsFile req = new JsFile();
		req.rootElement = new JsElem();
		req.rootElement.children.add(new JsElem(type,"" + value));
		PublishRequest pr = new PublishRequest();
		pr.setChannelName(SampleOrchestra.CONTROL);
		pr.setRequest(req);
		PublishRequest rpr = connector.publishRequest(pr);
		if (rpr!=null) {
			if (rpr.getError().length()>0) {
				err = "Failed to set gain: " + rpr.getError();
			}
		} else {
			err = SampleOrchestra.CONTROL + " channel is not available";
		}
		return err;
	}
}
