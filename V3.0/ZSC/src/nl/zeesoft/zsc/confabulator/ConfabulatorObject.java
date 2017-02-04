package nl.zeesoft.zsc.confabulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

/**
 * Confabulator object.
 * 
 * Can be used for thread safe initialization of other confabulators.
 */
public class ConfabulatorObject extends Locker {
	private int					maxLinkDistance		= 8;
	private int					maxLinkCount		= 1000;
	private List<Link>			links				= new ArrayList<Link>();
	
	protected ConfabulatorObject(Messenger msgr) {
		super(msgr);
	}
	
	/**
	 * Initializes the confabulator for usage.
	 * 
	 * Please note that calling this method will clear the link data.
	 * 
	 * @param maxLinkDistance The maximum symbol sequence distance used to limit learning
	 * @param maxLinkCount The maximum link count used to learn and forget links
	 * @param links The optional link data set
	 */
	public void initialize(int maxLinkDistance,int maxLinkCount,List<Link> links) {
		lockMe(this);
		if (maxLinkDistance<2) {
			maxLinkDistance=2;
		} else if (maxLinkDistance>32) {
			maxLinkDistance=32;
		}
		if (maxLinkCount<100) {
			maxLinkCount=100;
		}
		this.maxLinkDistance = maxLinkDistance;
		this.maxLinkCount = maxLinkCount;
		this.links.clear();
		if (links!=null) {
			this.links = links;
		}
		unlockMe(this);
	}

	/**
	 * Initializes the confabulator for usage.
	 * 
	 * @param original The original confabulator to copy the settings and link data set from
	 */
	public void initialize(ConfabulatorObject original) {
		initialize(original.getMaxLinkDistance(),original.getMaxLinkCount(),original.getLinks());
	}

	/**
	 * Creates a copy of the confabulator which can then be used to initialize other confabulators.
	 * 
	 * @return A copy of this confabulator
	 */
	public ConfabulatorObject getCopy() {
		ConfabulatorObject copy = new ConfabulatorObject(getMsgr());
		lockMe(this);
		int maxD = maxLinkDistance;
		int maxC = maxLinkCount;
		List<Link> linksCopy = new ArrayList<Link>();
		for (Link lnk: links) {
			linksCopy.add(lnk.getCopy());
		}
		unlockMe(this);
		copy.initialize(maxD,maxC,linksCopy);
		return copy;
	}

	/**
	 * Returns the maximum sequence link distance used to limit symbol link learning.
	 * 
	 * @return the maximum sequence link distance
	 */
	public final int getMaxLinkDistance() {
		int r = 0;
		lockMe(this);
		r = maxLinkDistance;
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns the maximum link count used when learning symbol links.
	 * 
	 * @return the maximum link count
	 */
	public final int getMaxLinkCount() {
		int r = 0;
		lockMe(this);
		r = maxLinkCount;
		unlockMe(this);
		return r;
	}

	/**
	 * Returns a copy of the list of links.
	 * 
	 * @return A copy of the list of links
	 */
	public final List<Link> getLinks() {
		List<Link> r = new ArrayList<Link>();
		lockMe(this);
		for (Link lnk: links) {
			r.add(lnk.getCopy());
		}
		unlockMe(this);
		return r;
	}

	/**
	 * Clears the list of links.
	 */
	public final void clearLinks() {
		lockMe(this);
		links.clear();
		unlockMe(this);
	}
	
	protected final int getMaxLinkDistanceNoLock() {
		return maxLinkDistance;
	}

	protected final int getMaxLinkCountNoLock() {
		return maxLinkCount;
	}

	protected final List<Link> getLinksNoLock() {
		return links;
	}
}
