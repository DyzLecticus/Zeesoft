package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Tester singleton provides an easy way to create a self testing and documenting library.
 * It is designed to sequentially execute tests that extend the abstract TestObject class.
 */
public final class Tester {
	private static Tester		tester			= null;

	private String				baseUrl			= "";
	private List<TestObject>	tests 			= new ArrayList<TestObject>();
	private List<MockObject>	mocks			= new ArrayList<MockObject>();
	
	private Tester() {
		// Singleton
	}
	
	/**
	 * Use this method to access the singleton.
	 * 
	 * @return The Tester singleton 
	 */
	public static Tester getInstance() {
		if (tester==null) {
			tester = new Tester();
		}
		return tester;
	}

	/**
	 * Blocks instance cloning by throwing a CloneNotSupportedException.
	 * 
	 * @return null
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(); 
	}
	
	/**
	 * Use this method to specify a base URL for documentation links.
	 * 
	 * @param baseUrl The base URL
	 */
	public final void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Use this method to add (or remove) tests.
	 */
	public List<TestObject> getTests() {
		return tests;
	}

	/**
	 * Call this method to execute all the tests.
	 *  
	 * @param args Optional command line arguments
	 * @return True if all tests executed successfully
	 */
	public final boolean test(String[] args) {
		boolean success = true;
		
		if (tests.size()==0) {
			System.out.println(this.getClass().getName() + ": Nothing to test");
			System.exit(0);
			return success;
		}

		int assertions = 0;
		List<String> failures = new ArrayList<String>();
		int sleepMs = 0;
		
		Date start = new Date();
		
		int i = 0;
		for (TestObject test: tests) {
			if (i>0) {
				System.out.println();
			}
			i++;
			System.out.println(test.getClass().getName());
			for (int c = 0; c < test.getClass().getName().length(); c++) {
				System.out.print("-");
			}
			System.out.println();
			test.describe();
			System.out.println("~~~~");
			test.test(args);
			assertions += test.getAssertions();
			failures.addAll(test.getFailures());
			sleepMs += test.getSleeptMs();
			System.out.println("~~~~");
		}
		
		System.out.println();
		System.out.println("Test results");
		System.out.println("------------");
		if (failures.size()>0) {
			String have = "have";
			if (failures.size()==1) {
				have = "has";
			}
			System.err.println(failures.size() + " of " + assertions + " assertions " + have + " failed;");
			for (String failure: failures) {
				System.err.println(" * " + failure);
			}
		} else {
			System.out.println("All " + tests.size() + " tests have been executed successfully (" + assertions + " assertions).  ");
			System.out.println("Total test duration: " + ((new Date()).getTime() - start.getTime()) + " ms (total sleep duration: " + sleepMs + " ms).  ");
		}
		
		if (failures.size()>0) {
			success = false;
		}
		return success;
	}
	
	/**
	 * Returns the anchor URL for a certain test.
	 * 
	 * @param cls The class (must extends TestObject)
	 * @return The anchor URL
	 */
	public String getAnchorUrlForTest(@SuppressWarnings("rawtypes") Class cls) {
		return "#" + cls.getName().replace(".","").toLowerCase();
	}

	/**
	 * Returns the URL for the source code of certain class.
	 * 
	 * @param cls The class
	 * @return the URL for the source code
	 */
	public String getUrlForClass(@SuppressWarnings("rawtypes") Class cls) {
		return baseUrl + "/src/" + cls.getName().replace(".","/") + ".java";
	}
	
	/**
	 * Returns the link (mark down syntax) for a certain class.
	 * 
	 * @param cls The class
	 * @return The link
	 */
	public String getLinkForClass(@SuppressWarnings("rawtypes") Class cls) {
		String[] split = cls.getName().split("\\.");
		return "[" + split[split.length - 1] + "](" + getUrlForClass(cls) + ")";
	}
	
	/**
	 * Used to describe mocks within tests.
	 * 
	 * Uses a buffer to store and retrieve MockObject instances.
	 * If an instance with the specified class name is not yet available it instantiates the MockObject and adds it to the buffer.
	 * 
	 * @param mockObjectClassName The class name of the mock to describe
	 */
	public void describeMock(String mockObjectClassName) {
		MockObject mock = getMock(mockObjectClassName);
		if (mock!=null) {
			mock.describe();
		}
	}

	/**
	 * Returns the mocked object.
	 * 
	 * Uses a buffer to store and retrieve MockObject instances.
	 * If an instance with the specified class name is not yet available it instantiates the MockObject and adds it to the buffer.
	 * 
	 * @param mockObjectClassName The class name of the mock to describe
	 * @return The mocked object
	 */
	public Object getMockedObject(String mockObjectClassName) {
		Object r = null;
		MockObject mock = getMock(mockObjectClassName);
		if (mock!=null) {
			r = mock.getMockedObject();
		}
		return r;
	}

	private MockObject getMock(String mockObjectClassName) {
		MockObject r = null; 
		for (MockObject mock: mocks) {
			if (mock.getClass().getName().equals(mockObjectClassName)) {
				r = mock;
				break;
			}
		}
		if (r==null) {
			Class<?> mockObjectClass;
			try {
				mockObjectClass = Class.forName(mockObjectClassName);
				r = (MockObject) mockObjectClass.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
}
