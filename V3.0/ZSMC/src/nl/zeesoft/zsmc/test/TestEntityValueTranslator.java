package nl.zeesoft.zsmc.test;

import java.util.Date;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.EntityValueTranslator;

public class TestEntityValueTranslator extends TestObject {
	public static final String QNA_FILE_NAME = "resources/nl-qna.txt";
	
	public TestEntityValueTranslator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestEntityValueTranslator(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *EntityValueTranslator* to translate a sequence to and from internal values.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the EntityValueTranslator");
		System.out.println("EntityValueTranslator translator = new EntityValueTranslator();");
		System.out.println("// Initialize the EntityValueTranslator");
		System.out.println("translator.initialize();");
		System.out.println("// Use EntityValueTranslator to translate a sequence");
		System.out.println("ZStringSymbolParser translated = translator.translateToInternalValues(new ZStringSymbolParser(\"some sequence\"));");
		System.out.println("ZStringSymbolParser retranslated = translator.translateToExternalValues(translated);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestEntityValueTranslator.class));
		System.out.println(" * " + getTester().getLinkForClass(EntityValueTranslator.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it takes to initialize the translator  ");
		System.out.println(" * The translation results including the time it takes, for a set of input sequences  ");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		EntityValueTranslator t = new EntityValueTranslator();
		t.initialize();
		System.out.println("Initializing the EntityValueTranslator took: " + ((new Date()).getTime() - started.getTime()) + " ms");

		testTranslation(t,
			"Eat three donuts at 9:00 or count to 110",
			"UNI_ABC:Eat ENG_NUM:3|UNI_ABC:three UNI_ABC:donuts UNI_ABC:at UNI_TIM:09:00:00 UNI_ABC:or UNI_ABC:count UNI_ABC:to UNI_NUM:110",
			"Eat three donuts at 09:00:00 or count to 110"
			);

		testTranslation(t,
			"Eet drie donuts om 9:00 of tel tot 110",
			"UNI_ABC:Eet NLD_NUM:3|UNI_ABC:drie UNI_ABC:donuts UNI_ABC:om UNI_TIM:09:00:00 UNI_ABC:of UNI_ABC:tel UNI_ABC:tot UNI_NUM:110",
			"Eet drie donuts om 09:00:00 of tel tot 110"
			);

		testTranslation(t,
			"I finished twohundredandtwentyfourth or 225th",
			"UNI_ABC:I UNI_ABC:finished ENG_ORD:224|UNI_ABC:twohundredandtwentyfourth UNI_ABC:or ENG_OR2:225",
			"I finished twohundredandtwentyfourth or 225th"
			);

		testTranslation(t,
			"Ik ben tweehonderdvierentwintigste geworden",
			"UNI_ABC:Ik UNI_ABC:ben NLD_ORD:224|UNI_ABC:tweehonderdvierentwintigste UNI_ABC:geworden",
			"Ik ben tweehonderdvierentwintigste geworden"
			);

		testTranslation(t,
			"februari march october december",
			"NLD_MNT:2|UNI_ABC:februari ENG_MNT:3|UNI_ABC:march ENG_MNT:10|UNI_ABC:october ENG_MNT:12|NLD_MNT:12|UNI_ABC:december",
			"februari march october december"
			);

		testTranslation(t,
			"thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten",
			"ENG_DUR:33:41 / NLD_DUR:33:41",
			"thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten"
			);
	}
	
	private void testTranslation(EntityValueTranslator t,String seq,String expTran,String expRetran) {
		System.out.println();
		
		Date started = new Date();
		ZStringSymbolParser sequence = new ZStringSymbolParser(seq);
		ZStringSymbolParser expectedTranslation = new ZStringSymbolParser(expTran);
		ZStringSymbolParser expectedRetranslation = new ZStringSymbolParser(expRetran);
		ZStringSymbolParser translation = t.translateToInternalValues(sequence);

		System.out.println("Sequence: " + sequence);
		System.out.println("Translation: " + translation);
		System.out.println("Translating the sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		started = new Date();
		ZStringSymbolParser retranslation = t.translateToExternalValues(translation);
		System.out.println("Retranslation: " + retranslation);
		System.out.println("Retranslating the sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		
		assertEqual(translation,expectedTranslation,"The translation does not match expectation");
		assertEqual(retranslation,expectedRetranslation,"The retranslation does not match expectation");
	}
}
