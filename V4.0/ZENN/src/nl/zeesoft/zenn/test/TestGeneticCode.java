package nl.zeesoft.zenn.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zenn.GeneticCode;

public class TestGeneticCode extends TestObject {
	public TestGeneticCode(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestGeneticCode(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create, mutate and use a *GeneticCode*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the genetic code");
		System.out.println("GeneticCode genCode = new GeneticCode(100);");
		System.out.println("// Mutate 5 genes");
		System.out.println("genCode.mutate(5);");
		System.out.println("// Get the number of properties");
		System.out.println("int size = genCode.size();");
		System.out.println("// Get a property value");
		System.out.println("float f = genCode.get(4);");
		System.out.println("// Get a scaled integer property value");
		System.out.println("int i = genCode.getInteger(4,100);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestGeneticCode.class));
		System.out.println(" * " + getTester().getLinkForClass(GeneticCode.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * A generated genetic code  ");
		System.out.println(" * The mutated genetic code and the resulting scaled property values  ");
	}
	
	@Override
	protected void test(String[] args) {
		int length = 100;
		int mutations = 5;
		GeneticCode genCode = new GeneticCode(length);
		
		ZStringBuilder ori = genCode.getCode();
		System.out.println("Genetic code: " + ori);
		assertEqual(genCode.length(),length,"Code length does not match expectation");

		genCode.mutate(mutations);
		ZStringBuilder mut = genCode.getCode();
		ZStringBuilder pointers = new ZStringBuilder();
		int muts = 0;
		for(int i = 0; i < ori.length(); i++) {
			if (!ori.substring(i,i+1).equals(mut.substring(i,i+1))) {
				muts++;
				pointers.append("^");
			} else {
				pointers.append(" ");
			}
		}
		assertEqual(muts,mutations,"Number of mutations does not match expectation");
		System.out.println("Mutated code: " + mut);
		System.out.println("              " + pointers);
		System.out.println();
		
		System.out.println("Scaled property values;");
		muts = 0;
		GeneticCode oriCode = new GeneticCode(ori);
		for (int p = 0; p < genCode.size(); p++) {
			int oVal = oriCode.getInteger(p,1000);
			int mVal = genCode.getInteger(p,1000);
			String append = "";
			if (mVal!=oVal) {
				append = " <";
				muts++;
			}
			System.out.println(p + ": " + mVal + append);
		}
		System.out.println("Mutated property values: " + muts);
		
		genCode = new GeneticCode(new ZStringBuilder("9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999"));
		assertEqual(genCode.size(),33,"Size does not match expectation");
		
		int val = 0;
		
		val = genCode.getInteger(0,5);
		assertEqual(val,5,"Property value does not match expectation");
		
		val = genCode.getInteger(0,25);
		assertEqual(val,25,"Property value does not match expectation");
		
		val = genCode.getInteger(0,100);
		assertEqual(val,100,"Property value does not match expectation");
		
		val = genCode.getInteger(0,250);
		assertEqual(val,250,"Property value does not match expectation");
		
		val = genCode.getInteger(0,1000);
		assertEqual(val,999,"Property value does not match expectation");
		
		val = genCode.getInteger(32,1000);
		assertEqual(val,999,"Property value does not match expectation");
		
		val = genCode.getInteger(33,1000);
		assertEqual(val,-1000,"Property value does not match expectation");
		
		val = genCode.getInteger(99,1000);
		assertEqual(val,-1000,"Property value does not match expectation");
	}
}
