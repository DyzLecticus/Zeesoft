package nl.zeesoft.zacs.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.Assignment;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class SimControllerAssignments extends HlpGetControllerObject {
	private SortedMap<String,Assignment>	assignmentsByName	= new TreeMap<String,Assignment>();

	public SimControllerAssignments() {
		super(ZACSModel.ASSIGNMENT_CLASS_FULL_NAME);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Assignment();
	}

	@Override
	public void reinitialize() {
		super.reinitialize();
		assignmentsByName.clear();
	}

	@Override
	protected void addedObject(HlpObject object) {
		Assignment as = (Assignment) object;
		assignmentsByName.put(as.getName().toString(),as);
		if (as.getWorkingModuleId()>0) {
			as.setWorkingModule(SimController.getInstance().getModules().getModuleById(as.getWorkingModuleId()));
		}
	}
	
	protected List<Assignment> getAssignmentsAsList() {
		List<Assignment> list = new ArrayList<Assignment>();
		for (HlpObject object: getObjectsAsList()) {
			list.add((Assignment)object);
		}
		return list;
	}
}
