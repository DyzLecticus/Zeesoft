package nl.zeesoft.zdk.confabulator;

/**
 * Abstract sequence confabulation object.
 */
public abstract class ConfabulationSequenceObject extends ConfabulationObject {
	private int							maxWidth			= 50;
	private boolean						forceMaxDepth		= false; 
	private boolean						strict				= true;

	public ConfabulationSequenceObject() {
		
	}

	public ConfabulationSequenceObject(String sequence) {
		super(sequence);
	}

	public ConfabulationSequenceObject(String sequence, String context) {
		super(sequence,context);
	}

	@Override
	public void copyToConfabulationObject(ConfabulationObject copy) {
		super.copyToConfabulationObject(copy);
		if (copy instanceof ConfabulationSequenceObject) {
			((ConfabulationSequenceObject) copy).setMaxWidth(maxWidth);
			((ConfabulationSequenceObject) copy).setForceMaxDepth(forceMaxDepth);
			((ConfabulationSequenceObject) copy).setStrict(strict);
		}
	}
	
	/**
	 * Returns the maximum width to be used for this confabulation.
	 * 
	 * @return The maximum width to be used for this confabulation
	 */
	public int getMaxWidth() {
		return maxWidth;
	}

	/**
	 * Sets the maximum width to be used for this confabulation (minimum 2, default 50).
	 * 
	 * Limits the maximum width of the search tree by cutting of least significant branches.
	 *
	 * @param maxWidth The maximum width
	 */
	public void setMaxWidth(int maxWidth) {
		if (maxWidth < 2) {
			maxWidth = 2;
		}
		this.maxWidth = maxWidth;
	}

	/**
	 * Returns true if the maximum depth of the search tree should be used.
	 * 
	 * @return true if the maximum depth of the search tree should be used
	 */
	public boolean isForceMaxDepth() {
		return forceMaxDepth;
	}

	/**
	 * Indicates the maximum depth of the search tree should be used (default false).
	 * 
	 * When false, the minimum depth that yields a conclusion is used.
	 * 
	 * @param forceMaxDepth True if the maximum depth of the search tree should be used
	 */
	public void setForceMaxDepth(boolean forceMaxDepth) {
		this.forceMaxDepth = forceMaxDepth;
	}
	
	/**
	 * Returns true if correction and extension should be limited to sequentially learned transitions.
	 * 
	 * @return True if correction and extension should be limited to sequentially learned transitions
	 */
	public boolean isStrict() {
		return strict;
	}

	/**
	 * Indicates correction and extension should be limited to sequentially learned transitions (default true).
	 * 
	 * @param strict True if correction and extension should be limited to sequentially learned transitions
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}
}
