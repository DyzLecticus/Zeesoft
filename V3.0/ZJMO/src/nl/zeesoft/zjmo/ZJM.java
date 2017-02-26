package nl.zeesoft.zjmo;

import java.io.File;

import nl.zeesoft.zjmo.orchestra.Orchestra;

/**
 * Entry point for local Zeesoft JSON Machine control and generation.
 */
public class ZJM {
	public static final String	GENERATE_ORCHESTRA	= "GENERATE_ORCHESTRA";
	public static final String	START_POSITION		= "START_POSITION";
	public static final String	STOP_POSITION		= "STOP_POSITION";
	public static void main(String[] args) {
		String action = "";
		
		String generateClassName = "";
		String generateDirectory = "";

		String position = Orchestra.CONDUCTOR;
		int positionBackupNumber = 0;
		
		if (args!=null && args.length>=2) {
			action = args[0];
			if (action.equals(GENERATE_ORCHESTRA)) {
				generateClassName = args[1];
				if (args.length>=3) {
					generateDirectory = args[2];
				}
			} else if (
				action.equals(START_POSITION) || 
				action.equals(STOP_POSITION)
				) {
				generateDirectory = args[1];
			}
		}
		
		if (action.equals(GENERATE_ORCHESTRA)) {
			String err = "";
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
				if (!genDir.exists() || !genDir.isDirectory() || !genDir.canWrite()) {
					err = "Unable to generate orchestra to directory: " + generateDirectory;
				}
			}
			if (err.length()>0) {
				System.err.println(err);
				System.exit(1);
			} else {
				System.out.println("Generating orchestra to directory: " + genDir.getAbsolutePath() + " ...");
			}
		}
	}
}
