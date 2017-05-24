package nl.zeesoft.zmmt.gui.state;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.Settings;
import nl.zeesoft.zmmt.synthesizer.Instrument;

public class StateManager extends StateObject {
	private List<StateChangeSubscriber>			subscribers				= new ArrayList<StateChangeSubscriber>();
	
	private List<CompositionChangePublisher>	waitingPublishers		= new ArrayList<CompositionChangePublisher>();
	private CompositionChangePublishWorker 		publishWorker			= null;
	
	public StateManager(Messenger msgr,WorkerUnion union) {
		super(msgr);
		publishWorker = new CompositionChangePublishWorker(msgr,union,this);
		super.setSelectedTab(FrameMain.TAB_COMPOSITION);
		super.setSelectedInstrument(Instrument.LEAD);
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
	
	public void setSelectedPattern(Object source,int pattern) {
		lockMe(this);
		if (super.getSelectedPattern()!=pattern) {
			super.setSelectedPattern(pattern);
			publishStateChangeEvent(StateChangeEvent.SELECTED_PATTERN,source);
		}
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
			publishStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
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
		
		publishStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
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
			super.setSelectedTab(state.getSelectedTab());
			super.setSelectedInstrument(state.getSelectedInstrument());
			super.setSelectedPattern(state.getSelectedPattern());
			
			publishStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
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
			
			publishStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
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
		unlockMe(this);
	}
	
	private StateChangeEvent getNewStateChangeEvent(String type, Object source) {
		StateChangeEvent evt = new StateChangeEvent(type,source);
		evt.setSelectedTab(super.getSelectedTab());
		evt.setSelectedInstrument(super.getSelectedInstrument());
		evt.setSelectedPattern(super.getSelectedPattern());
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
