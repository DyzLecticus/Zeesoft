package nl.zeesoft.zsd.sequence.dutch;

import nl.zeesoft.zsd.sequence.SequencePreprocessor;

public class DutchPreprocessor extends SequencePreprocessor {
	@Override
	public void initialize() {
		addReplacement("smorgensvroeg","smorgens vroeg");
		addReplacement("savondslaat","savonds laat");
		addReplacement("morgensvroeg","smorgens vroeg");
		addReplacement("avondslaat","savonds laat");
		addReplacement("'s ochtends","sochteds");
		addReplacement("'s middags","smiddags");
		addReplacement("'s avonds","savonds");
	}
}
