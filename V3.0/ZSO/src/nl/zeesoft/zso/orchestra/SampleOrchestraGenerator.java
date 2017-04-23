package nl.zeesoft.zso.orchestra;

import java.io.File;

import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraGenerator;
import nl.zeesoft.zso.composition.DemoComposition;

public class SampleOrchestraGenerator extends OrchestraGenerator {
	@Override
	protected String getJarFileName() {
		return "zso";
	}

	@Override
	protected void generateControllerScripts(File controllerDir, Orchestra orch) {
		super.generateControllerScripts(controllerDir, orch);
		DemoComposition comp = new DemoComposition();
		comp.toJson().toFile(controllerDir.getAbsolutePath() + "/composition.json",true);
	}
}
