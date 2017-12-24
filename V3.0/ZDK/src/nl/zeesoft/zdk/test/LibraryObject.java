package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract library object.
 * 
 * Can be used to create self documenting and self testing libraries.
 */
public abstract class LibraryObject {
	private	Tester				tester			= null;
	private String 				nameAbbreviated	= "";
	private String 				nameFull		= "";
	private String 				version			= "[???]";
	private String 				baseProjectUrl	= "";
	private String 				baseSrcUrl		= "src/";
	private String 				baseReleaseUrl	= "releases/";
	private List<LibraryObject> dependencies	= new ArrayList<LibraryObject>();
	
	public LibraryObject(Tester tester) {
		this.tester = tester;
	}
	
	/**
	 * Call this method from the main method.
	 * 
	 * Make sure to pass the version number as the first argument.
	 * 
	 * @param args The arguments
	 */
	public void describeAndTest(String[] args) {
		if (args!=null && args.length>=1 && args[0]!=null && args[0].length()>0) {
			version = args[0];
		}
		tester.setBaseUrl(baseSrcUrl);
		addTests(tester.getTests());
		describe();
		boolean success = tester.test(args);
		if (success) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
	
	/**
	 * Describe the library.
	 */
	public abstract void describe();
	
	/**
	 * Add the tests for this library to the list.
	 * 
	 * @param tests The list of tests for this library
	 */
	public abstract void addTests(List<TestObject> tests);

	/**
	 * Tests the library.
	 * 
	 * @param args The command line arguments
	 * @return True if the tests were successful
	 */
	protected boolean test(String[] args) {
		return tester.test(args);
	}

	protected void describeDependencies() {
		if (dependencies.size()>0) {
			if (dependencies.size()==1) {
				System.out.println("This library depends on the [" + dependencies.get(0).getNameFull() + "](" + dependencies.get(0).getBaseProjectUrl() + ").  ");
			} else {
				System.out.println("**Dependencies**  ");
				System.out.println("This library depends on the following libraries;  ");
				for (LibraryObject library: dependencies) {
					System.out.println(" * [" + library.getNameFull() + "](" + library.getBaseProjectUrl() + ")  ");		
				}
			}
		}
	}

	protected void describeRelease() {
		System.out.println("**Release downloads**  ");
		System.out.println("Click [here](" + baseReleaseUrl + nameAbbreviated.toLowerCase() + "-" + version + ".zip) to download the latest " + nameAbbreviated + " release (version " + version + ").  ");
		System.out.println("All " + nameAbbreviated + " releases can be downloaded [here](" + baseReleaseUrl + ").  ");
		System.out.println("*All jar files in the release include source code and build scripts.*  ");
	}

	protected void describeTesting(Class<?> cls) {
		System.out.println("**Self documenting and self testing**  ");
		System.out.println("The tests used to develop this libary are also used to generate this README file.  ");		
		System.out.println("Run the " + tester.getLinkForClass(cls) + " class as a java application to print this documentation to the standard out.  ");
		System.out.println("Click [here](#test-results) to scroll down to the test result summary.  ");
	}

	public Tester getTester() {
		return tester;
	}

	public String getNameAbbreviated() {
		return nameAbbreviated;
	}

	public void setNameAbbreviated(String nameAbbreviated) {
		this.nameAbbreviated = nameAbbreviated;
	}

	public String getNameFull() {
		return nameFull;
	}

	public void setNameFull(String nameFull) {
		this.nameFull = nameFull;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBaseSrcUrl() {
		return baseSrcUrl;
	}

	public void setBaseSrcUrl(String baseSrcUrl) {
		this.baseSrcUrl = baseSrcUrl;
	}

	public String getBaseReleaseUrl() {
		return baseReleaseUrl;
	}

	public void setBaseReleaseUrl(String baseReleaseUrl) {
		this.baseReleaseUrl = baseReleaseUrl;
	}

	public String getBaseProjectUrl() {
		return baseProjectUrl;
	}

	public void setBaseProjectUrl(String baseProjectUrl) {
		this.baseProjectUrl = baseProjectUrl;
	}

	public List<LibraryObject> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<LibraryObject> dependencies) {
		this.dependencies = dependencies;
	}
}
