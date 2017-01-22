package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.index.IdxLink;
import nl.zeesoft.zodb.database.index.IdxNumber;
import nl.zeesoft.zodb.database.index.IdxObject;
import nl.zeesoft.zodb.database.index.IdxString;
import nl.zeesoft.zodb.database.index.IdxUniqueConstraint;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.file.FileIO;

public final class DbIndexBuilder {
	private IdxObject							index					= null;
	private boolean 							buildIndexDone 			= false;
	private boolean 							buildIndexError			= false;
	
	public DbIndexBuilder(IdxObject idx) {
		index = idx;
	}
	
	protected boolean buildIndex() {
		boolean error = false;
		List<MdlClass> clsList = new ArrayList<MdlClass>();
		if (index instanceof IdxString) {
			clsList.add(((IdxString)index).getString().getCls());
		} else if (index instanceof IdxNumber) {
			clsList.add(((IdxNumber)index).getNumber().getCls());
		} else if (index instanceof IdxLink) {
			clsList.add(((IdxLink)index).getLink().getCls());
		} else if (index instanceof IdxUniqueConstraint) {
			for (MdlClass cls: ((IdxUniqueConstraint)index).getUniqueConstraint().getClassesList()) {
				clsList.add(cls);
			}
		} else {
			return !error;
		}
		final List<MdlClass> indexClasses = clsList;
		if (indexClasses.size()>0) {
			Messenger.getInstance().debug(this,"Building index: " + index.getDirName() + " ...");
			final ReqGet reqGetObjects = new ReqGet(indexClasses.get(0).getFullName());
			reqGetObjects.setLimit(1000);
			reqGetObjects.getProperties().add(ReqGet.ALL_PROPERTIES);
			reqGetObjects.addSubscriber(new EvtEventSubscriber() {
				private ReqGet reqGetNextObjects = null;
				private int reqGetNextObjectsStart = 0;
				private int indexClassIndex = 0;
				private boolean first = true;
				@Override
				public void handleEvent(EvtEvent e) {
					if (first) {
						reqGetNextObjects = reqGetObjects;
						first = false;
					}
					if (e.getValue()==reqGetNextObjects) {
						if (reqGetNextObjects.getObjects().size()>0) {
							for (ReqDataObject reqObj: reqGetNextObjects.getObjects()) {
								if (index instanceof IdxString) {
									((IdxString)index).addObject(reqObj.getDataObject());
								} else if (index instanceof IdxNumber) {
									((IdxNumber)index).addObject(reqObj.getDataObject());
								} else if (index instanceof IdxLink) {
									IdxLink lnkIdx = (IdxLink) index; 
									List<Long> linkValues = reqObj.getDataObject().getLinkValue(lnkIdx.getLink().getName());
									lnkIdx.addChildValues(reqObj.getDataObject().getId(),linkValues);
								} else if (index instanceof IdxUniqueConstraint) {
									boolean added = ((IdxUniqueConstraint)index).addObject(indexClasses.get(indexClassIndex).getFullName(),reqObj.getDataObject());
									if (!added) {
										buildIndexError();
										break;
									}
								}
							}
						}
						if (buildIndexError) {
							reqGetNextObjects = null;
						} else if (reqGetNextObjects.getObjects().size()==0) {
							reqGetNextObjects = null;
						} else if (reqGetNextObjects.getObjects().size()==1000) {
							reqGetNextObjects = new ReqGet(indexClasses.get(indexClassIndex).getFullName());
							reqGetNextObjectsStart += 1000;
							reqGetNextObjects.setStart(reqGetNextObjectsStart);
							reqGetNextObjects.setLimit(1000);
							reqGetNextObjects.getProperties().add(ReqGet.ALL_PROPERTIES);
							reqGetNextObjects.addSubscriber(this);
						} else if (indexClassIndex < (indexClasses.size() - 1)) {
							indexClassIndex++;
							reqGetNextObjects = new ReqGet(indexClasses.get(indexClassIndex).getFullName());
							reqGetNextObjectsStart = 0;
							reqGetNextObjects.setStart(reqGetNextObjectsStart);
							reqGetNextObjects.setLimit(1000);
							reqGetNextObjects.getProperties().add(ReqGet.ALL_PROPERTIES);
							reqGetNextObjects.addSubscriber(this);
						} else {
							reqGetNextObjects=null;
						}
						if (reqGetNextObjects!=null) {
							DbRequestQueue.getInstance().addRequest(reqGetNextObjects,this);
						} else {
							buildIndexDone();
						}
					}
				}
			});
			buildIndexError = false;
			buildIndexDone = false;
			DbRequestQueue.getInstance().addRequest(reqGetObjects,this);
		} else {
			buildIndexError = false;
			buildIndexDone = true;
		}
	
		while (!buildIndexDone) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				Messenger.getInstance().error(this,"Building index was interrupted");
				error = true;
			}
		}
		
		if (!buildIndexError) {
			SortedMap<Integer, StringBuilder> changedFiles = index.getChangedFiles(0);
			for (Entry<Integer, StringBuilder> entry: changedFiles.entrySet()) {
				FileIO file = new FileIO();
				if (!file.writeFile(index.getDirName() + entry.getKey(),entry.getValue())) {
					Messenger.getInstance().error(this,"Unable to write index file: " + index.getDirName() + entry.getKey());
					error = true;
					break;
				}
			}
		} else {
			error = true;
		}
		
		if (!error) {
			Messenger.getInstance().debug(this,"Built index: " + index.getDirName());
		}
		return !error;
	}
	
	private final void buildIndexDone() {
		buildIndexDone = true;
	}

	private final void buildIndexError() {
		buildIndexError = true;
	}
}
