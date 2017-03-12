package nl.zeesoft.zjmo;

import java.io.File;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraGenerator;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
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
				OrchestraGenerator generator = orch.getNewGenerator();
				System.out.println("Generating " + generateClassName + " to directory: " + genDir.getAbsolutePath() + " ...");
				err = generator.generate(orch,genDir);
			}
		} else if (action.equals(START) || action.equals(STOP)) {
			Orchestra orch = new Orchestra();
			OrchestraMember member = null;
			File orchJs = new File("orchestra.json");
			if (!orchJs.exists()) {
				err = "Orchestra JSON file not found: orchestra.json";
			}
			if (err.length()==0) {
				JsFile jsonFile = new JsFile();
				err = jsonFile.fromFile(orchJs.getAbsolutePath());
				if (err.length()>0) {
					err = "Error parsing orchestra.json: " + err;
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
						mem = orch.getNewConductor(null);
					} else {
						mem = orch.getNewPlayer(null,positionName,positionBackupNumber);
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
}
