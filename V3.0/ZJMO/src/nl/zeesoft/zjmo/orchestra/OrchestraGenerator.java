package nl.zeesoft.zjmo.orchestra;

import java.io.File;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.Orchestrator;

/**
 * Generates the orchestra directory containing scripts and folders required to manage MemberObject instances.
 */
public class OrchestraGenerator {
	public String generate(Orchestra orch,File dir) {
		String err = "";
		File orchDir = new File(dir.getAbsolutePath() + "/orchestra");
		if (!orchDir.exists()) {
			orchDir.mkdirs();
		}
		File libDir = new File(orchDir.getAbsolutePath() + "/lib");
		if (!libDir.exists()) {
			libDir.mkdir();
		}
		File memberDir = new File(orchDir.getAbsolutePath() + "/members");
		if (memberDir.exists()) {
			if (!emptyDirectory(memberDir)) {
				err = "Unable to cleanup member directory: " + memberDir.getAbsolutePath();
			}
		} else {
			memberDir.mkdir();
		}
		if (err.length()==0) {
			generateLibraryCopyScript(orchDir,memberDir,orch);
			generateMemberDirs(memberDir,orch);
			generateMemberScripts(memberDir,orch);
			if (orch.isLocalHost()) {
				generateOrchestraScripts(orchDir,orch);
			}
		}
		return err;
	}
	
	protected String getJarFileName() {
		return "zjmo";
	}
	
	protected void generateMemberDirs(File memberDir,Orchestra orch) {
		for (OrchestraMember member: orch.getMembers()) {
			File libDir = new File(getDirectoryNameForMember(memberDir,member) + "/lib");
			libDir.mkdirs();
			File outDir = new File(getDirectoryNameForMember(memberDir,member) + "/out");
			outDir.mkdirs();
			if (isWindows()) {
				File binDir = new File(getDirectoryNameForMember(memberDir,member) + "/bin");
				binDir.mkdirs();
			}
		}
	}

	protected void generateOrchestraScripts(File orchDir,Orchestra orch) {
		ZStringBuilder startScript = null;
		ZStringBuilder stopScript = null;
		if (isWindows()) {
			startScript = getOrchestraBatScript(orch,Orchestrator.START);
			stopScript = getOrchestraBatScript(orch,Orchestrator.STOP);
			startScript.toFile(orchDir.getAbsolutePath() + "/startOrchestra.bat");
			stopScript.toFile(orchDir.getAbsolutePath() + "/stopOrchestra.bat");
		} else {
			startScript = getOrchestraScript(orch,Orchestrator.START);
			stopScript = getOrchestraScript(orch,Orchestrator.STOP);
			startScript.toFile(orchDir.getAbsolutePath() + "/startOrchestra.sh");
			stopScript.toFile(orchDir.getAbsolutePath() + "/stopOrchestra.sh");
		}
	}

	protected ZStringBuilder getOrchestraScript(Orchestra orch,String action) {
		ZStringBuilder script = new ZStringBuilder();
		script.append("#!/bin/bash\n");
		script.append("\n");
		for (OrchestraMember member: orch.getMembers()) {
			script.append("cd " + getRelativeDirectoryNameForMember(member));
			script.append("\n");
			if (action.equals(Orchestrator.START)) {
				script.append("start.sh");
				script.append("\n");
			} else if (action.equals(Orchestrator.STOP)) {
				script.append("stop.sh");
				script.append("\n");
			}
			script.append("cd ../../../");
			script.append("\n");
		}
		return script;
	}

	protected ZStringBuilder getOrchestraBatScript(Orchestra orch,String action) {
		ZStringBuilder script = new ZStringBuilder();
		for (OrchestraMember member: orch.getMembers()) {
			script.append("cd " + getRelativeDirectoryNameForMember(member));
			script.append("\r\n");
			if (action.equals(Orchestrator.START)) {
				script.append("call start.bat");
				script.append("\r\n");
			} else if (action.equals(Orchestrator.STOP)) {
				script.append("call stop.bat");
				script.append("\r\n");
			}
			script.append("cd ..\\..\\..\\");
			script.append("\r\n");
		}
		return script;
	}

	protected void generateMemberScripts(File memberDir,Orchestra orch) {
		ZStringBuilder json = orch.toJson(false).toStringBuilderReadFormat();
		for (OrchestraMember member: orch.getMembers()) {
			generateMemberStartScript(orch,memberDir,member);
			generateMemberStopScript(orch,memberDir,member);
			json.toFile(getDirectoryNameForMember(memberDir,member) + "/orchestra.json");
			if (isWindows()) {
				generateMemberBackgroundScript(memberDir,member);
			}
		}
	}
	
	protected void generateMemberStartScript(Orchestra orch,File memberDir,OrchestraMember member) {
		String fileName = getDirectoryNameForMember(memberDir,member);
		ZStringBuilder script = null;
		if (isWindows()) {
			fileName += "/bin/start.bat";
			script = getBatScriptForMember(orch,Orchestrator.START,member);
		} else {
			fileName += "/start.sh";
			script = getScriptForMember(orch,Orchestrator.START,member);
		}
		script.toFile(fileName);
	}

	protected void generateMemberStopScript(Orchestra orch,File memberDir,OrchestraMember member) {
		String fileName = getDirectoryNameForMember(memberDir,member);
		ZStringBuilder script = null;
		if (isWindows()) {
			fileName += "/bin/stop.bat";
			script = getBatScriptForMember(orch,Orchestrator.STOP,member);
		} else {
			fileName += "/stop.sh";
			script = getScriptForMember(orch,Orchestrator.STOP,member);
		}
		script.toFile(fileName);
	}

