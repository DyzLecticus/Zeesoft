package nl.zeesoft.zso.orchestra;

import java.io.File;

import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraGenerator;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zso.composition.DemoComposition;

public class SampleOrchestraGenerator extends OrchestraGenerator {
	@Override
	public String generate(Orchestra orch,File dir) {
		return super.generate(orch, dir);
	}
	
	@Override
	protected String getJarFileName() {
		return "zso";
	}

	@Override
	protected void generateMemberStartScript(Orchestra orch, File memberDir, OrchestraMember member) {
		super.generateMemberStartScript(orch, memberDir, member);
		if (member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
			String fileName = getDirectoryNameForMember(memberDir,member);
			DemoComposition comp = new DemoComposition();
			comp.toJson().toFile(fileName + "/composition.json", true);
		}
	}
}
