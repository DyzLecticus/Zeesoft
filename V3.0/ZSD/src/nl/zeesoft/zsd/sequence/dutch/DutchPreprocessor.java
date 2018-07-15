package nl.zeesoft.zsd.sequence.dutch;

import nl.zeesoft.zsd.sequence.SequencePreprocessor;

public class DutchPreprocessor extends SequencePreprocessor {
	@Override
	public void initialize() {
		addReplacement("smorgensvroeg","morgens vroeg");
		addReplacement("savondslaat","avonds laat");
		addReplacement("morgensvroeg","morgens vroeg");
		addReplacement("avondslaat","avonds laat");
		addReplacement("'s ochtends","sochteds");
		addReplacement("'s middags","smiddags");
		addReplacement("'s avonds","savonds");
	}
}
