package nl.zeesoft.zjmo;

import java.io.File;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;

/**
 * Entry point for local orchestra generation and member control.
 */
public class Orchestrator {
	public static final String	GENERATE	= "GENERATE_ORCHESTRA";
	public static final String	START		= "START";
	public static final String	STOP		= "STOP";
	
	public static void main(String[] args) {
		String err = "";

		String action = "";
		
		String generateClassName = "";
		String generateDirectory = "";

		String positionName = Orchestra.CONDUCTOR;
		int positionBackupNumber = 0;
		
		if (args!=null && args.length>=2) {
			action = args[0];
			if (action.equals(GENERATE)) {
				generateClassName = args[1];
				if (args.length>=3) {
					generateDirectory = args[2];
				}
			} else if (
				action.equals(START) || 
				action.equals(STOP)
				) {
				positionName = args[1];
				if (args.length>=3) {
					try {
						positionBackupNumber = Integer.parseInt(args[2]);
					} catch(NumberFormatException e) {
						action = "";
						err = "Unable to parse position backup number: '" + args[2] + "', error: " + e;
					}
				}
			}
		}
		
		if (action.equals(GENERATE)) {
			Orchestra orch = null;
			File genDir = new File(generateDirectory);
			if (generateClassName.length()==0) {
				err = "Orchestra generation requires an orchestra class name as second parameter";
			}
			if (err.length()==0) {
				Object obj = null;
				try {
					Class<?> clas = Class.forName(generateClassName);
					obj = clas.newInstance();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				if (obj==null) {
					err = "Unable to instantiate orchestra class name: " + generateClassName;
				} else if (!(obj instanceof Orchestra)) {
					err = "Class name: " + generateClassName + " must extend " + Orchestra.class.getName();
				} else {
					orch = (Orchestra) obj;
				}
			}
			if (err.length()==0) {
				orch.initialize();
				System.out.println("Generating " + generateClassName + " to directory: " + genDir.getAbsolutePath() + " ...");
				err = generate(orch,genDir);
			}
		} else if (action.equals(START) || action.equals(STOP)) {
			Orchestra orch = new Orchestra();
			OrchestraMember member = null;
			File orchJs = new File("orchestra.json");
			if (!orchJs.exists()) {
				err = "Orchestra JSON file not found: orchestra.json";
			}
			if (err.length()==0) {
				FileIO jsFile = new FileIO();
				StringBuilder json = jsFile.readFile(orchJs.getAbsolutePath());
				JsFile jsonFile = new JsFile();
				jsonFile.fromStringBuilder(new ZStringBuilder(json));
				if (jsonFile.rootElement==null) {
					err = "Failed to parse orchestra.json";
				} else {
					orch.fromJson(jsonFile);
				}
			}
			if (err.length()==0) {
				member = orch.getMemberForPosition(positionName,positionBackupNumber);
				if (member==null) {
					err = "Orchestra member not found: " + positionName + "/" + positionBackupNumber;
				}
			}
			if (member!=null) {
				if (action.equals(START)) {
					MemberObject mem = null;
					if (positionName.equals(Orchestra.CONDUCTOR)) {
						mem = new Conductor(null,orch);
					} else {
						mem = new Player(null,orch,positionName,positionBackupNumber);
					}
					mem.start();
				} else if (action.equals(STOP)) {
					MemberClient client = member.getNewControlClient(null);
					client.sendCommand(ProtocolControl.STOP_PROGRAM);
				}
			}
		}
		
		if (err.length()>0) {
			System.err.println(err);
			System.exit(1);
		}
	}
	
	public static String generate(Orchestra orch,File dir) {
		String err = "";
		FileIO fileIO = new FileIO();
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
			fileIO.emptyDirectory(memberDir);
		} else {
			memberDir.mkdir();
		}
		generateLibraryCopyScript(orchDir,memberDir,orch);
		generateMemberDirs(memberDir,orch);
		generateMemberScripts(memberDir,orch);
		return err;
	}
	
	private static void generateMemberDirs(File memberDir,Orchestra orch) {
		for (OrchestraMember member: orch.getMembers()) {
			File libDir = new File(getDirectoryNameForMember(memberDir,member) + "/lib");
			libDir.mkdirs();
		}
	}

	private static void generateMemberScripts(File memberDir,Orchestra orch) {
		ZStringBuilder json = orch.toJson(false).toStringBuilderReadFormat();
		for (OrchestraMember member: orch.getMembers()) {
			generateMemberStartScript(memberDir,member);
			generateMemberStopScript(memberDir,member);
			generateMemberOrchestraJson(memberDir,member,json);
		}
	}
	
