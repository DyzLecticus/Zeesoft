package nl.zeesoft.zdbd;

import nl.zeesoft.zdk.midi.State;

public class Settings {
	public String						workDir						= "ZIPD/";
	
	public String						soundBankDir				= "resources/";
	public boolean						useInternalDrumKit			= true;
	public boolean						useInternalSyntesizers		= true;
	
	public State						midiState					= new State();
	
	public Settings() {
		initialize();
	}
	
	private void initialize() {
		String dir = System.getProperty("user.home");
		if (dir.length()>0) {
			workDir = dir + "/" + workDir;
		}
	}
}
