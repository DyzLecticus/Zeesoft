package nl.zeesoft.zjmo.test.mocks;

import nl.zeesoft.zjmo.orchestra.Channel;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

public class TestOrchestra extends Orchestra {
	public static final String ORCHESTRA_CRITICAL = "Orchestra critical";
	public static final String ORCHESTRA_OPTIONAL = "Orchestra optional";
	
	@Override
	public void initialize() {
		// Positions
		addPosition("Database X");
		addPosition("Database Y");
		addPosition("Application server X");
		addPosition("Application server Y");

		// Members
		getConductors().get(0).setWorkRequestTimeoutDrain(true);
		addMember(CONDUCTOR,1,LOCALHOST,5431,5430,500,false);
		addMember("Database X",0,LOCALHOST,6543,6542,2000,true);
		addMember("Database Y",0,LOCALHOST,7654,7653,2000,true);
		addMember("Application server X",0,LOCALHOST,8765,8764,500,false);
		addMember("Application server Y",0,LOCALHOST,9876,9875,500,false);
		addMember("Database X",1,LOCALHOST,6541,6540,2000,true);
		addMember("Database Y",1,LOCALHOST,7652,7651,2000,true);

		// Channels
		Channel cri = addChannel(ORCHESTRA_CRITICAL,true);
		Channel opt = addChannel(ORCHESTRA_OPTIONAL,false);
		for (OrchestraMember member: getMembers()) {
			cri.getSubscriberIdList().add(member.getId());
			opt.getSubscriberIdList().add(member.getId());
		}
	}
}
