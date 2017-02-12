package nl.zeesoft.zidm.dialog.pattern;

import java.util.List;

import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;

public class ModelPatternManager extends PatternManager {
	@Override
	protected void addCustomPatterns(List<PatternObject> patterns) {
		patterns.add(0,new IndefiniteArticleEnglish(getMessenger()));
		super.addCustomPatterns(patterns);
	}
}
