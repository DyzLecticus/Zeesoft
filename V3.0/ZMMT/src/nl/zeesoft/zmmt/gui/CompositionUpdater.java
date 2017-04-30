package nl.zeesoft.zmmt.gui;

import nl.zeesoft.zmmt.composition.Composition;

public interface CompositionUpdater {
	public void updatedComposition(String tab,Composition comp);
	public void getCompositionUpdate(String tab,Composition comp);
}
