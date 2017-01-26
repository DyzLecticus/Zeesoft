package nl.zeesoft.zsc.confabulator;

/**
 * Used by confabulators to contain context sensitive symbolic link data.
 */
public final class Link {
	private String 				symbolFrom		= "";
	private String	 			symbolContext	= "";
	private int					distance		= 0;
	private String 				symbolTo		= "";
	private int					count			= 2;
	
	/**
	 * Returns a copy of this link.
	 * 
	 * @return a copy of this link
	 */
	public Link getCopy() {
		Link copy = new Link();
		copy.setSymbolFrom(getSymbolFrom());
		copy.setSymbolContext(getSymbolContext());
		copy.setDistance(getDistance());
		copy.setSymbolTo(getSymbolTo());
		copy.setCount(getCount());
		return copy;
	}
	
	public String getSymbolFrom() {
		return symbolFrom;
	}

	public void setSymbolFrom(String symbolFrom) {
		this.symbolFrom = symbolFrom;
	}

	public String getSymbolContext() {
		return symbolContext;
	}

	public void setSymbolContext(String symbolContext) {
		this.symbolContext = symbolContext;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getSymbolTo() {
		return symbolTo;
	}

	public void setSymbolTo(String symbolTo) {
		this.symbolTo = symbolTo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
