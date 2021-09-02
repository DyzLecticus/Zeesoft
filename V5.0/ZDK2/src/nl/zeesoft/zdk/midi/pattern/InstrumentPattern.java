package nl.zeesoft.zdk.midi.pattern;

import java.util.ArrayList;
import java.util.List;

public class InstrumentPattern {
	public String			name			= "";
	public ChordPattern		chordPattern	= null;
	public List<Pattern>	patterns		= new ArrayList<Pattern>();
}
