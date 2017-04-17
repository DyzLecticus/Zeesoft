package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestra channels.
 */
public class Channel {
	private String			name						= "";
	private boolean			failOnSubscriberError		= false;
	private	List<String>	subscriberIdList			= new ArrayList<String>();

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

	public List<String> getSubscriberIdList() {
		return subscriberIdList;
	}
}
