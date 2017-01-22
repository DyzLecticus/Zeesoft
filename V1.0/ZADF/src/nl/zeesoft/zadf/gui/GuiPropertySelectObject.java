package nl.zeesoft.zadf.gui;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbReferenceFilter;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.datatypes.DtObject;

public abstract class GuiPropertySelectObject extends GuiPropertySelectValue implements EvtEventSubscriber {
	private boolean 				fetchReferences		= false; // Turned off by default
	private String 					fetchType			= QryFetch.TYPE_FETCH_REFERENCES;
	private boolean 				fetching 			= false;
	private boolean 				enableAfterResponse	= false;
	private MdlObjectRefList 		results				= new MdlObjectRefList();
	
	private List<DbReferenceFilter>	filters				= new ArrayList<DbReferenceFilter>();
	
	public GuiPropertySelectObject(String name, int row, int column,
			DtObject valueObject) {
		super(name, row, column, valueObject);
	}
		
	protected void fetchReferences(List<Long> idList) {
		if ((fetchReferences) && (idList.size()>0) && (!fetching)) {
			if (isEnabled()) {
				enableAfterResponse = true;
				setEnabled(false);
			}
			fetching = true;

			String colName = Generic.getValuesFromString(getName())[0];
			String propName = Generic.getValuesFromString(getName())[1];
			
			DbCollection dbCol = DbModel.getInstance().getCollectionByName(colName);
			DbCollectionProperty dbProp = null;
			DbCollection fetchCol = null;
			if (dbCol!=null) {
				dbProp = dbCol.getPropertyByName(propName);
			}
			if (dbProp!=null) {
				fetchCol = dbProp.getRefColl();
			}
	
			if (fetchCol!=null) {
				QryFetchList fetchList = new QryFetchList(null);
				int i = 0;
				for (long id: idList) {
					QryFetch fetch = new QryFetch(fetchCol.getName().getValue(),id);
					fetch.setType(fetchType);
					fetchList.addQuery(fetch);
					if (fetchType.equals(QryFetch.TYPE_FETCH_REFERENCES)) {
						i++;
						if (i==3) {
							break;
						}
					}
				}
		
				ClSession session = GuiController.getInstance().getSession();
				ClRequest request = session.getRequestQueue().getNewRequest(this);
				request.setQueryRequest(fetchList);
				request.addSubscriber(this);
				session.getRequestQueue().addRequest(request, this);
			}
		}
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			ClRequest request = (ClRequest) e.getValue();
			
			MdlObjectRefList newResults = new MdlObjectRefList();
			int added = 0;
			List<String> nameList = new ArrayList<String>();
			if (request.getQueryRequest()!=null) {
				for (QryObject fObj: request.getQueryRequest().getQueries()) {
					QryFetch fetch = (QryFetch) fObj; 
					if ((fetch.getResults()!=null) && (fetch.getMainResults().getReferences().size()>0)) {
						for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
							if (fetchType.equals(QryFetch.TYPE_FETCH_REFERENCES)) {
								if (added<3) {
									nameList.add(ref.getName().getValue());
								} else {
									break;
								}
								added++;
							}
							newResults.getReferences().add(ref);
						}
					}
				}
			}
			results = newResults;
			
			String stringValue = DbModel.getStringValueForNameList(nameList);
			if (!stringValue.equals("")) {
				setStringValue(stringValue);
				updateComponentValue();
			}
			if (enableAfterResponse) {
				setEnabled(true);
			}
		}
	}

	/**
	 * @param fetchReferences the fetchReferences to set
	 */
	public void setFetchReferences(boolean fetchReferences) {
		this.fetchReferences = fetchReferences;
	}

	/**
	 * @param fetchType the fetchType to set
	 */
	public void setFetchType(String fetchType) {
		this.fetchType = fetchType;
	}

	/**
	 * @return the results
	 */
	public MdlObjectRefList getResults() {
		return results;
	}

	/**
	 * @return the filters
	 */
	public List<DbReferenceFilter> getFilters() {
		return filters;
	}
}
