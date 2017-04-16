package nl.zeesoft.zjmo.orchestra.controller;

import java.io.File;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.client.OrchestraConnector;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;
import nl.zeesoft.zjmo.orchestra.protocol.PublishRequest;

public class ControllerPublishWorker extends Worker {
	private OrchestraController 	controller 		= null;
	private OrchestraConnector		workConnector	= null;
	
	private	Orchestra				orchestraUpdate	= null;

	public ControllerPublishWorker(Messenger msgr, WorkerUnion union,OrchestraController controller,OrchestraConnector workConnector) {
		super(msgr, union);
		setSleep(1000);
		this.controller = controller;
		this.workConnector = workConnector;
	}

	public void publishOrchestraUpdate(Orchestra orchestraUpdate) {
		this.orchestraUpdate = orchestraUpdate;
		start();
	}
	
	@Override
	public void whileWorking() {
		String err = "";
		JsFile json = orchestraUpdate.toJson(false);
		json.rootElement.children.add(new JsElem("requestType",ProtocolWork.UPDATE_ORCHESTRA,true));
		PublishRequest pr = new PublishRequest();
		pr.setChannelName(Orchestra.ORCHESTRA_CRITICAL);
		pr.setRequest(json);
		pr = workConnector.publishRequest(pr);
		if (pr==null) {
			err = "Failed to publish request";
		} else if (pr.getError().length()>0) {
			err = pr.getError();
		} else {
			File f = new File("orchestra.json");
			if (f.exists()) {
				err = orchestraUpdate.toJson(false).toFile("orchestra.json",true);
			}
			controller.publishedOrchestraUpdate();
		}
		if (err.length() > 0) {
			controller.showErrorMessage(err);
		}
		stop();
	}
}
