package nl.zeesoft.zmmt.gui.state;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.Settings;

public abstract class StateObject extends Locker {
	private String 				selectedTab				= "";
	private String 				selectedInstrument		= "";
	private boolean 			compositionChanged		= false;
	
	private Settings			settings				= null;
	private Composition			composition				= null;
	
	public StateObject(Messenger msgr) {
		super(msgr);
	}

	public String getSelectedTab() {
		return selectedTab;
	}
	
	protected void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}
	
	public String getSelectedInstrument() {
		return selectedInstrument;
	}
	
	protected void setSelectedInstrument(String selectedInstrument) {
		this.selectedInstrument = selectedInstrument;
	}
	
	public boolean isCompositionChanged() {
		return compositionChanged;
	}
	
	protected void setCompositionChanged(boolean compositionChanged) {
		this.compositionChanged = compositionChanged;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	protected void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	public Composition getComposition() {
		return composition;
	}
	
	protected void setComposition(Composition composition) {
		this.composition = composition;
	}
}
