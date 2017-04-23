package nl.zeesoft.zso.composition.sequencer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zso.composition.Composition;
import nl.zeesoft.zso.orchestra.controller.SampleOrchestraController;

public class Sequencer extends Locker implements ActionListener {
	public static final String			LOAD_COMPOSITION	= "LOAD_COMPOSITION";
	public static final String			START				= "START";
	public static final String			STOP				= "STOP";
	
	private Orchestra					orchestra			= null;
	private SampleOrchestraController	controller			= null;

	private	CompositionPlayer			compositionPlayer	= null;

	private Composition					composition			= null;
	
	public Sequencer(Messenger msgr,WorkerUnion uni,Orchestra orch,SampleOrchestraController controller) {
		super(msgr);
		this.orchestra = orch;
		this.controller = controller;
		compositionPlayer = new CompositionPlayer(getMessenger(),uni,this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(LOAD_COMPOSITION)) {
			if (controller.getFileChooser()==null) {
				controller.showErrorMessage("Composition file selector is busy initializing");
			} else {
				File file = controller.chooseFile(true,"Load");
				if (file!=null) {
					JsFile json = new JsFile();
					String err = json.fromFile(file.getAbsolutePath());
					if (err.length()==0) {
						Composition comp = new Composition();
						comp.fromJson(json);
						if (comp.getSteps().size()>0) {
							controller.setComposition(comp);
						} else {
							err = "Composition does not contain any steps";
						}
					}
					if (err.length()>0) {
						controller.showErrorMessage(err);
					}
				}
			}
		} else if (evt.getActionCommand().equals(START)) {
			start();
		} else if (evt.getActionCommand().equals(STOP)) {
			stop();
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

	public void start() {
		stop();
		compositionPlayer.start();
	}

	public void stop() {
		if (compositionPlayer.isWorking()) {
			compositionPlayer.stop();
		}
	}
}
