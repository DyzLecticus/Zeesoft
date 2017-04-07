package nl.zeesoft.zjmo.orchestra;

/**
 * Orchestra channels.
 */
public class Channel {
	private String		name						= "";
	private boolean		failOnSubscriberError		= false;

	public Channel(String name, boolean failOnSubscriberError) {
		this.name = name;
		this.failOnSubscriberError = failOnSubscriberError;
	}
	
	public String getName() {
		return name;
	}

	public boolean isFailOnSubscriberError() {
		return failOnSubscriberError;
	}

	public void setFailOnSubscriberError(boolean failOnSubscriberError) {
		this.failOnSubscriberError = failOnSubscriberError;
	}
}
