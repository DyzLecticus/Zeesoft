package nl.zeesoft.zjmo.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.members.Player;

public class MockPlayers extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockPlayers*.");
		System.out.println("The *MockPlayers* uses the *MockTestOrchestra*.");
	}

	@Override
	protected Object initialzeMock() {
		List<Player> players = new ArrayList<Player>();
		TestOrchestra orch = (TestOrchestra) getTester().getMockedObject(MockTestOrchestra.class.getName());
		for (OrchestraMember member: orch.getMembers()) {
			if (!member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
				Player player = new Player(orch,member.getPosition().getName(),member.getPositionBackupNumber()) {
					@Override
					protected void stopProgram() {
						//System.out.println("Stopping MockPlayer ...");
						stop();
						//System.out.println("Stopped MockPlayer");
					}
				};
				players.add(player);
			}
		}
		return players;
	}
}
