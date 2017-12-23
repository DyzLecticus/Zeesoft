package nl.zeesoft.zsc.confabulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;

/**
 * Training confabulator.
 * 
 * Intended for thread safe confabulator training purposes.
 */
public class ConfabulatorTrainer extends ConfabulatorObject {
	private boolean deriveLinkData = false;

	public ConfabulatorTrainer() {
		super(null);
	}

	public ConfabulatorTrainer(Messenger msgr) {
		super(msgr);
	}

	/**
	 * Learns confabulator link data.
	 * 
	 * @param sequence The sequence to learn
	 * @return True if learning the sequence has caused a division of all link counts (and possible removal of links)
	 */
	public boolean learnSequence(ZStringSymbolParser sequence) {
		return learnSequence(sequence,null);
	}

	/**
	 * Learns confabulator link data.
	 * 
	 * @param sequence The sequence to learn
	 * @return True if learning the sequence has caused a division of all link counts (and possible removal of links)
	 */
	public boolean learnSequence(String sequence) {
		return learnSequence(new ZStringSymbolParser(sequence),null);
	}

	/**
	 * Learns confabulator link data.
	 * 
	 * @param sequence The sequence to learn
	 * @param context The optional sequence context to learn
	 * @return True if learning the sequence has caused a division of all link counts (and possible removal of links)
	 */
	public boolean learnSequence(String sequence, String context) {
		return learnSequence(new ZStringSymbolParser(sequence),new ZStringSymbolParser(context));
	}
	
	/**
	 * Learns confabulator link data.
	 * 
	 * @param sequence The sequence to learn
	 * @param context The optional sequence context to learn
	 * @return True if learning the sequence has caused a division of all link counts (and possible removal of links)
	 */
	public boolean learnSequence(ZStringSymbolParser sequence, ZStringSymbolParser context) {
		if (context==null) {
			context = new ZStringSymbolParser();
		}
		boolean divide = false;
		List<String> contextSymbols = context.toSymbols();
		List<String> sequenceSymbols = sequence.toSymbols();
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
							addLinkNoLock(lnk);
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
					removeLinkNoLock(lnk);
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
		List<Link> lnks = getLinksFromDistanceNoLock(symbolFrom,distance);
		if (lnks!=null) {
			for (Link lnk: lnks) {
				if (lnk.getSymbolContext().equals(symbolContext) && lnk.getSymbolTo().equals(symbolTo)) {
					r = lnk;
					break;
				}
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
