package nl.zeesoft.zodb.client;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.MdlCollectionReference;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtObject;

/**
 * This class can be used to load complete entities in one request.
 * It will instantiate all the retrieved objects and link them together to construct the entities.
 */
public class ClEntityLoader extends EvtEventPublisher implements EvtEventSubscriber {
	private ClSession 							session 		= null;
	private QryFetch							fetch			= null;
	private boolean								loading			= false;
	
	public ClEntityLoader(ClSession session) {
		this.session = session;
	}
	
	public void initialize(String[] entities) {
		if (!loading) {
			if ((entities!=null) && (entities.length>=1)) {
				fetch = new QryFetch(entities[0]);
				fetch.setType(QryFetch.TYPE_FETCH_OBJECTS_ENTITY);
				for (int i = 1; i<entities.length; i++) {
					fetch.getEntities().add(entities[i]);
				}
			} else {
				Messenger.getInstance().error(this,"Entity loader requires at one entity class name to load");
			}
		}
	}
	
	public void loadEntities() {
		if (!loading) {
			loading = true;
			QryFetchList fetchList = new QryFetchList(null);
			fetchList.addQuery(fetch);
			ClRequest r = session.getRequestQueue().getNewRequest(this);
			r.setQueryRequest(fetchList);
			r.addSubscriber(this);
			session.getRequestQueue().addRequest(r, this);
		}
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			ClRequest r = (ClRequest) e.getValue();
			for (QryObject qryObject: r.getQueryRequest().getQueries()) {
				if (qryObject instanceof QryFetch) {
					fetch = (QryFetch) qryObject;
				}
			}
			if (fetch.getMainResults().getReferences().size()>0) {
				for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
					setChildObjects(ref.getDataObject());
					setReferencedObjects(ref.getDataObject());
				}
				for (String entity: fetch.getEntities()) {
					MdlObjectRefList entityResults = fetch.getResults().getReferenceListForCollection(entity);
					if (entityResults!=null) {
						for (MdlObjectRef ref: entityResults.getReferences()) {
							setChildObjects(ref.getDataObject());
							setReferencedObjects(ref.getDataObject());
						}
					}
				}
			}
			if (fetch.isResultsIncomplete()) {
				resultsIncomplete();
			} 
			loading = false;
		}
	}
	
	protected void resultsIncomplete() {
		Messenger.getInstance().warn(this, "Fetch results are incomplete. Try increasing the maximum fetch load and/or maximum fetch results.");
	}
	
	private void setChildObjects(MdlDataObject parentObject) {
		for (MdlCollectionReference cColRef: DbConfig.getInstance().getModel().getCollectionReferencesByReferenceClass(parentObject.getClassName().getValue())) {
			if (cColRef.isEntity()) {
				if (cColRef.getModelCollection().getName().equals(fetch.getClassName())) {
					setChildObjects(parentObject,fetch.getMainResults(),cColRef);
				}
				MdlObjectRefList entityResults = fetch.getResults().getReferenceListForCollection(cColRef.getModelCollection().getName());
				if (entityResults!=null) {
					setChildObjects(parentObject,entityResults,cColRef);
				}
			}
		}
	}
	
	private void setChildObjects(MdlDataObject parentObject, MdlObjectRefList results, MdlCollectionReference cColRef) {
		String childCollectionAndPropertyName = cColRef.getModelCollection().getName() +  Generic.SEP_STR + cColRef.getName();
		for (MdlObjectRef cRef: results.getReferences()) {
			DtObject valRef = cRef.getDataObject().getPropertyValue(cColRef.getName());
			if (valRef instanceof DtIdRef) {
				DtIdRef idRef = (DtIdRef) valRef;
				long id = idRef.getValue();
				if (id==parentObject.getId().getValue()) {
					parentObject.addChildObject(childCollectionAndPropertyName, cRef.getDataObject());
				}
			} else if (valRef instanceof DtIdRefList) {
				DtIdRefList idRefList = (DtIdRefList) valRef;
				for (long id: idRefList.getValue()) {
					if (id==parentObject.getId().getValue()) {
						parentObject.addChildObject(childCollectionAndPropertyName, cRef.getDataObject());
						break;
					}
				}
			}
		}
	}
	
	private void setReferencedObjects(MdlDataObject parentObject) {
		for (MdlCollectionReference colRef: DbConfig.getInstance().getModel().getCollectionReferences(parentObject.getClassName().getValue())) {
			MdlObjectRefList entityResults = null;
			for (String entity: fetch.getEntities()) {
				if (colRef.getReference().getName().equals(entity)) {
					entityResults = fetch.getResults().getReferenceListForCollection(entity);
				}
			}
			if ((entityResults==null) && (fetch.getClassName().equals(colRef.getReference().getName()))) {
				entityResults = fetch.getMainResults();
			}
			if (entityResults!=null) {
				DtObject valRef = parentObject.getPropertyValue(colRef.getName());
				if (valRef instanceof DtIdRef) {
					DtIdRef idRef = (DtIdRef) valRef;
					MdlObjectRef objRef = entityResults.getMdlObjectRefById(idRef.getValue());
					if (objRef!=null) {
						parentObject.addReferencedObject(colRef.getName(), objRef.getDataObject());
					}
				} else if (valRef instanceof DtIdRefList) {
					DtIdRefList idRefList = (DtIdRefList) valRef;
					for (long id: idRefList.getValue()) {
						MdlObjectRef objRef = entityResults.getMdlObjectRefById(id);
						if (objRef!=null) {
							parentObject.addReferencedObject(colRef.getName(), objRef.getDataObject());
						}
					}
				}
			}
		}
	}
	
	public List<MdlDataObject> getCollectionObjects(String collection) {
		List<MdlDataObject> objects = new ArrayList<MdlDataObject>();
		if (fetch.getMainResults().getReferences().size()>0) {
			if (collection.equals(fetch.getClassName())) {
				for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
					objects.add(ref.getDataObject());
				}
			}
			MdlObjectRefList entityResults = fetch.getResults().getReferenceListForCollection(collection);
			if (entityResults!=null) {
				for (MdlObjectRef ref: entityResults.getReferences()) {
					objects.add(ref.getDataObject());
				}
			}
		}
		return objects;
	}

	public MdlDataObject getCollectionObjectById(String collection,long id) {
		MdlDataObject object = null;
		if (fetch.getMainResults().getReferences().size()>0) {
			if (collection.equals(fetch.getClassName())) {
				MdlObjectRef ref = fetch.getResults().getMdlObjectRefById(id);
				if (ref!=null) {
					object = ref.getDataObject();
				}
			}
			if (object==null) {
				MdlObjectRefList entityResults = fetch.getResults().getReferenceListForCollection(collection);
				if (entityResults!=null) {
					MdlObjectRef ref = entityResults.getMdlObjectRefById(id);
					if (ref!=null) {
						object = ref.getDataObject();
					}
				}
			}
		}
		return object;
	}

	public MdlDataObject getCollectionObjectByName(String collection,String name) {
		MdlDataObject object = null;
		if (fetch.getMainResults().getReferences().size()>0) {
			if (collection.equals(fetch.getClassName())) {
				MdlObjectRef ref = fetch.getMainResults().getMdlObjectRefByName(name);
				if (ref!=null) {
					object = ref.getDataObject();
				}
			}
			if (object==null) {
				MdlObjectRefList entityResults = fetch.getResults().getReferenceListForCollection(collection);
				if (entityResults!=null) {
					MdlObjectRef ref = entityResults.getMdlObjectRefByName(name);
					if (ref!=null) {
						object = ref.getDataObject();
					}
				}
			}
		}
		return object;
	}

	/**
	 * @return the session
	 */
	public ClSession getSession() {
		return session;
	}

	/**
	 * @return the fetch
	 */
	public QryFetch getFetch() {
		return fetch;
	}
}
