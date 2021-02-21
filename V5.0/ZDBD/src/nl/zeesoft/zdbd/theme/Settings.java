package nl.zeesoft.zdbd.theme;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.collection.PersistableCollection;

public abstract class Settings {
	public String		workDir						= "MidiDreamer/";
	
	public Settings() {
		initialize();
	}

	public Settings copy() {
		Settings r = (Settings) Instantiator.getNewClassInstance(getClass());
		r.copyFrom(this);
		return r;
	}
	
	public void copyFrom(Settings set) {
		this.workDir = set.workDir;
	}
	
	public String getWorkDir() {
		return FileIO.addSlash(workDir);
	}
	
	public abstract String getFileName();
	
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
