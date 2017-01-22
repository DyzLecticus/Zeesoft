package nl.zeesoft.zacs.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.Command;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class SimControllerCommands extends HlpGetControllerObject {
	private SortedMap<String,Command>	commandsByCode	= new TreeMap<String,Command>();

	public SimControllerCommands() {
		super(ZACSModel.COMMAND_CLASS_FULL_NAME);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Command();
	}

	@Override
	public void reinitialize() {
		super.reinitialize();
		commandsByCode.clear();
	}

	@Override
	protected void addedObject(HlpObject object) {
		Command command = (Command) object;
		commandsByCode.put(command.getCode().toString(),command);
	}
	
	protected List<Command> getCommandsAsList() {
		List<Command> r = new ArrayList<Command>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Command)object);
		}
		return r;
	}
}
