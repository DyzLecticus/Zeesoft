package nl.zeesoft.zmmt.gui.state;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.Settings;
import nl.zeesoft.zmmt.gui.panel.PanelPatterns;
import nl.zeesoft.zmmt.synthesizer.Instrument;

public class StateManager extends StateObject {
	private List<StateChangeSubscriber>			subscribers						= new ArrayList<StateChangeSubscriber>();
	
	private List<CompositionChangePublisher>	waitingPublishers				= new ArrayList<CompositionChangePublisher>();
	private CompositionChangePublishWorker 		publishWorker					= null;
	
	private StateChangeEvent					waitingCompositionChangeEvent	= null;
	
	public StateManager(Messenger msgr,WorkerUnion union,Settings settings) {
		super(msgr);
		publishWorker = new CompositionChangePublishWorker(msgr,union,this);
		super.setSettings(settings);
		super.setSelectedTab(FrameMain.TAB_COMPOSITION);
		super.setSelectedInstrument(Instrument.LEAD);
		super.setPatternEditMode(PanelPatterns.EDIT_NOTES);
	}
	
	public void start() {
		lockMe(this);
		waitingPublishers.clear();
		publishWorker.start();
		unlockMe(this);
	}
	
	public void stop() {
		lockMe(this);
		publishWorker.stop();
		unlockMe(this);
	}
	
	public void addSubscriber(StateChangeSubscriber subscriber) {
		lockMe(this);
		if (!subscribers.contains(subscriber)) {
			subscribers.add(subscriber);
		}
		unlockMe(this);
	}

	public void setSelectedTab(Object source,String tab) {
		lockMe(this);
		if (!super.getSelectedTab().equals(tab)) {
			super.setSelectedTab(tab);
			publishStateChangeEvent(StateChangeEvent.SELECTED_TAB,source);
		}
		unlockMe(this);
	}
	
	public void setSelectedInstrument(Object source,String instrument) {
		lockMe(this);
		if (!super.getSelectedInstrument().equals(instrument)) {
			super.setSelectedInstrument(instrument);
			publishStateChangeEvent(StateChangeEvent.SELECTED_INSTRUMENT,source);
		}
		unlockMe(this);
	}

	public void setPatternEditMode(Object source,String editMode) {
		lockMe(this);
		if (!super.getPatternEditMode().equals(editMode)) {
			super.setPatternEditMode(editMode);
			publishStateChangeEvent(StateChangeEvent.CHANGED_PATTERN_EDIT_MODE,source);
		}
		unlockMe(this);
	}

	public void setSelectedPattern(Object source,int pattern) {
		lockMe(this);
		if (super.getSelectedPattern()!=pattern) {
			super.setSelectedPattern(pattern);
			publishStateChangeEvent(StateChangeEvent.SELECTED_PATTERN,source);
		}
		unlockMe(this);
	}
	
	public void selectNextPattern(Object source) {
		lockMe(this);
		if (super.getSelectedPattern()<99) {
			super.setSelectedPattern((super.getSelectedPattern() + 1));
			publishStateChangeEvent(StateChangeEvent.SELECTED_PATTERN,source);
		}
		unlockMe(this);
	}
	
	public void selectPreviousPattern(Object source) {
		lockMe(this);
		if (super.getSelectedPattern()>0) {
			super.setSelectedPattern((super.getSelectedPattern() - 1));
			publishStateChangeEvent(StateChangeEvent.SELECTED_PATTERN,source);
		}
		unlockMe(this);
	}
	
	public void setSelectedPatternSelection(Object source,int rowFrom,int rowTo,int colFrom,int colTo) {
		lockMe(this);
		super.setSelectedPatternRowFrom(rowFrom);
		super.setSelectedPatternRowTo(rowTo);
		super.setSelectedPatternColFrom(colFrom);
		super.setSelectedPatternColTo(colTo);
		publishStateChangeEvent(StateChangeEvent.CHANGED_PATTERN_SELECTION,source);
		unlockMe(this);
	}
	
