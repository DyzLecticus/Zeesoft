package nl.zeesoft.zeetracker.gui.state;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zeetracker.gui.Settings;
import nl.zeesoft.zmmt.composition.Composition;

public abstract class StateObject extends Locker {
	private String 					selectedTab				= "";
	private String 					selectedInstrument		= "";
	private boolean					showInstrumentFX		= false;
	private String 					patternEditMode			= "";
	private int 					selectedPattern			= 0;
	private int 					selectedPatternRowFrom	= -1;
	private int 					selectedPatternRowTo	= -1;
	private int 					selectedPatternColFrom	= -1;
	private int 					selectedPatternColTo	= -1;
	private int 					selectedSequenceRowFrom	= -1;
	private int 					selectedSequenceRowTo	= -1;
	
	private boolean 				compositionChanged		= false;
	
	private Settings				settings				= null;
	private Composition				composition				= null;
	
	private List<StateChangeEvent>	states					= new ArrayList<StateChangeEvent>();
	private int						state					= 0;
	
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

	public boolean isShowInstrumentFX() {
		return showInstrumentFX;
	}

	protected void setShowInstrumentFX(boolean showInstrumentFX) {
		this.showInstrumentFX = showInstrumentFX;
	}

	public String getPatternEditMode() {
		return patternEditMode;
	}

	protected void setPatternEditMode(String patternEditMode) {
		this.patternEditMode = patternEditMode;
	}

	public int getSelectedPattern() {
		return selectedPattern;
	}

	protected void setSelectedPattern(int selectedPattern) {
		this.selectedPattern = selectedPattern;
	}

	public int getSelectedPatternRowFrom() {
		return selectedPatternRowFrom;
	}

	protected void setSelectedPatternRowFrom(int selectedPatternRowFrom) {
		this.selectedPatternRowFrom = selectedPatternRowFrom;
	}

	public int getSelectedPatternRowTo() {
		return selectedPatternRowTo;
	}

	protected void setSelectedPatternRowTo(int selectedPatternRowTo) {
		this.selectedPatternRowTo = selectedPatternRowTo;
	}

	public int getSelectedPatternColFrom() {
		return selectedPatternColFrom;
	}

	protected void setSelectedPatternColFrom(int selectedPatternColFrom) {
		this.selectedPatternColFrom = selectedPatternColFrom;
	}

	public int getSelectedPatternColTo() {
		return selectedPatternColTo;
	}

	protected void setSelectedPatternColTo(int selectedPatternColTo) {
		this.selectedPatternColTo = selectedPatternColTo;
	}

	public int getSelectedSequenceRowFrom() {
		return selectedSequenceRowFrom;
	}

	protected void setSelectedSequenceRowFrom(int selectedSequenceRowFrom) {
		this.selectedSequenceRowFrom = selectedSequenceRowFrom;
	}

	public int getSelectedSequenceRowTo() {
		return selectedSequenceRowTo;
	}

	protected void setSelectedSequenceRowTo(int selectedSequenceRowTo) {
		this.selectedSequenceRowTo = selectedSequenceRowTo;
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

	public List<StateChangeEvent> getStates() {
		return states;
	}

	public int getState() {
		return state;
	}

	protected void setState(int state) {
		this.state = state;
	}
}
