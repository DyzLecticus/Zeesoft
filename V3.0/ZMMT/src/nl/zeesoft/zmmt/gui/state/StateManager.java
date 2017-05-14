package nl.zeesoft.zmmt.gui.state;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.Settings;
import nl.zeesoft.zmmt.syntesizer.Instrument;

public class StateManager extends StateObject {
	private List<StateChangeSubscriber>			subscribers				= new ArrayList<StateChangeSubscriber>();
	
	private List<CompositionChangePublisher>	waitingPublishers		= new ArrayList<CompositionChangePublisher>();
	private CompositionChangePublishWorker 		publishWorker			= null;
	
	public StateManager(Messenger msgr,WorkerUnion union) {
		super(msgr);
		publishWorker = new CompositionChangePublishWorker(msgr,union,this);
		super.setSelectedTab(FrameMain.COMPOSITION);
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
				PatternState ps = new PatternState();
				ps.fromPatternList(super.getComposition().getPatterns());
				super.getPatternStates().clear();
				super.getPatternStates().add(ps);
				super.setPatternState(0);
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
			PatternState ps = new PatternState();
			ps.fromPatternList(super.getComposition().getPatterns());
			super.getPatternStates().clear();
			super.getPatternStates().add(ps);
			super.setPatternState(0);
			publishStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
		}
		unlockMe(this);
	}

	public void changedPattern(Object source,Pattern pattern) {
		lockMe(this);
		int currentPatternState = super.getPatternState();
		if (currentPatternState>=0) {
			List<PatternState> patternStates = new ArrayList<PatternState>(super.getPatternStates());
			int i = 0;
			for (PatternState ps: patternStates) {
				if (i>currentPatternState) {
					super.getPatternStates().remove(ps);
				}
				i++;
			}
		}
		Pattern existing = super.getComposition().getPattern(pattern.getNumber());
		if (existing!=null) {
			super.getComposition().getPatterns().remove(existing);
		}
		super.getComposition().getPatterns().add(0,pattern.copy());
		super.setCompositionChanged(true);
		PatternState ps = new PatternState();
		ps.fromPatternList(super.getComposition().getPatterns());
		super.getPatternStates().add(ps);
		super.setPatternState(super.getPatternStates().size() - 1);
		publishStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
		unlockMe(this);
	}

	public void undoPatternChange(Object source) {
		lockMe(this);
		int currentPatternState = super.getPatternState();
		if (currentPatternState>0) {
			List<Pattern> patterns = super.getPatternStates().get((currentPatternState - 1)).getPatterns();
			super.setPatternState((currentPatternState - 1));
			super.getComposition().getPatterns().clear();
			for (Pattern pattern: patterns) {
				super.getComposition().getPatterns().add(pattern);
			}
			super.setCompositionChanged(true);
			super.setSelectedPattern(super.getComposition().getPatterns().get(0).getNumber());
			publishStateChangeEvent(StateChangeEvent.CHANGED_COMPOSITION,source);
		}
		unlockMe(this);
	}

	public void redoPatternChange(Object source) {
		lockMe(this);
		int currentPatternState = super.getPatternState();
		if (currentPatternState<(super.getPatternStates().size() - 1)) {
			List<Pattern> patterns = super.getPatternStates().get((currentPatternState + 1)).getPatterns();
			super.setPatternState((currentPatternState + 1));
			super.getComposition().getPatterns().clear();
			for (Pattern pattern: patterns) {
				super.getComposition().getPatterns().add(pattern);
			}
			super.setCompositionChanged(true);
			super.setSelectedPattern(super.getComposition().getPatterns().get(0).getNumber());
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
		evt.setPatternState(getPatternState());
		for (PatternState ps: getPatternStates()) {
			evt.getPatternStates().add(ps);
		}
		return evt;
	}
	
	private void publishStateChangeEvent(String type, Object source) {
		StateChangeEvent evt = getNewStateChangeEvent(type,source);
		for (StateChangeSubscriber sub: subscribers) {
			sub.handleStateChange(evt);
		}
	}
}
