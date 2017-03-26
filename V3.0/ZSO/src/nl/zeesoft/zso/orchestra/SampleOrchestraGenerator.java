package nl.zeesoft.zso.orchestra;

import java.io.File;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraGenerator;
import nl.zeesoft.zso.composition.DemoComposition;
import nl.zeesoft.zso.composition.sequencer.Sequencer;

public class SampleOrchestraGenerator extends OrchestraGenerator {
	@Override
	public String generate(Orchestra orch,File dir) {
		String err = super.generate(orch, dir);
		if (err.length()==0) {
			File orchDir = new File(dir.getAbsolutePath() + "/orchestra");
			File sequencerDir = new File(orchDir.getAbsolutePath() + "/sequencer");
			if (sequencerDir.exists()) {
				if (!emptyDirectory(sequencerDir)) {
					err = "Unable to cleanup sequencer directory: " + sequencerDir.getAbsolutePath();
				}
			} else {
				sequencerDir.mkdir();
			}
			generateSequencerDirs(sequencerDir);
			generateSequencerScripts(sequencerDir,orch);
		}
		return err;
	}
	
	@Override
	protected String getJarFileName() {
		return "zso";
	}

	protected void generateSequencerDirs(File sequencerDir) {
		File libDir = new File(sequencerDir.getAbsolutePath() + "/lib");
		libDir.mkdirs();
		File outDir = new File(sequencerDir.getAbsolutePath() + "/out");
		outDir.mkdirs();
	}

	protected void generateSequencerScripts(File sequencerDir,Orchestra orch) {
		String fileName = sequencerDir.getAbsolutePath();
		DemoComposition comp = new DemoComposition();
		comp.toJson().toFile(fileName + "/composition.json",true);
		orch.toJson(false).toFile(fileName + "/orchestra.json",true);
		ZStringBuilder startScript = null;
		if (isWindows()) {
			startScript = this.getBatScriptForAction(orch,Sequencer.PLAY_COMPOSITION,null);
			startScript.toFile(sequencerDir.getAbsolutePath() + "/start.bat");
		} else {
			startScript = this.getScriptForAction(orch,Sequencer.PLAY_COMPOSITION,null);
			startScript.toFile(sequencerDir.getAbsolutePath() + "/start.sh");
		}
	}

	@Override
	protected ZStringBuilder getMemberUpdateScript(File memberDir,Orchestra orch) {
		ZStringBuilder script = super.getMemberUpdateScript(memberDir, orch);
		script.append("cp lib/* sequencer/lib");
		script.append("\n");
		return script;
	}

	@Override
	protected ZStringBuilder getMemberUpdateBatScript(File memberDir,Orchestra orch) {
		ZStringBuilder script = super.getMemberUpdateBatScript(memberDir, orch);
		script.append("xcopy lib\\* sequencer\\lib");
		script.append(" /Y \r\n");
		return script;
	}
	
	
}
