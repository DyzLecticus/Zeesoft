package nl.zeesoft.zjmo.orchestra.client;

import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

/**
 * Used to proactively connect to conductors.
 */
public class ConductorConnector extends OrchestraConnector {
	public ConductorConnector(Messenger msgr, WorkerUnion uni, boolean control) {
		super(msgr, uni, control);
	}

	@Override
	protected List<OrchestraMember> getOrchestraMembers(Orchestra orch) {
		return orch.getConductors();
	}
}
