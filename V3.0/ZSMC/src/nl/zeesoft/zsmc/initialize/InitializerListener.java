package nl.zeesoft.zsmc.initialize;

public interface InitializerListener {
	/**
	 * This method is invoked when a specific class has been initialized.
	 * 
	 * @param cls The initialized class
	 * @param done Indicates all classes have been initialized
	 */
	public void initializedClass(InitializeClass cls, boolean done);
}