	private static void generateMemberStartScript(File memberDir,OrchestraMember member) {
		FileIO fileIO = new FileIO();
		String fileName = getDirectoryNameForMember(memberDir,member) + "/start";
		StringBuilder script = null;
		if (isWindows()) {
			fileName += ".bat";
			script = getBatScriptForMember(START,member);
		} else {
			fileName += ".sh";
			script = getScriptForMember(START,member);
		}
		fileIO.writeFile(fileName,script);
	}

	private static void generateMemberStopScript(File memberDir,OrchestraMember member) {
		FileIO fileIO = new FileIO();
		String fileName = getDirectoryNameForMember(memberDir,member) + "/stop";
		StringBuilder script = null;
		if (isWindows()) {
			fileName += ".bat";
			script = getBatScriptForMember(STOP,member);
		} else {
			fileName += ".sh";
			script = getScriptForMember(STOP,member);
		}
		fileIO.writeFile(fileName,script);
	}

	private static void generateMemberOrchestraJson(File memberDir,OrchestraMember member,ZStringBuilder json) {
		FileIO fileIO = new FileIO();
		String fileName = getDirectoryNameForMember(memberDir,member) + "/orchestra.json";
		fileIO.writeFile(fileName,json.getStringBuilder());
	}
	
	private static StringBuilder getScriptForMember(String action,OrchestraMember member) {
		StringBuilder script = new StringBuilder();
		script.append("#!/bin/bash\n");
		script.append("\n");
		script.append("java -jar lib/zjmo.jar ");
		script.append(action);
		script.append(" ");
		script.append(member.getPosition().getName());
		script.append(" ");
		script.append(member.getPositionBackupNumber());
		script.append(" ");
		script.append("&");
		script.append("\n");
		return script;
	}

	private static StringBuilder getBatScriptForMember(String action,OrchestraMember member) {
		StringBuilder script = new StringBuilder();
		script.append("java -jar lib\\zjmo.jar ");
		script.append(action);
		script.append(" ");
		script.append(member.getPosition().getName());
		script.append(" ");
		script.append(member.getPositionBackupNumber());
		script.append("\r\n");
		return script;
	}
	
	private static void generateLibraryCopyScript(File orchDir,File memberDir,Orchestra orch) {
		FileIO fileIO = new FileIO();
		String fileName = orchDir.getAbsolutePath() + "/copyLib";
		StringBuilder script = null;
		if (isWindows()) {
			fileName += ".bat";
			script = getLibraryCopyBatScript(memberDir,orch);
		} else {
			fileName += ".sh";
			script = getLibraryCopyScript(memberDir,orch);
		}
		fileIO.writeFile(fileName,script);
	}

	private static StringBuilder getLibraryCopyScript(File memberDir,Orchestra orch) {
		StringBuilder script = new StringBuilder();
		script.append("#!/bin/bash\n");
		script.append("\n");
		for (OrchestraMember member: orch.getMembers()) {
			script.append("cp lib/*  ");
			script.append(getRelativeDirectoryNameForMember(member) + "/lib");
			script.append("\n");
		}
		return script;
	}

	private static StringBuilder getLibraryCopyBatScript(File memberDir,Orchestra orch) {
		StringBuilder script = new StringBuilder();
		for (OrchestraMember member: orch.getMembers()) {
			script.append("xcopy lib\\*  ");
			script.append(getRelativeDirectoryNameForMember(member) + "\\lib");
			script.append(" /Y \r\n");
		}
		return script;
	}

	private static String getRelativeDirectoryNameForMember(OrchestraMember member) {
		String dir = "";
		if (isWindows()) {
			dir = "members\\" + member.getPosition().getName().replace(" ","_") + "\\" + member.getPositionBackupNumber();
		} else {
			dir = "members/" + member.getPosition().getName().replace(" ","_") + "/" + member.getPositionBackupNumber();
		}
		return dir;
	}

	private static String getDirectoryNameForMember(File memberDir,OrchestraMember member) {
		File dir = new File(memberDir.getAbsolutePath() + "/" + member.getPosition().getName().replace(" ","_") + "/" + member.getPositionBackupNumber());
		return dir.getAbsolutePath();
	}
	
	private static boolean isWindows() {
		return System.getProperty("os.name").startsWith("Windows");
	}
}
