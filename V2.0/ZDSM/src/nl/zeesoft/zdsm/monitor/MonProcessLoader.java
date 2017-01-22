package nl.zeesoft.zdsm.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdsm.database.model.Process;
import nl.zeesoft.zdsm.database.model.ZDSMModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class MonProcessLoader extends HlpGetControllerObject {
	private SortedMap<String,Process>	processesByName	= new TreeMap<String,Process>();

	protected MonProcessLoader() {
		super(ZDSMModel.PROCESS_CLASS_FULL_NAME);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Process();
	}

	@Override
	public void reinitialize() {
		super.reinitialize();
		processesByName.clear();
	}

	@Override
	protected void addedObject(HlpObject object) {
		Process obj = (Process) object;
		processesByName.put(obj.getName(),obj);
	}
	
	protected List<Process> getProcessesAsList() {
		List<Process> r = new ArrayList<Process>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Process)object);
		}
		return r;
	}
}
