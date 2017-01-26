package nl.zeesoft.zspr.pattern.patterns;

import java.util.List;

import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;

public class UniversalAlphabetic extends PatternObject {
	private List<String> knownSymbols = null;
	
	public UniversalAlphabetic() {
		super(TYPE_ALPHABETIC,SPECIFIER_UNIVERSAL);
	}
	
	@Override
	public String transformStringToValue(String str) {
		return str.replaceAll("\\|","");
	}
	
	@Override
	public String transformValueToString(String str) {
		return str;
	}

	@Override
	protected boolean stringMatchesPatternNoLock(String str) {
		boolean r = false;
		if (isAlphabetic(str) && (knownSymbols==null || knownSymbols.contains(str))) {
			r = true;
		}
		return r;
	}

	@Override
	public void initializePatternStrings(PatternManager manager) {
		// Empty
	}

	public void setKnownSymbols(List<String> knownSymbols) {
		lockMe(this);
		this.knownSymbols = knownSymbols;
		unlockMe(this);
	}

	public void addKnownSymbols(List<String> knownSymbols) {
		lockMe(this);
		if (knownSymbols==null) {
			this.knownSymbols = knownSymbols;
		} else {
			for (String symbol: knownSymbols) {
				if (!this.knownSymbols.contains(symbol)) {
					this.knownSymbols.add(symbol);
				}
			}
		}
		unlockMe(this);
	}

	public void clearKnownSymbols() {
		lockMe(this);
		knownSymbols.clear();
		unlockMe(this);
	}
}