	protected void generateMemberBackgroundScript(File memberDir,OrchestraMember member) {
		String fileName = getDirectoryNameForMember(memberDir,member) + "/bin/background.vbs";
		ZStringBuilder script = new ZStringBuilder();
		script.append("CreateObject(\"Wscript.Shell\").Run \"\"\"\" & WScript.Arguments(0) & \"\"\"\", 0, False");
		script.append("\r\n");
		script.toFile(fileName);

		fileName = getDirectoryNameForMember(memberDir,member) + "/start.bat";
		script = new ZStringBuilder();
		script.append("wscript.exe \"bin\\background.vbs\" \"bin\\start.bat\"");
		script.append("\r\n");
		script.toFile(fileName);
		
		fileName = getDirectoryNameForMember(memberDir,member) + "/stop.bat";
		script = new ZStringBuilder();
		script.append("wscript.exe \"bin\\background.vbs\" \"bin\\stop.bat\"");
		script.append("\r\n");
		script.toFile(fileName);
	}
	
	protected ZStringBuilder getScriptForMember(Orchestra orch,String action,OrchestraMember member) {
		ZStringBuilder script = new ZStringBuilder();
		script.append("#!/bin/bash\n");
		script.append("\n");
		script.append("java -jar lib/");
		script.append(getJarFileName());
		script.append(".jar ");
		script.append(action);
		script.append(" ");
		script.append(orch.getClass().getName());
		script.append(" ");
		script.append("\"");
		script.append(member.getPosition().getName());
		script.append("\"");
		script.append(" ");
		script.append("" + member.getPositionBackupNumber());
		script.append(" ");
		script.append(" 1>out/");
		script.append(action);
		script.append("_OUT.log ");
		script.append(" 2>out/");
		script.append(action);
		script.append("_ERR.log ");
		script.append("&");
		script.append("\n");
		return script;
	}

	protected ZStringBuilder getBatScriptForMember(Orchestra orch,String action,OrchestraMember member) {
		ZStringBuilder script = new ZStringBuilder();
		script.append("java -jar lib\\");
		script.append(getJarFileName());
		script.append(".jar ");
		script.append(action);
		script.append(" ");
		script.append(orch.getClass().getName());
		script.append(" ");
		script.append("\"");
		script.append(member.getPosition().getName());
		script.append("\"");
		script.append(" ");
		script.append("" + member.getPositionBackupNumber());
		script.append(" 1>out\\");
		script.append(action);
		script.append("_OUT.txt ");
		script.append(" 2>out\\");
		script.append(action);
		script.append("_ERR.txt ");
		script.append("\r\n");
		return script;
	}
	
	protected void generateLibraryCopyScript(File orchDir,File memberDir,Orchestra orch) {
		String fileName = orchDir.getAbsolutePath() + "/copyLib";
		ZStringBuilder script = null;
		if (isWindows()) {
			fileName += ".bat";
			script = getLibraryCopyBatScript(memberDir,orch);
		} else {
			fileName += ".sh";
			script = getLibraryCopyScript(memberDir,orch);
		}
		script.toFile(fileName);
	}

	protected ZStringBuilder getLibraryCopyScript(File memberDir,Orchestra orch) {
		ZStringBuilder script = new ZStringBuilder();
		script.append("#!/bin/bash\n");
		script.append("\n");
		for (OrchestraMember member: orch.getMembers()) {
			script.append("cp lib/*  ");
			script.append(getRelativeDirectoryNameForMember(member) + "/lib");
			script.append("\n");
		}
		return script;
	}

	protected ZStringBuilder getLibraryCopyBatScript(File memberDir,Orchestra orch) {
		ZStringBuilder script = new ZStringBuilder();
		for (OrchestraMember member: orch.getMembers()) {
			script.append("xcopy lib\\*  ");
			script.append(getRelativeDirectoryNameForMember(member) + "\\lib");
			script.append(" /Y \r\n");
		}
		return script;
	}

	protected String getRelativeDirectoryNameForMember(OrchestraMember member) {
		String dir = "";
		if (isWindows()) {
			dir = "members\\" + member.getPosition().getName().replace(" ","_") + "\\" + member.getPositionBackupNumber();
		} else {
			dir = "members/" + member.getPosition().getName().replace(" ","_") + "/" + member.getPositionBackupNumber();
		}
		return dir;
	}

	protected String getDirectoryNameForMember(File memberDir,OrchestraMember member) {
		File dir = new File(memberDir.getAbsolutePath() + "/" + member.getPosition().getName().replace(" ","_") + "/" + member.getPositionBackupNumber());
		return dir.getAbsolutePath();
	}

	protected boolean emptyDirectory(File dir) {
		boolean delete = true;
		for (File f: dir.listFiles()) {
			if (f.isFile()) {
				delete = f.delete();
				if (!delete) {
					break;
				}
			}
		}
		if (delete) {
			for (File f: dir.listFiles()) {
				if (f.isDirectory()) {
					delete = emptyDirectory(f);
					if (!delete) {
						break;
					} else {
						f.delete();
					}
				}
			}
		}
		return delete;
	}

	protected boolean isWindows() {
		return System.getProperty("os.name").startsWith("Windows");
	}
}
