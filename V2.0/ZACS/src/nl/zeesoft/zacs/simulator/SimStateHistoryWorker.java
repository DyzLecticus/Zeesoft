package nl.zeesoft.zacs.simulator;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.StateHistory;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zacs.image.ImgStateHistory;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.server.SvrConfig;
import nl.zeesoft.zodb.database.server.SvrController;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.file.FileIO;

public class SimStateHistoryWorker extends Worker implements EvtEventSubscriber {
	private static SimStateHistoryWorker	stateHistoryWorker		= null;

	private SortedMap<Long,StateHistory>	stateHistoryByDateTime	= new TreeMap<Long,StateHistory>();
	private List<StateHistory>				stateHistory			= new ArrayList<StateHistory>();
	private ReqGet							getRequest				= null;
	private int								getStart				= 0;

	private boolean							initialized				= false;

	private ReqRemove						removeRequest			= null;
	private ReqAdd							addRequest				= null;
	
	private	ImgStateHistory 				generateImage			= new ImgStateHistory();
	private boolean							generateImages			= false;
		
	private SimStateHistoryWorker() {
		// Singleton
	}

	public static SimStateHistoryWorker getInstance() {
		if (stateHistoryWorker==null) {
			stateHistoryWorker = new SimStateHistoryWorker();
		}
		return stateHistoryWorker;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public void initialize() {
		if (isInitialized()) {
			return;
		}
		generateImages = canGenerateImages();
		stateHistoryByDateTime.clear();
		stateHistory.clear();
		getStart = 0;
		resetGetRequest();
		DbRequestQueue.getInstance().addRequest(getRequest,this);
		while(!isInitialized()) {
			GuiController.getInstance().refreshProgressFrame();
			sleep(10);
		}
	}

	@Override
	public void start() {
		setSleep((SimController.getInstance().getControl().getAddHistorySeconds() * 1000));
		if (!isInitialized()) {
			return;
		}
		super.start();
	}
	
	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
	}
	
