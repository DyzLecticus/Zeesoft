package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.DatabaseClientListener;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;

public abstract class DatabaseEntityObject extends EntityObject implements DatabaseClientListener {
	protected DatabaseEntityObject(EntityValueTranslator t) {
		super(t);
	}
	
	public void install() {
		getData();
	}

	@Override
	public void initialize() {
		getData();
	}

	@Override
	public void handledRequest(DatabaseResponse res, ZStringBuilder err, Exception ex) {
		if (err.length()>0) {
			if (getTranslator().logDatabaseRequestFailures()) {
				getTranslator().getConfiguration().error(this,err.toString(),ex);
			}
			if (!isInitialized()) {
				initializeEntityValues();
				initialized();
			}
		} else {
			if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
				if (res.results.size()==0) {
					initializeEntityValues();
					DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
					request.name = getName();
					request.obj = toJson();
					getTranslator().getConfiguration().handleDatabaseRequest(request,this);
				} else if (res.results.get(0).obj!=null){
					fromJson(res.results.get(0).obj);
					getTranslator().getConfiguration().debug(this,"Loaded entity values from database: " + getExternalValues().size());
				}
			}
			if (!isInitialized()) {
				initialized();
			}
		}
	}
	
	protected JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem evsElem = new JsElem("entityValues",true);
		json.rootElement.children.add(evsElem);
		for (EntityValue ev: getExternalValues().values()) {
			JsElem evElem = new JsElem();
			evsElem.children.add(evElem);
			evElem.children.add(new JsElem("ev",ev.externalValue,true));
			evElem.children.add(new JsElem("iv",ev.internalValue.substring(getInternalValuePrefix().length()),true));
		}
		return json;
	}
	
	protected void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			JsElem evsElem = json.rootElement.getChildByName("entityValues");
			if (evsElem!=null) {
				for (JsElem evElem: evsElem.children) {
					EntityValue ev = new EntityValue();
					ev.externalValue = evElem.getChildString("ev");
					ev.internalValue = evElem.getChildString("iv");
					addEntityValue(ev.externalValue,ev.internalValue,ev.externalValue);
				}
			}
		}
	}
	
	private String getName() {
		String[] split = (getClass().getName()).split("\\.");
		return "ZEVT/" + split[(split.length - 1)];
	}
	
	private void getData() {
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		request.name = getName(); 
		getTranslator().getConfiguration().handleDatabaseRequest(request,this);
	}
}
