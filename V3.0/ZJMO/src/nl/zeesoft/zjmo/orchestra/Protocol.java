package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;

public class Protocol {
	private boolean stop = false;
	
	protected ZStringBuilder handleInput(ZStringBuilder input) {
		return input;
	}

	protected boolean isStop() {
		return stop;
	}

	protected void setStop(boolean stop) {
		this.stop = stop;
	}

}