	@Override
	public void whileWorking() {
		Date started = null;
		if (SimController.DEBUG_PERFORMANCE) {
			started = new Date();
		}
		StateHistory statHist = SimController.getInstance().getNewStateHistory();
		if (SimController.DEBUG_PERFORMANCE) {
			Messenger.getInstance().debug(this,"Retrieving state information took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		}

		if (stateHistoryByDateTime.size()>=SimController.getInstance().getControl().getStateHistoryMaximum()) {
			StateHistory firstStatHist = stateHistoryByDateTime.remove(stateHistoryByDateTime.firstKey());
			stateHistory.remove(firstStatHist);
			
			removeRequest = new ReqRemove(ZACSModel.STATE_HISTORY_CLASS_FULL_NAME);
			removeRequest.getGet().setOrderBy("dateTime");
			removeRequest.getGet().setOrderAscending(false);
			removeRequest.getGet().setStart((SimController.getInstance().getControl().getStateHistoryMaximum() - 1));
			removeRequest.addSubscriber(this);
			DbRequestQueue.getInstance().addRequest(removeRequest,this);
		}

		addRequest = new ReqAdd(ZACSModel.STATE_HISTORY_CLASS_FULL_NAME);
		addRequest.getObjects().add(new ReqDataObject(statHist.toDataObject()));
		addRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(addRequest,this);

		stateHistoryByDateTime.put(statHist.getDateTime(),statHist);
		stateHistory.add(statHist);
		
		boolean generate = false;
		lockMe(this);
		generate = generateImages;
		unlockMe(this);
		if (generate) {
			generateImages(statHist.getDateTime());
		}
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (getRequest!=null && e.getValue()==getRequest) {
			if (!getRequest.hasError()) {
				for (ReqDataObject object: getRequest.getObjects()) {
					StateHistory statHist = new StateHistory();
					statHist.fromDataObject(object.getDataObject()); 
					stateHistoryByDateTime.put(statHist.getDateTime(),statHist);
					stateHistory.add(statHist);
				}
				int increment = getRequest.getObjects().size();
				GuiController.getInstance().incrementProgressFrameDone(increment);
				if (getStart + increment < getRequest.getCount()) {
					getStart += getRequest.getLimit();
					resetGetRequest();
					DbRequestQueue.getInstance().addRequest(getRequest,this);
				} else {
					Messenger.getInstance().debug(this,"Loaded state history objects: " + stateHistory.size());
					setInitialized(true);
				}
			} else {
				Messenger.getInstance().error(this,"Error loading state history: " + getRequest.getErrors().get(0).getMessage());
				setInitialized(true);
			}
		} else if (removeRequest!=null && e.getValue()==removeRequest) {
			if (removeRequest.hasError()) {
				Messenger.getInstance().error(this,"Error removing state history: " + removeRequest.getErrors().get(0).getMessage());
			}
		} else if (addRequest!=null && e.getValue()==addRequest) {
			if (addRequest.hasError()) {
				Messenger.getInstance().error(this,"Error adding state history: " + addRequest.getErrors().get(0).getMessage());
			}
		}
	}
	
	public void generateInitialImages() {
		if (canGenerateImages()) {
			generateImages(0);
			lockMe(this);
			generateImages = true;
			unlockMe(this);
		}
	}
	
	private void setInitialized(boolean initialized) {
		lockMe(this);
		this.initialized = initialized;
		unlockMe(this);
	}

	private boolean isInitialized() {
		boolean r = false;
		lockMe(this);
		r = initialized;
		unlockMe(this);
		return r;
	}
	
	private void generateImages(long now) {
		Date started = null;
		if (SimController.DEBUG_PERFORMANCE) {
			started = new Date();
		}
		long tenMinsAgo = now - (10 * 60000);
		long oneHourAgo = now - (60 * 60000);
		long sixHoursAgo = now - (6 * 60 * 60000);
		long twelveHoursAgo = now - (12 * 60 * 60000);
		long oneDayAgo = now - (24 * 60 * 60000);
		for (int i = 0; i < ImgStateHistory.PROPERTIES.length; i++) {
			long start = 0;
			String fileName = "";
			for (int s = 0; s < 5; s++) {
				fileName = "images/" + ImgStateHistory.PROPERTIES[i];
				if (s==0) {
					start = tenMinsAgo;
					fileName+="-tenMinutes";
				} else if (s==1) {
					start = oneHourAgo;
					fileName+="-oneHour";
				} else if (s==2) {
					start = sixHoursAgo;
					fileName+="-sixHours";
				} else if (s==3) {
					start = twelveHoursAgo;
					fileName+="-twelveHours";
				} else if (s==4) {
					start = oneDayAgo;
					fileName+="-oneDay";
				}
				fileName+=".png";
				// Generate image
				generateImage.setDateTimeStart(start);
				generateImage.setDateTimeEnd(now);
				generateImage.setPropertyName(ImgStateHistory.PROPERTIES[i]);
				generateImage.parseDateTimeValueMapFromStateHistory(stateHistory);
				SvrController.getInstance().writeImage(fileName,generateImage.getBufferedImage());
			}
		}
		if (SimController.DEBUG_PERFORMANCE) {
			Messenger.getInstance().debug(this,"Generating images took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		}
	}
	
	private boolean canGenerateImages() {
		boolean r = false;
		if (FileIO.fileExists(SvrConfig.getInstance().getDataDir())) {
			File dir = new File(SvrConfig.getInstance().getDataDir() + "images/");
			if (!dir.exists()) {
				dir.mkdir();
			}
			if (dir.exists()) {
				r = true;
			}
		}
		return r;
	}
	
	private void resetGetRequest() {
		getRequest = new ReqGet(ZACSModel.STATE_HISTORY_CLASS_FULL_NAME);
		getRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
		getRequest.addSubscriber(this);
		getRequest.setStart(getStart);
		getRequest.setLimit(100);
	}
}
