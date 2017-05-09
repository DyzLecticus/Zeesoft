package nl.zeesoft.zmmt.gui.state;

import nl.zeesoft.zmmt.composition.Composition;

public interface CompositionChangeSubscriber {
	public void changedComposition(Object source,Composition composition);
}
