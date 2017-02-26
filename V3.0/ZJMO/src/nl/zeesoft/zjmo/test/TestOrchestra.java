package nl.zeesoft.zjmo.test;

import nl.zeesoft.zjmo.orchestra.Orchestra;

public class TestOrchestra {

	public static void main(String[] args) {
		Orchestra orc = new Orchestra();
		orc.addPosition("Database X");
		orc.addPosition("Database Y");
		orc.addPosition("Confabulator X");
		orc.addPosition("Confabulator Y");
		
		orc.addMember("Database X",0,"localhost",6543,6542);
		orc.addMember("Database Y",0,"localhost",7654,7653);
		orc.addMember("Confabulator X",0,"localhost",8765,8764);
		orc.addMember("Confabulator Y",0,"localhost",9876,9875);

		// Backup databases
		orc.addMember("Database X",1,"localhost",6541,6540);
		orc.addMember("Database Y",1,"localhost",7652,7651);
		
		System.out.println("Orchestra JSON:");
		System.out.println(orc.toJson(true).toStringBuilderReadFormat());
		
		orc.fromJson(orc.toJson(false));

		System.out.println("After to and from JSON:");
		System.out.println(orc.toJson(true).toStringBuilderReadFormat());
	}
}
