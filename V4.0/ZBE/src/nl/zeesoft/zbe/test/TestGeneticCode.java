package nl.zeesoft.zbe.test;

import nl.zeesoft.zbe.brain.GeneticCode;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestGeneticCode extends TestObject {
	public TestGeneticCode(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestGeneticCode(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and mutate a *GeneticCode*.");
		System.out.println();
		/* TODO describe
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the test configuration");
		System.out.println("Config config = new Config();");
		System.out.println("// Convert the test configuration to JSON");
		System.out.println("JsFile json = config.toJson();");
		System.out.println("// Convert the test configuration from JSON");
		System.out.println("config.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestGeneticCode.class));
		System.out.println(" * " + getTester().getLinkForClass(Config.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		GeneticCode genCode = new GeneticCode();
		int length = 100;
		int mutations = 5;
		
		genCode.generate(length);
		ZStringBuilder ori = genCode.getCode();
		System.out.println("Genetic code: " + ori);
		assertEqual(genCode.getCode().length(),length,"Code length does not match expectation");

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
		
		System.out.println("Property values;");
		muts = 0;
		GeneticCode oriCode = new GeneticCode(ori);
		for (int p = 0; p < genCode.size(); p++) {
			int oVal = oriCode.getValue(p,1000);
			int mVal = genCode.getValue(p,1000);
			String append = "";
			if (mVal!=oVal) {
				append = " <";
				muts++;
			}
			System.out.println(p + ": " + mVal + append);
		}
		System.out.println("Mutated factors: " + muts);
		System.out.println();
		
		genCode = new GeneticCode(new ZStringBuilder("9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999"));
		System.out.println("Genetic code: " + genCode.getCode());
		assertEqual(genCode.size(),33,"Size does not match expectation");
		
		int val = 0;
		
		val = genCode.getValue(0,25);
		System.out.println("Property 0, scale 25: " + val);
		assertEqual(val,25,"Property value does not match expectation");
		
		val = genCode.getValue(0,100);
		System.out.println("Property 0, scale 100: " + val);
		assertEqual(val,100,"Property value does not match expectation");
		
		val = genCode.getValue(0,250);
		System.out.println("Property 0, scale 250: " + val);
		assertEqual(val,250,"Property value does not match expectation");
		
		val = genCode.getValue(0,1000);
		System.out.println("Property 0, scale 1000: " + val);
		assertEqual(val,999,"Property value does not match expectation");
		
		val = genCode.getValue(32,1000);
		System.out.println("Property 32, scale 1000: " + val);
		assertEqual(val,999,"Property value does not match expectation");
		
		val = genCode.getValue(33,1000);
		System.out.println("Property 33, scale 1000: " + val);
		assertEqual(val,-1000,"Property value does not match expectation");
		
		val = genCode.getValue(99,1000);
		System.out.println("Property 99, scale 1000: " + val);
		assertEqual(val,-1000,"Property value does not match expectation");
	}
}
