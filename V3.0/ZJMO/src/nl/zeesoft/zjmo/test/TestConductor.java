package nl.zeesoft.zjmo.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.Protocol;
import nl.zeesoft.zjmo.orchestra.members.Conductor;

public class TestConductor {

	public static void main(String[] args) {
		Conductor con = new Conductor(TestOrchestra.getTestOrchestra()) {
			@Override
			protected void stopProgram() {
				System.out.println("Conductor stop program method called");
				stop();
				System.out.println("Conductor stopped");
			}
		};
		boolean started = con.start();
		if (started) {
			MemberClient client = new MemberClient("localhost",5433);
			client.open();
			if (!client.isOpen()) {
				System.err.println("Failed to connect to conductor");
			} else {
				ZStringBuilder response = client.sendCommand(Protocol.STOP_PROGRAM);
				System.out.println("Stop program command response: " + response);
			}
			/*
			if (con.isWorking()) {
				con.stop();
			}
			*/
		} else {
			System.err.println("Failed to start conductor");
		}
	}
}
