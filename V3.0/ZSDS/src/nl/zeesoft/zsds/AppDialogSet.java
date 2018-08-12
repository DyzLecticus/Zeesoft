package nl.zeesoft.zsds;

import java.util.List;

import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsds.dialogs.dutch.DutchStateAccuracy;
import nl.zeesoft.zsds.dialogs.english.EnglishStateAccuracy;

public class AppDialogSet extends DialogSet {
	@Override
	protected List<DialogInstance> getDefaultDialogs() {
		List<DialogInstance> r = super.getDefaultDialogs();
		r.add(new EnglishStateAccuracy());
		r.add(new DutchStateAccuracy());
		return r;
	}
}
