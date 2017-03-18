package nl.zeesoft.zso.orchestra;

import java.io.File;

import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraGenerator;

public class SampleOrchestraGenerator extends OrchestraGenerator {
	@Override
	public String generate(Orchestra orch,File dir) {
		return super.generate(orch, dir);
	}
	
	@Override
	protected String getJarFileName() {
		return "zso";
	}
}
