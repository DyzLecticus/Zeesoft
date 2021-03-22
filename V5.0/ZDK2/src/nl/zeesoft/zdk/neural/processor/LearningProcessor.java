package nl.zeesoft.zdk.neural.processor;

public abstract class LearningProcessor extends Processor {
	protected boolean	learn	= true;
	
	public boolean isLearn() {
		return learn;
	}
	
	public void setLearn(boolean learn) {
		this.learn = learn;
	}
	
	public abstract void reset();
}
