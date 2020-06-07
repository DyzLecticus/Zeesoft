package nl.zeesoft.zdk.test.http;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpClient;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestHttpClient extends TestObject {
	public TestHttpClient(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestHttpClient(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		/*
		System.out.println("This test shows how a *Str* instance can be used to split a comma separated string into a list of *Str* instances. ");
		System.out.println("The *Str* class is designed to add features of the Java String to a Java StringBuilder. ");
		System.out.println("It also contains methods for file writing and reading. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Str");
		System.out.println("Str str = new Str(\"qwer,asdf,zxcv\");");
		System.out.println("// Split the Str");
		System.out.println("List<Str> strs = str.split(\",\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockStr.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestStr.class));
		System.out.println(" * " + getTester().getLinkForClass(Str.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and lists the *Str* objects.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		HttpClient request = new HttpClient("GET","http://www.duckduckgo.com/");
		
		Str response = request.sendRequest();
		
		System.out.println(response);
		
		
		/*
		Str test = new Str((Str)getTester().getMockedObject(MockStr.class.getName()));
		assertEqual(test.trim(),new Str("qwer,asdf,zxcv"),"Encoding does not match expectation");
		System.out.println("Input: " + test);
		List<Str> strs = test.split(",");
		assertEqual(strs.size(),test.toString().split(",").length,"Split size does not match expectation");
		System.out.println("Split; ");
		for (Str str: strs) {
			System.out.println("- " + str);
		}
		assertEqual(strs.size(),3,"List size does not match expectation");
		assertEqual(strs.get(0),new Str("qwer"),"List element does not match expectation");
		assertEqual(strs.get(0).startsWith(new Str("qw")),true,"StartsWith result does not match expectation");
		assertEqual(strs.get(0).endsWith(new Str("er")),true,"EndsWith result does not match expectation");
		assertEqual(strs.get(0).equalsIgnoreCase(new Str("QWER")),true,"Equals ignore case result does not match expectation");
		assertEqual(strs.get(0).toUpperCase(),new Str("QWER"),"To upper case result does not match expectation");
		assertEqual(strs.get(0).replace("WE","UAZA"),new Str("QUAZAR"),"Replace result does not match expectation");
		assertEqual(test.merge(strs,","),new Str("QUAZAR,asdf,zxcv"),"Merge result does not match expectation");
		assertEqual((new Str("")).split(",").get(0).toString(),"".toString().split(",")[0],"Split does not match expectation");
		
		Str longStr = new Str();
		longStr.sb().append("@PO|nl.zeesoft.zdk.test.collection.CollectionTestChild@2");
		longStr.sb().append("\n");
		longStr.sb().append("@PP|testInt|EQ=111");
		longStr.sb().append("\n");
		longStr.sb().append("@PP|testBoolean|EQ=true");
		longStr.sb().append("\n");
		longStr.sb().append("@PP|testParents|EQ=nl.zeesoft.zdk.test.collection.CollectionTestParent@LS|nl.zeesoft.zdk.test.collection.CollectionTestParent@1|LC|nl.zeesoft.zdk.test.collection.CollectionTestParent@3|LE@");
		longStr.sb().append("\n");
		longStr.sb().append("@PP|testStrs|EQ=nl.zeesoft.zdk.Str@LS|null|LE@");
		longStr.sb().append("\n");
		longStr.sb().append("@PP|testStr|EQ=I like pizza!");
		longStr.sb().append("\n");
		longStr.sb().append("@PP|testString|EQ=TestChild1");
		
		longStr.replace("nl.zeesoft.zdk.test.collection.CollectionTestParent@", "@CN|000@");
		longStr.replace("nl.zeesoft.zdk.test.collection.CollectionTestChild@", "@CN|001@");
		
		Str expectedStr = new Str();
		expectedStr.sb().append("@PO|@CN|001@2");
		expectedStr.sb().append("\n");
		expectedStr.sb().append("@PP|testInt|EQ=111");
		expectedStr.sb().append("\n");
		expectedStr.sb().append("@PP|testBoolean|EQ=true");
		expectedStr.sb().append("\n");
		expectedStr.sb().append("@PP|testParents|EQ=@CN|000@LS|@CN|000@1|LC|@CN|000@3|LE@");
		expectedStr.sb().append("\n");
		expectedStr.sb().append("@PP|testStrs|EQ=nl.zeesoft.zdk.Str@LS|null|LE@");
		expectedStr.sb().append("\n");
		expectedStr.sb().append("@PP|testStr|EQ=I like pizza!");
		expectedStr.sb().append("\n");
		expectedStr.sb().append("@PP|testString|EQ=TestChild1");
		
		assertEqual(longStr,expectedStr,"Expected replacecement were not made");
		*/
	}
}
