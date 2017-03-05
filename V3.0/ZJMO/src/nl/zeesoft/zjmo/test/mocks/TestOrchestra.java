package nl.zeesoft.zjmo.test.mocks;

import nl.zeesoft.zjmo.orchestra.Orchestra;

public class TestOrchestra extends Orchestra {
	@Override
	public void initialize() {
		addPosition("Database X");
		addPosition("Database Y");
		addPosition("Application server X");
		addPosition("Application server Y");
		
		addMember("Database X",0,"localhost",6543,6542);
		addMember("Database Y",0,"localhost",7654,7653);
		addMember("Application server X",0,"localhost",8765,8764);
		addMember("Application server Y",0,"localhost",9876,9875);

		// Backup databases
		addMember("Database X",1,"localhost",6541,6540);
		addMember("Database Y",1,"localhost",7652,7651);
	}
}
