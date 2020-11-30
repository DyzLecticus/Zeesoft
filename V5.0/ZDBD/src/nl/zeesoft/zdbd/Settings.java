package nl.zeesoft.zdbd;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.collection.PersistableCollection;

public class Settings {
	public String		workDir						= "ZDBD/";
	
	public String		soundBankDir				= "resources/";
	public boolean		useInternalDrumKit			= true;
	public boolean		useInternalSyntesizers		= true;
	
	public String		workingComposition			= "";
	
	public Settings() {
		initialize();
	}

	public String getFileName() {
		return FileIO.addSlash(workDir) + "Settings.txt";
	}
	
	public boolean fileExists() {
		return FileIO.checkFile(getFileName()).length() == 0;
	}
	
	public Settings fromFile() {
		return (Settings) PersistableCollection.fromFile(getFileName());
	}
	
	public void toFile() {
		if (FileIO.checkDirectory(workDir).length()>0) {
			FileIO.mkDirs(workDir);
		}
		PersistableCollection.toFile(this, getFileName());
	}
	
	private void initialize() {
		String dir = System.getProperty("user.home");
		if (dir.length()>0) {
			workDir = FileIO.addSlash(dir) + workDir;
		}
	}
}
