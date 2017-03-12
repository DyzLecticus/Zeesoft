package nl.zeesoft.zjmo.test.mocks;

import nl.zeesoft.zjmo.orchestra.Orchestra;

public class TestOrchestra extends Orchestra {
	@Override
	public void initialize() {
		addPosition("Database X");
		addPosition("Database Y");
		addPosition("Application server X");
		addPosition("Application server Y");
		
		addMember("Database X",0,LOCALHOST,6543,6542);
		addMember("Database Y",0,LOCALHOST,7654,7653);
		addMember("Application server X",0,LOCALHOST,8765,8764);
		addMember("Application server Y",0,LOCALHOST,9876,9875);

		// Backup databases
		addMember("Database X",1,LOCALHOST,6541,6540);
		addMember("Database Y",1,LOCALHOST,7652,7651);
	}
}
