package nl.zeesoft.zeetracker.gui.state;

import nl.zeesoft.zmmt.composition.Composition;

public interface CompositionChangePublisher {
	public void setChangesInComposition(Composition composition);
}
