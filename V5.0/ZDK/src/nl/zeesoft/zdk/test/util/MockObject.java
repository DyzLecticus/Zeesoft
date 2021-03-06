package nl.zeesoft.zdk.test.util;

/**
 * Abstract mock object.
 * Can be used to create mock objects and share them across tests.
 */
public abstract class MockObject {
	private Tester	tester			= null;
	private Object	mockedObject	= null;
	
	/**
	 * Print the description of this mock to System.out.
	 */
	protected abstract void describe();
	
	/**
	 * Initialize and return the mock object.
	 * 
	 * @return The mock object
	 */
	protected abstract Object initialzeMock();

	/**
	 * Returns the mocked object.
	 * 
	 * If the object has not yet been mocked it initializes and stores it for future reference.
	 * 
	 * @return The mocked object
	 */
	protected Object getMockedObject() {
		if (mockedObject==null) {
			mockedObject = initialzeMock();
		}
		return mockedObject;
	}

	protected Tester getTester() {
		if (tester==null) {
			tester = new Tester();
		}
		return tester;
	}

	protected void setTester(Tester tester) {
		this.tester = tester;
	}
}
