package nl.zeesoft.zjmo.orchestra;

/**
 * Orchestra channels.
 */
public class Channel {
	private String		name						= "";
	private boolean		failOnMissingSubscriber		= false;

	public Channel(String name, boolean failOnMissingSubscriber) {
		this.name = name;
		this.failOnMissingSubscriber = failOnMissingSubscriber;
	}
	
	public String getName() {
		return name;
	}

	public boolean isFailOnMissingSubscriber() {
		return failOnMissingSubscriber;
	}

	public void setFailOnMissingSubscriber(boolean failOnMissingSubscriber) {
		this.failOnMissingSubscriber = failOnMissingSubscriber;
	}
}
