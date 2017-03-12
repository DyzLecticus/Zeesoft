package nl.zeesoft.zjmo;

import java.io.File;

import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

/**
 * Entry point for local Zeesoft JSON Machine control and generation.
 */
public class Orchestrator {
	public static final String	GENERATE_ORCHESTRA	= "GENERATE_ORCHESTRA";
	public static final String	START				= "START";
	public static final String	STOP				= "STOP";
	
	public static void main(String[] args) {
		String err = "";

		String action = "";
		
		String generateClassName = "";
		String generateDirectory = "";

		String positionName = Orchestra.CONDUCTOR;
		int positionBackupNumber = 0;
		
		if (args!=null && args.length>=2) {
			action = args[0];
			if (action.equals(GENERATE_ORCHESTRA)) {
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
		
		if (action.equals(GENERATE_ORCHESTRA)) {
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
				System.out.println("Generating orchestra to directory: " + genDir.getAbsolutePath() + " ...");
				err = generate(orch,genDir);
			}
		}
		
		if (err.length()>0) {
			System.err.println(err);
			System.exit(1);
		}
	}
	
	public static String generate(Orchestra orch,File dir) {
		String err = "";
		File orchDir = new File(dir.getAbsolutePath() + "/orchestra");
		if (orchDir.exists()) {
			emptyDir(orchDir);
		} else {
			orchDir.mkdir();
		}
		return err;
	}
	
	private static boolean emptyDir(File dir) {
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
					delete = emptyDir(f);
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
	
	public static void generateMemberDirs(File orchDir,Orchestra orch) {
		for (OrchestraMember member: orch.getMembers()) {
			File positionDir = new File(orchDir + "/" + member.getPosition().getName());
			File numberDir = new File(positionDir + "/" + member.getPositionBackupNumber());
			positionDir.mkdir();
			numberDir.mkdir();
		}
	}

	public static void generateMemberStartScript(File orchDir,OrchestraMember member) {
		File memberDir = new File(orchDir + "/" + member.getPosition().getName() + "/" + member.getPositionBackupNumber());
	}
}
