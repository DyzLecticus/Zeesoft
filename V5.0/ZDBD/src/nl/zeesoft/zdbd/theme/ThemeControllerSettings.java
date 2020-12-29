package nl.zeesoft.zdbd.theme;

import nl.zeesoft.zdk.FileIO;

public class ThemeControllerSettings extends Settings {
	public String		soundBankDir				= "resources/";
	public boolean		useInternalDrumKit			= true;
	public boolean		useInternalSyntesizers		= true;
	
	public String		workingTheme				= "";
	
	@Override
	public void copyFrom(Settings set) {
		super.copyFrom(set);
		ThemeControllerSettings tcs = (ThemeControllerSettings) set;
		this.soundBankDir = tcs.soundBankDir;
		this.useInternalDrumKit = tcs.useInternalDrumKit;
		this.useInternalSyntesizers = tcs.useInternalSyntesizers;
		this.workingTheme = tcs.workingTheme;
	}
	
	@Override
	public String getFileName() {
		return getWorkDir() + "ThemeControllerSettings.txt";
	}
	
	public String getThemeDir() {
		return FileIO.addSlash(getWorkDir() + "Themes");
	}
}
