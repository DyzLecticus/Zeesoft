package nl.zeesoft.zspr.pattern.patterns;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;

public class UniversalAlphabetic extends PatternObject {
	private List<String> knownSymbols = null;
	
	public UniversalAlphabetic(Messenger msgr) {
		super(msgr,TYPE_ALPHABETIC,SPECIFIER_UNIVERSAL);
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
		if (isAlphabetic(str) && (knownSymbols==null || knownSymbols.size()==0 || !knownSymbols.contains(str))) {
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
		this.knownSymbols = new ArrayList<String>();
		for (String symbol: knownSymbols) {
			symbol = symbol.toLowerCase();
			if (!this.knownSymbols.contains(symbol)) {
				this.knownSymbols.add(symbol);
			}
		}
		unlockMe(this);
	}

	public void addKnownSymbols(List<String> knownSymbols) {
		lockMe(this);
		if (knownSymbols==null) {
			this.knownSymbols = knownSymbols;
		} else {
			for (String symbol: knownSymbols) {
				symbol = symbol.toLowerCase();
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
