package nl.zeesoft.zsds.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zsd.test.ZSD;
import nl.zeesoft.zsds.ZSDSServlet;

/**
 * Documents and tests the ZSD.
 */
public class ZSDS extends LibraryObject {
	public ZSDS(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSDS");
		setNameFull("Zeesoft Smart Dialog Server");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSDS/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSDS/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSDS/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZSD(null));
	}

	public static void main(String[] args) {
		(new ZSDS(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Smart Dialog Server");
		System.out.println("===========================");
		System.out.println(ZSDSServlet.DESCRIPTION);
		System.out.println();
		System.out.println("Features include;  ");
		System.out.println(" * A web based administration interface  ");
		System.out.println(" * Out-of-the-box support for basic English and Dutch entities and dialogs  ");
		System.out.println(" * Request specific contextual behavior and response randomization   ");
		System.out.println(" * Three level hierarchical intent classification (including language classification)  ");
		System.out.println(" * Input preprocessing and spelling correction  ");
		System.out.println(" * Automated self and DTAP environment integration testing  ");
		System.out.println(" * Highly configurable, extendable and scalable architecture  ");
		System.out.println();
		System.out.println("Out-of-the-box Universal, English and Dutch entities include;  ");
		System.out.println(" * Names of people  ");
		System.out.println(" * Country and language names  ");
		System.out.println(" * Currency codes and names  ");
		System.out.println(" * Typed smiley and frowny emoticons  ");
		System.out.println(" * Month, date, time and duration  ");
		System.out.println(" * Confirmation booleans  ");
		System.out.println(" * Integer numbers  ");
		System.out.println(" * Profanity  ");
		System.out.println();
		describeDependencies();
		System.out.println();
		System.out.println("**Downloads**  ");
		System.out.println("Click [here](" + getBaseReleaseUrl() + "zsds-dev.war) to download the latest ZSDS development WAR.  ");
		System.out.println("Click [here](" + getBaseReleaseUrl() + "zsds.war) to download the latest ZSDS production WAR.  ");
		System.out.println("The only difference between the war files is the initial configuration.  ");
		System.out.println("In the development WAR, debugging and automated self testing are enabled by default.  ");
		System.out.println();
		describeTesting(ZSDS.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestTestConfiguration(getTester()));
		tests.add(new TestTestCaseSet(getTester()));
	}
}
