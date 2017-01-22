package nl.zeesoft.zadf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbFetch;
import nl.zeesoft.zadf.model.impl.DbFetchFilter;
import nl.zeesoft.zadf.model.impl.DbFilterOperator;
import nl.zeesoft.zadf.model.impl.DbModule;
import nl.zeesoft.zadf.model.impl.DbReport;
import nl.zeesoft.zadf.model.impl.DbReportFormat;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClEntityLoader;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.model.MdlDataObject;

public class RepModel extends ClEntityLoader {
	public static final String 			LOADED_REPORT_MODEL		= "LOADED_REPORT_MODEL";
	
	private static RepModel 			model 					= null;
	
	private long						time					= 0; 

	private RepModel() {
		// Singleton
		super(GuiController.getInstance().getSession());
		String[] entities = {
			DbCollection.class.getName(),
			DbFilterOperator.class.getName(),
			DbFetch.class.getName(),
			DbFetchFilter.class.getName(),
			DbReport.class.getName(),
			DbReportFormat.class.getName()
		};
		initialize(entities);
	}

	public static RepModel getInstance() {
		if (model==null) {
			model = new RepModel();
		}
		return model;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		super.handleEvent(e);
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			for (DbFetch fetch: getFetches()) {
				fetch.addReferencedObject("orderBy",DbModel.getInstance().getCollectionObjectById(DbCollectionProperty.class.getName(),fetch.getOrderBy().getValue()));
			}
			for (DbFetchFilter filter: getFetchFilters()) {
				if (
					(filter.getValue().getValue().startsWith(DbModel.SESSION_PREFIX)) ||
					(filter.getValue().getValue().startsWith(DbModel.DATE_PREFIX))
					) {
					Object value = DbModel.getInstance().evaluateExpressionToValue(filter.getValue().getValue(),null);
					String stringValue = DbModel.getInstance().evaluateExpressionToStringValue(filter.getValue().getValue(),null);
					filter.getValue().setValue("" + value);
					filter.setStringValue(stringValue);
				}
				filter.addReferencedObject("property",DbModel.getInstance().getCollectionObjectById(DbCollectionProperty.class.getName(),filter.getProperty().getValue()));
				filter.addReferencedObject("operator",DbModel.getInstance().getCollectionObjectById(DbFilterOperator.class.getName(),filter.getOperator().getValue()));
			}
			Messenger.getInstance().debug(this, "Loaded report model in " + (new Date().getTime() - time) + " ms");
			this.publishEvent(new EvtEvent(LOADED_REPORT_MODEL,this,LOADED_REPORT_MODEL));
		}
	}

	@Override
	protected void resultsIncomplete() {
		Messenger.getInstance().warn(this, "Report model results are incomplete. Try increasing the maximum fetch load and/or maximum fetch results.");
	}

	@Override
	public void loadEntities() {
		time = new Date().getTime();
		super.loadEntities();
	}

	public DbModule getModuleByName(String modName) {
		return (DbModule) this.getCollectionObjectByName(DbModule.class.getName(), modName);
	}

	public DbCollection getCollectionByName(String colName) {
		return (DbCollection) this.getCollectionObjectByName(DbCollection.class.getName(), colName);
	}

	/**
	 * @return the fetches
	 */
	private List<DbFetch> getFetches() {
		List<DbFetch> list = new ArrayList<DbFetch>();
		for (MdlDataObject obj: this.getCollectionObjects(DbFetch.class.getName())) {
			list.add((DbFetch) obj);
		}
		return list;
	}

	/**
	 * @return the fetch filters
	 */
	private List<DbFetchFilter> getFetchFilters() {
		List<DbFetchFilter> list = new ArrayList<DbFetchFilter>();
		for (MdlDataObject obj: this.getCollectionObjects(DbFetchFilter.class.getName())) {
			list.add((DbFetchFilter) obj);
		}
		return list;
	}

	/**
	 * @return the reports
	 */
	public List<DbReport> getReports() {
		List<DbReport> list = new ArrayList<DbReport>();
		for (MdlDataObject obj: this.getCollectionObjects(DbReport.class.getName())) {
			list.add((DbReport) obj);
		}
		return list;
	}
}
