package nl.zeesoft.zipd;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.midi.State;

public class Settings {
	public Logger						logger						= new Logger();
	
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
