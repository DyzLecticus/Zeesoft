package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;

public abstract class DatabaseEntityObject extends EntityObject implements JsAble, JsClientListener {
	protected DatabaseEntityObject(Translator t) {
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
	public void handledRequest(JsClientResponse response) {
		if (response.error.length()>0) {
			if (getTranslator().logDatabaseRequestFailures()) {
				getTranslator().getConfiguration().error(this,response.error.toString(),response.ex);
			}
			if (!isInitialized()) {
				initializeEntityValues();
				initialized();
			}
		} else {
			DatabaseResponse res = getTranslator().getConfiguration().handledDatabaseRequest(response);
			if (res!=null && res.request.type.equals(DatabaseRequest.TYPE_GET)) {
				if (res.results.size()==0) {
					initializeEntityValues();
					DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
					request.name = getObjectName();
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
	
	@Override
	public JsFile toJson() {
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
	
	@Override
	public void fromJson(JsFile json) {
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
	
	private String getObjectName() {
		return ModZEVT.NAME + "/Entities/" + getName();
	}
	
	private void getData() {
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		request.name = getObjectName(); 
		getTranslator().getConfiguration().handleDatabaseRequest(request,this);
	}
}