	public void setSelectedSequenceSelection(Object source,int rowFrom,int rowTo) {
		lockMe(this);
		super.setSelectedPatternRowFrom(rowFrom);
		super.setSelectedPatternRowTo(rowTo);
		publishStateChangeEvent(StateChangeEvent.CHANGED_SEQUENCE_SELECTION,source);
		unlockMe(this);
	}
	
	public void setCompositionChanged(Object source,boolean compositionChanged) {
		lockMe(this);
		if (super.isCompositionChanged()!=compositionChanged) {
			super.setCompositionChanged(compositionChanged);
			if (!compositionChanged) {
				super.getStates().clear();
				StateChangeEvent state = getNewStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
				state.setComposition(super.getComposition().copy());
				super.getStates().add(state);
				super.setState(0);
			}
			publishStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION_STATE,source);
		}
		unlockMe(this);
	}
	
	public void setSettings(Object source,Settings settings) {
		lockMe(this);
		if (super.getSettings()!=settings) {
			super.setSettings(settings);
			publishStateChangeEvent(StateChangeEvent.CHANGED_SETTINGS,source);
		}
		unlockMe(this);
	}

	public void setComposition(Object source,Composition composition) {
		lockMe(this);
		if (super.getComposition()!=composition) {
			super.setComposition(composition);
			super.getStates().clear();
			StateChangeEvent state = getNewStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
			state.setComposition(super.getComposition().copy());
			super.getStates().add(state);
			super.setState(0);
			waitingCompositionChangeEvent = getNewStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
		}
		unlockMe(this);
	}

	public void changedPattern(Object source,Pattern pattern) {
		lockMe(this);
		
		Pattern existing = super.getComposition().getPattern(pattern.getNumber());
		if (existing!=null) {
			super.getComposition().getPatterns().remove(existing);
		}
		super.getComposition().getPatterns().add(0,pattern.copy());
		super.setCompositionChanged(true);

		addState(source);
		
		waitingCompositionChangeEvent = getNewStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
		unlockMe(this);
	}

	public void changedSequence(Object source,List<Integer> sequence) {
		lockMe(this);
		
		super.getComposition().getSequence().clear();
		for (Integer num: sequence) {
			super.getComposition().getSequence().add(num);
		}
		super.setCompositionChanged(true);

		addState(source);
		
		waitingCompositionChangeEvent = getNewStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
		unlockMe(this);
	}


	public void undoCompositionChange(Object source) {
		lockMe(this);
		int currentState = super.getState();
		if (currentState>0) {
			StateChangeEvent state = super.getStates().get((currentState - 1));
			super.setState((currentState - 1));
			
			super.setComposition(state.getComposition().copy());
			super.setCompositionChanged(state.isCompositionChanged());
			if (state.isCompositionChanged()) {
				super.setSelectedTab(state.getSelectedTab());
				super.setSelectedInstrument(state.getSelectedInstrument());
				super.setSelectedPattern(state.getSelectedPattern());
			}
			
			waitingCompositionChangeEvent = getNewStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
		}
		unlockMe(this);
	}

	public void redoCompositionChange(Object source) {
		lockMe(this);
		int currentState = super.getState();
		if (currentState<(super.getStates().size() - 1)) {
			StateChangeEvent state = super.getStates().get((currentState + 1));
			super.setState((currentState + 1));
			
			super.setComposition(state.getComposition().copy());
			super.setCompositionChanged(state.isCompositionChanged());
			super.setSelectedTab(state.getSelectedTab());
			super.setSelectedInstrument(state.getSelectedInstrument());
			super.setSelectedPattern(state.getSelectedPattern());
			
			waitingCompositionChangeEvent = getNewStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
		}
		unlockMe(this);
	}

	public void addWaitingPublisher(CompositionChangePublisher publisher) {
		lockMe(this);
		if (!waitingPublishers.contains(publisher)) {
			waitingPublishers.add(publisher);
		}
		unlockMe(this);
	}

	@Override
	public String getSelectedTab() {
		String r = "";
		lockMe(this);
		r = super.getSelectedTab();
		unlockMe(this);
		return r;
	}

	@Override
	public String getSelectedInstrument() {
		String r = "";
		lockMe(this);
		r = super.getSelectedInstrument();
		unlockMe(this);
		return r;
	}

	@Override
	public String getPatternEditMode() {
		String r = "";
		lockMe(this);
		r = super.getPatternEditMode();
		unlockMe(this);
		return r;
	}

	@Override
	public int getSelectedPattern() {
		int r = 0;
		lockMe(this);
		r = super.getSelectedPattern();
		unlockMe(this);
		return r;
	}
	
	@Override
	public boolean isCompositionChanged() {
		boolean r = false;
		lockMe(this);
		r = super.isCompositionChanged();
		unlockMe(this);
		return r;
	}

	@Override
	public Settings getSettings() {
		Settings r = null;
		lockMe(this);
		r = super.getSettings();
		unlockMe(this);
		return r;
	}

	@Override
	public Composition getComposition() {
		Composition r = null;
		lockMe(this);
		r = super.getComposition();
		unlockMe(this);
		return r;
	}

	protected void publishChanges() {
		lockMe(this);
		if (waitingPublishers.size() > 0) {
			for (CompositionChangePublisher publisher: waitingPublishers) {
				publisher.setChangesInComposition(super.getComposition());
				super.setCompositionChanged(true);
				addState(publisher);
				publishStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,publisher);
			}
			waitingPublishers.clear();
		}
		if (waitingCompositionChangeEvent!=null) {
			for (StateChangeSubscriber sub: subscribers) {
				sub.handleStateChange(waitingCompositionChangeEvent);
			}
			waitingCompositionChangeEvent = null;
		}
		unlockMe(this);
	}
	
	private StateChangeEvent getNewStateChangeEvent(String type, Object source) {
		StateChangeEvent evt = new StateChangeEvent(type,source);
		evt.setSelectedTab(super.getSelectedTab());
		evt.setSelectedInstrument(super.getSelectedInstrument());
		evt.setPatternEditMode(super.getPatternEditMode());
		evt.setSelectedPattern(super.getSelectedPattern());
		evt.setSelectedPatternRowFrom(super.getSelectedPatternRowFrom());
		evt.setSelectedPatternRowTo(super.getSelectedPatternRowTo());
		evt.setSelectedPatternColFrom(super.getSelectedPatternColFrom());
		evt.setSelectedPatternColTo(super.getSelectedPatternColTo());
		evt.setSelectedSequenceRowFrom(super.getSelectedSequenceRowFrom());
		evt.setSelectedSequenceRowTo(super.getSelectedSequenceRowTo());
		evt.setCompositionChanged(super.isCompositionChanged());
		evt.setComposition(super.getComposition());
		evt.setSettings(super.getSettings());
		evt.setState(getState());
		for (StateChangeEvent sce: getStates()) {
			evt.getStates().add(sce);
		}
		return evt;
	}
	
	private void publishStateChangeEvent(String type, Object source) {
		StateChangeEvent evt = getNewStateChangeEvent(type,source);
		for (StateChangeSubscriber sub: subscribers) {
			sub.handleStateChange(evt);
		}
	}
	
	private void addState(Object source) {
		int currentState = super.getState();
		if (currentState>=0) {
			List<StateChangeEvent> states = new ArrayList<StateChangeEvent>(super.getStates());
			int i = 0;
			for (StateChangeEvent sce: states) {
				if (i>currentState) {
					super.getStates().remove(sce);
				}
				i++;
			}
		}
		StateChangeEvent state = getNewStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
		state.setComposition(super.getComposition().copy());
		super.getStates().add(state);
		super.setState(super.getStates().size() - 1);
	}
}
