package nl.zeesoft.zjmo.test.mocks;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;

public class MockPlayers extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockPlayers*.");
		System.out.println("The *MockPlayers* uses the *MockConductor* and the *MockTestOrchestra*.");
	}

	@Override
	protected Object initialzeMock() {
		List<Player> players = new ArrayList<Player>();
		Conductor con = (Conductor) getTester().getMockedObject(MockConductor.class.getName());
		TestOrchestra orch = (TestOrchestra) getTester().getMockedObject(MockTestOrchestra.class.getName());
		for (OrchestraMember member: orch.getMembers()) {
			if (!member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
				Player player = new Player(con.getMessenger(),orch,member.getPosition().getName(),member.getPositionBackupNumber()) {
					@Override
					public boolean start() {
						System.out.println("Starting " + getId() + " (control: " + getControlPort() + ", work: " + getWorkPort() +  ") ...");
						return super.start();
					}
					protected void stopProgram(Worker ignoreWorker) {
						//System.out.println("Stopping MockPlayer ...");
						stop(ignoreWorker);
						//System.out.println("Stopped MockPlayer");
					}
				};
				players.add(player);
			}
		}
		return players;
	}
}
