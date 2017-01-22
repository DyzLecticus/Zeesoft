package nl.zeesoft.zacs.simulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zacs.database.model.Example;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class SimControllerExamples extends HlpGetControllerObject {

	public SimControllerExamples() {
		super(ZACSModel.EXAMPLE_CLASS_FULL_NAME);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Example();
	}

	protected List<Example> getExamplesAsList() {
		List<Example> r = new ArrayList<Example>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Example)object);
		}
		return r;
	}
}
