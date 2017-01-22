package nl.zeesoft.zacs.crawler;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zacs.database.model.Crawler;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;


public class CrwController extends HlpGetControllerObject {
	public CrwController() {
		super(ZACSModel.CRAWLER_CLASS_FULL_NAME);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Crawler();
	}
	
	public List<Crawler> getCrawlersAsList() {
		List<Crawler> r = new ArrayList<Crawler>();
		for (HlpObject obj: this.getObjectsAsList()) {
			r.add((Crawler) obj);
		}
		return r;
	}
}
