package nl.zeesoft.zjmo;

import java.io.File;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraGenerator;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.controller.OrchestraController;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;

/**
 * Entry point for local orchestra generation and member control.
 */
public class Orchestrator {
	public static final String	GENERATE	= "GENERATE";
	public static final String	UPDATE		= "UPDATE";
	public static final String	START		= "START";
	public static final String	STOP		= "STOP";
	public static final String	CONTROL		= "CONTROL";
	
	public static boolean isOrchestratorAction(String action) {
		boolean r = false;
		if (action.equals(GENERATE) ||
			action.equals(UPDATE) ||
			action.equals(START) ||
			action.equals(STOP) ||
			action.equals(CONTROL)
			) {
			r = true;
		}
		return r;
	}

	public static void main(String[] args) {
		String err = "";

		String action = "";
		
		String orchestraClassName = "";
		String generateDirectory = "";

		String positionName = Orchestra.CONDUCTOR;
		int positionBackupNumber = 0;
		
		if (args!=null && args.length>=2) {
			action = args[0];
			if (action.equals(GENERATE) || action.equals(UPDATE)) {
				orchestraClassName = args[1];
				if (args.length>=3) {
					generateDirectory = args[2];
				}
			} else if (
				action.equals(START) || 
				action.equals(STOP)
				) {
				orchestraClassName = args[1];
				positionName = args[2];
				if (args.length>=4) {
					try {
						positionBackupNumber = Integer.parseInt(args[3]);
					} catch(NumberFormatException e) {
						action = "";
						err = "Unable to parse position backup number: '" + args[3] + "', error: " + e;
					}
				}
			} else if (action.equals(CONTROL)) {
				orchestraClassName = args[1];
			}
		}
		
		Orchestra orch = getOrchestraForClassName(orchestraClassName);
		if (orch==null) {
			err = "The second parameter must refer to a valid orchestra class name";
		}
		
		if (orch!=null) {
			if (action.equals(GENERATE)) {
				File genDir = new File(generateDirectory);
				if (generateDirectory.length()>0 && !genDir.isDirectory()) {
					err = "Orchestra generation requires valid directory as a third parameter";
				}
				if (err.length()==0) {
					orch.initialize();
					OrchestraGenerator generator = orch.getNewGenerator();
					System.out.println("Generating " + orchestraClassName + " to directory: " + genDir.getAbsolutePath() + " ...");
					err = generator.generate(orch,genDir);
				}
			} else if (action.equals(UPDATE)) {
				File genDir = new File(generateDirectory);
				if (generateDirectory.length()>0 && !genDir.isDirectory()) {
					err = "Orchestra update requires valid directory as a third parameter";
				}
				if (err.length()==0) {
					JsFile json = new JsFile();
					err = json.fromFile(genDir.getAbsolutePath() + "/orchestra/orchestra.json");
					if (err.length()==0) {
						orch.fromJson(json);
						OrchestraGenerator generator = orch.getNewGenerator();
						System.out.println("Updating " + orchestraClassName + " in directory: " + genDir.getAbsolutePath() + " ...");
						err = generator.generate(orch,genDir);
					}
				}
			} else if (action.equals(START) || action.equals(STOP)) {
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
							mem = orch.getNewConductor(null,positionBackupNumber);
						} else {
							mem = orch.getNewPlayer(null,positionName,positionBackupNumber);
						}
						if (!mem.start()) {
							mem.stop();
							err = "Failed to start " + mem.getId();
						}
					} else if (action.equals(STOP)) {
						MemberClient client = member.getNewControlClient(null,null);
						client.sendCommand(ProtocolControl.STOP_PROGRAM);
					}
				}
			} else if (action.equals(CONTROL)) {
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
					OrchestraController controller = orch.getNewController(true);
					err = controller.start();
				}
			}
		}
		
		if (err.length()>0) {
			System.err.println(err);
			System.exit(1);
		}
	}
	
	public static Orchestra getOrchestraForClassName(String orchestraClassName) {
		Orchestra orch = null;
		if (orchestraClassName.length()>0) {
			Object obj = null;
			try {
				Class<?> clas = Class.forName(orchestraClassName);
				obj = clas.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (obj!=null && obj instanceof Orchestra) {
				orch = (Orchestra) obj;
			}
		}
		return orch;
	}
}
