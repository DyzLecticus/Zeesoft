package nl.zeesoft.zeetracker.gui.state;

public interface StateChangeSubscriber {
	public void handleStateChange(StateChangeEvent evt);
}
