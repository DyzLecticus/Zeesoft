package nl.zeesoft.znlb.context;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;

public class Language implements InitializerDatabaseObject {
	public String				name			= "";
	public String				code			= "";
	public List<MasterContext>	masterContexts	= new ArrayList<MasterContext>();
	
	public MasterContext addMasterContext(String name, String desc) {
		MasterContext mc = new MasterContext();
		mc.name = name;
		mc.desc = desc;
		masterContexts.add(mc);
		return mc;
	}

	@Override
	public String getObjectName() {
		return name;
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("code",code,true));
		JsElem mcsElem = new JsElem("masterContexts",true);
		json.rootElement.children.add(mcsElem);
		for (MasterContext mc: masterContexts) {
			JsElem mcElem = new JsElem();
			mcsElem.children.add(mcElem);
			mcElem.children.add(new JsElem("name",mc.name,true));
			mcElem.children.add(new JsElem("desc",mc.desc,true));
			JsElem csElem = new JsElem("contexts",true);
			mcElem.children.add(csElem);
			for (Context c: mc.contexts) {
				JsElem cElem = new JsElem();
				csElem.children.add(cElem);
				cElem.children.add(new JsElem("name",c.name,true));
				cElem.children.add(new JsElem("desc",c.desc,true));
			}
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			masterContexts.clear();
			code = json.rootElement.getChildString("code",code);
			JsElem mcsElem = json.rootElement.getChildByName("masterContexts");
			if (mcsElem!=null) {
				for (JsElem mcElem: mcsElem.children) {
					MasterContext mc = new MasterContext();
					mc.name = mcElem.getChildString("name",mc.name);
					mc.desc = mcElem.getChildString("desc",mc.desc);
					JsElem csElem = mcElem.getChildByName("contexts");
					if (mc.name.length()>0) {
						masterContexts.add(mc);
						if (csElem!=null) {
							for (JsElem cElem: csElem.children) {
								Context c = new Context();
								c.name = cElem.getChildString("name",mc.name);
								c.desc = cElem.getChildString("desc",mc.desc);
								if (c.name.length()>0) {
									mc.contexts.add(c);
								}
							}
						}
					}
				}
			}
		}
	}
}
