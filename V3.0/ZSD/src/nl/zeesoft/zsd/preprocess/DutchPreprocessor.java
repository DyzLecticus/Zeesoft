package nl.zeesoft.zsd.preprocess;

public class DutchPreprocessor extends PreprocessorInstance {
	@Override
	public void initialize() {
		addReplacement("'s ochtends","sochtends");
		addReplacement("'s morgens","smorgens");
		addReplacement("'s middags","smiddags");
		addReplacement("'s avonds","savonds");
		addReplacement("ochtendsvroeg","ochtends vroeg");
		addReplacement("morgensvroeg","morgens vroeg");
		addReplacement("avondslaat","avonds laat");
	}
}
