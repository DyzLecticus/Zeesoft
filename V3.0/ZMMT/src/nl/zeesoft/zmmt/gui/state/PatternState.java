package nl.zeesoft.zmmt.gui.state;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zmmt.composition.Pattern;

public class PatternState {
	private List<Pattern>	patterns	= new ArrayList<Pattern>();

	public List<Pattern> getPatterns() {
		return patterns;
	}
	
	public void fromPatternList(List<Pattern> list) {
		patterns.clear();
		for (Pattern p: list) {
			patterns.add(p.copy());
		}
	}
}
