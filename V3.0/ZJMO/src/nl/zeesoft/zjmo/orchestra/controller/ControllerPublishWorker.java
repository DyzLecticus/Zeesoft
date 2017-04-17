package nl.zeesoft.zjmo.orchestra.controller;

import java.io.File;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.client.ConductorConnector;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class ControllerPublishWorker extends Worker {
	private OrchestraController 	controller 	= null;
	private ConductorConnector		connector	= null;
	
	private	Orchestra				orchestraUpdate	= null;

	public ControllerPublishWorker(Messenger msgr, WorkerUnion union,OrchestraController controller,ConductorConnector connector) {
		super(msgr, union);
		setSleep(1000);
		this.controller = controller;
		this.connector = connector;
	}

	public void publishOrchestraUpdate(Orchestra orchestraUpdate) {
		this.orchestraUpdate = orchestraUpdate;
		start();
	}
	
	@Override
	public void whileWorking() {
		String err = "";
		JsFile json = orchestraUpdate.toJson(false);
		json.rootElement.children.add(0,new JsElem("command",ProtocolControlConductor.PUBLISH_ORCHESTRA,true));
		List<ActiveClient> clients = connector.getOpenClients(Orchestra.CONDUCTOR);
		if (clients.size()==0) {
			err = "Not connected to a conductor";
		} else {
			ZStringBuilder response = clients.get(0).getClient().writeOutputReadInput(json.toStringBuilder(),1000);
			if (response==null || !response.equals(ProtocolObject.getExecutedCommandResponse())) {
				err = "Failed to publish changes: " + ProtocolObject.getErrorFromJson(response);
			} else {
				File f = new File("orchestra.json");
				if (f.exists()) {
					err = orchestraUpdate.toJson(false).toFile("orchestra.json",true);
				}
				controller.publishedOrchestraUpdate();
			}
		}
		if (err.length() > 0) {
			controller.showErrorMessage(err);
		}
		stop();
	}
}
