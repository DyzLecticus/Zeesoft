package nl.zeesoft.zdk.confabulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.SymbolParser;

/**
 * Training confabulator.
 * 
 * Intended for thread safe confabulator training purposes.
 */
public class ConfabulatorTrainer extends ConfabulatorObject {
	private boolean deriveLinkData = false;

	/**
	 * Learns confabulator link data.
	 * 
	 * @param sequence The sequence to learn
	 * @return True if learning the sequence has caused a division of all link counts (and possible removal of links)
	 */
	public boolean learnSequence(String sequence) {
		return learnSequence(sequence,"");
	}

	/**
	 * Learns confabulator link data.
	 * 
	 * @param sequence The sequence to learn
	 * @param context The optional sequence context to learn
	 * @return True if learning the sequence has caused a division of all link counts (and possible removal of links)
	 */
	public boolean learnSequence(String sequence, String context) {
		return learnSequence(new StringBuilder(sequence),new StringBuilder(context));
	}

	/**
	 * Learns confabulator link data.
	 * 
	 * @param sequence The sequence to learn
	 * @return True if learning the sequence has caused a division of all link counts (and possible removal of links)
	 */
	public boolean learnSequence(StringBuilder sequence) {
		return learnSequence(sequence,null);
	}
	
	/**
	 * Learns confabulator link data.
	 * 
	 * @param sequence The sequence to learn
	 * @param context The optional sequence context to learn
	 * @return True if learning the sequence has caused a division of all link counts (and possible removal of links)
	 */
	public boolean learnSequence(StringBuilder sequence, StringBuilder context) {
		if (context==null) {
			context = new StringBuilder();
		}
		boolean divide = false;
		List<String> contextSymbols = SymbolParser.parseSymbolsFromText(context);
		List<String> sequenceSymbols = SymbolParser.parseSymbolsFromText(sequence);
		if (sequenceSymbols.size()>1) {
			lockMe(this);
			if (contextSymbols.size()==0) {
				contextSymbols.add("");
			}
			int i = 0;
			for (String symbol: sequenceSymbols) {
				String symbolTo = "";
				for (int distance = 1; distance<=getMaxLinkDistanceNoLock(); distance++) {
					int index = i + distance;
					if (index>=sequenceSymbols.size()) {
						break;
					} else {
						symbolTo = sequenceSymbols.get(index);
					}
					for (String contextSymbol: contextSymbols) {
						Link lnk = getLinkNoLock(symbol,contextSymbol,distance,symbolTo);
						if (lnk!=null) {
							lnk.setCount((lnk.getCount() + 2));
							if (lnk.getCount()>=getMaxLinkCountNoLock()) {
								divide = true;
							}
						} else {
							lnk = new Link();
							lnk.setSymbolFrom(symbol);
							lnk.setSymbolContext(contextSymbol);
							lnk.setDistance(distance);
							lnk.setSymbolTo(symbolTo);
							lnk.setCount(2);
							getLinksNoLock().add(lnk);
						}
					}
				}
				i++;
			}
			setDeriveLinkDataNoLock(true);
			unlockMe(this);
		}
		if (divide) {
			lockMe(this);
			List<Link> links = new ArrayList<Link>(getLinksNoLock());	
			for (Link lnk: links) {
				if (lnk.getCount()==1) {
					lnk.setCount(0);
					getLinksNoLock().remove(lnk);
				} else {
					lnk.setCount((lnk.getCount() / 2));
				}
			}
			unlockMe(this);
		}
		return divide;
	}

	private Link getLinkNoLock(String symbolFrom,String symbolContext,int distance,String symbolTo) {
		Link r = null;
		for (Link lnk: getLinksNoLock()) {
			if (lnk.getSymbolFrom().equals(symbolFrom) && lnk.getSymbolContext().equals(symbolContext) && lnk.getDistance()==distance && lnk.getSymbolTo().equals(symbolTo)) {
				r = lnk;
				break;
			}
		}
		return r;
	}
	
	protected final boolean getDeriveLinkDataNoLock() {
		return deriveLinkData;
	}

	protected final void setDeriveLinkDataNoLock(boolean deriveLinkData) {
		this.deriveLinkData = deriveLinkData;
	}
}
