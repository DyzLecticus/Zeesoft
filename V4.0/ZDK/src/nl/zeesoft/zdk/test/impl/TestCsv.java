package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.CsvFile;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestCsv extends TestObject {
	public TestCsv(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestCsv(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *CsvFile* instance from a CSV string.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create CSV object");
		System.out.println("CsvFile csv = new CsvFile();");
		System.out.println("// Parse CSV from string");
		System.out.println("csv.fromStringBuilder(new ZStringBuilder(\"test,qwer,123\\n\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestCsv.class));
		System.out.println(" * " + getTester().getLinkForClass(CsvFile.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and the resulting number of rows and columns.  ");
	}

	@Override
	protected void test(String[] args) {
		ZStringBuilder testCsv = new ZStringBuilder();
		testCsv.append("test,qwer,123\n");
		testCsv.append("test,qwer,123,qwer\n");
		testCsv.append("\"t,e\\\"\\nst\",qwer,123\n");
		System.out.println("Input:");
		System.out.println(testCsv);
		CsvFile csv = new CsvFile();
		csv.fromStringBuilder(testCsv);
		System.out.println("Rows: " + csv.rows.size() + ", columns: " + csv.columns);
		
		assertEqual(csv.rows.size(),3,"Number of rows does not match expectation");
		assertEqual(csv.columns,3,"Number of columns does not match expectation");
		if (csv.rows.size()==3) {
			assertEqual(csv.rows.get(2)[0].toString(),"t,e\"\nst","Column value does not match expectation");
		}
	}
}
