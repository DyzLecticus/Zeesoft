package nl.zeesoft.zals.database.model;

import nl.zeesoft.zac.database.model.ZACDataGenerator;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.event.EvtEvent;

public class ZALSDataGenerator extends ZACDataGenerator {
	private ReqAdd 			addEnvironmentRequest	= new ReqAdd(ZALSModel.ENVIRONMENT_CLASS_FULL_NAME);
	private ReqAdd 			addPlantRequest			= new ReqAdd(ZALSModel.PLANT_CLASS_FULL_NAME);

	protected Environment getNewEnvironment() {
		return new Environment();
	}
	
	@Override
	public void confirmInstallDemoData() {
		// Ignore
	}
	
	@Override
	protected boolean installDemoData() {
		return false;
	}
	
	@Override
	protected void generateInitialData() {
		Messenger.getInstance().debug(this,"Generating environment ...");
		setDone(false);
		addEnvironmentRequest.addSubscriber(this);
		addEnvironmentRequest.getObjects().add(new ReqDataObject(getNewEnvironment().toDataObject()));
		DbRequestQueue.getInstance().addRequest(addEnvironmentRequest,this);
		waitTillDone("Generating environment was interrupted");
		Messenger.getInstance().debug(this,"Generated environment");
	}
	
	@Override
	protected void handleRequestEvent(ReqObject request,EvtEvent evt) {
		// Override to implement
		if (request==addEnvironmentRequest) {
			if (!addEnvironmentRequest.hasError()) {
				int dist = 10;
				addPlantRequest.addSubscriber(this);
				Plant plant = new Plant();
				plant.setEnvironmentId(addEnvironmentRequest.getImpactedIds().get(0));
				plant.setPosX(0 + dist);
				plant.setPosY(0 + dist);
				addPlantRequest.getObjects().add(new ReqDataObject(plant.toDataObject()));
				plant = new Plant();
				plant.setEnvironmentId(addEnvironmentRequest.getImpactedIds().get(0));
				plant.setPosX((Environment.SIZE_X - 1) - dist);
				plant.setPosY(0 + dist);
				addPlantRequest.getObjects().add(new ReqDataObject(plant.toDataObject()));
				plant = new Plant();
				plant.setEnvironmentId(addEnvironmentRequest.getImpactedIds().get(0));
				plant.setPosX(0 + dist);
				plant.setPosY((Environment.SIZE_Y - 1) - dist);
				addPlantRequest.getObjects().add(new ReqDataObject(plant.toDataObject()));
				plant = new Plant();
				plant.setEnvironmentId(addEnvironmentRequest.getImpactedIds().get(0));
				plant.setPosX((Environment.SIZE_X - 1) - dist);
				plant.setPosY((Environment.SIZE_Y - 1) - dist);
				addPlantRequest.getObjects().add(new ReqDataObject(plant.toDataObject()));
				DbRequestQueue.getInstance().addRequest(addPlantRequest,this);
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addEnvironmentRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (request==addPlantRequest) {
			if (!addPlantRequest.hasError()) {
				setDone(true);
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addPlantRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		}
	}
}
