package nl.zeesoft.zevt.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zevt.trans.UniversalMathematic;
import nl.zeesoft.zodb.lang.Languages;

public class TestTranslator extends TestObject {
	public TestTranslator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTranslator(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *Translator* to translate a sequence to and from internal values.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Translator");
		System.out.println("Translator translator = new Translator(new Config());");
		System.out.println("// Initialize the Translator (and wait or listen for initialization to finish)");
		System.out.println("translator.initialize();");
		System.out.println("// Use Translator to translate a sequence");
		System.out.println("ZStringSymbolParser translated = translator.translateToInternalValues(new ZStringSymbolParser(\"some sequence\"));");
		System.out.println("ZStringSymbolParser retranslated = translator.translateToExternalValues(translated);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockTranslator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTranslator.class));
		System.out.println(" * " + getTester().getLinkForClass(MockTranslator.class));
		System.out.println(" * " + getTester().getLinkForClass(Translator.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it takes to initialize the translator  ");
		System.out.println(" * The translation results including the time it takes, for a set of input sequences  ");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		Translator t = (Translator) getTester().getMockedObject(MockTranslator.class.getName());
		while(!t.isInitialized()) {
			sleep(100);
		}
		System.out.println("Initializing the Translator took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		
		testTranslation(t,"",
			"Eat three donuts at 9:00 or count to 110",
			"UN_ABC:Eat EN_NUM:3|UN_ABC:three UN_ABC:donuts UN_ABC:at UN_TIM:09:00:00 UN_ABC:or UN_ABC:count UN_ABC:to UN_NUM:110",
			"Eat three donuts at 09:00:00 or count to 110");
		testTranslation(t,Languages.NLD,
			"Eet drie donuts om 9:00 of tel tot 110",
			"UN_ABC:Eet NL_NUM:3|UN_ABC:drie UN_ABC:donuts UN_ABC:om UN_TIM:09:00:00 UN_ABC:of UN_ABC:tel UN_ABC:tot UN_NUM:110",
			"Eet drie donuts om 09:00:00 of tel tot 110");
		testTranslation(t,"",
			"I finished twohundredandtwentyfourth or 225th",
			"UN_ABC:I UN_ABC:finished EN_ORD:224|UN_ABC:twohundredandtwentyfourth UN_ABC:or EN_OR2:225",
			"I finished twohundredandtwentyfourth or 225th");
		testTranslation(t,"",
			"Ik ben tweehonderdvierentwintigste geworden",
			"UN_ABC:Ik UN_ABC:ben NL_ORD:224|UN_ABC:tweehonderdvierentwintigste UN_ABC:geworden",
			"Ik ben tweehonderdvierentwintigste geworden");
		testTranslation(t,"",
			"februari march october december",
			"NL_MNT:2|UN_ABC:februari EN_MNT:3|UN_ABC:march EN_MNT:10|UN_ABC:october EN_MNT:12|NL_MNT:12|UN_ABC:december",
			"februari march october december");
		testTranslation(t,"",
			"thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten",
			"EN_DUR:33:41 UN_MTH:D NL_DUR:33:41",
			"thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten");
		testTranslation(t,"",
			"yesterday OR today OR the 1st of october",
			"EN_DAT:2018-07-15|UN_ABC:yesterday UN_ABC:OR EN_DAT:2018-07-16|UN_ABC:today UN_ABC:OR EN_DAT:2018-10-01",
			"july fifteenth twothousandeighteen OR july sixteenth twothousandeighteen OR october first twothousandeighteen");
		testTranslation(t,Languages.NLD,
			"gisteren OF vandaag OF 1 oktober",
			"NL_DAT:2018-07-15|UN_ABC:gisteren UN_ABC:OF NL_DAT:2018-07-16|UN_ABC:vandaag UN_ABC:OF NL_DAT:2018-10-01",
			"vijftien juli tweeduizendachttien OF zestien juli tweeduizendachttien OF een oktober tweeduizendachttien");
		testTranslation(t,"",
			"twelve o'clock OR five minutes to nine OR ten past one in the morning",
			"EN_TIM:12:00:00 UN_ABC:OR EN_TIM:08:55:00 UN_ABC:OR EN_TIM:01:10:00",
			"twelve o'clock OR fiftyfive past eight OR ten past one in the morning");
		testTranslation(t,Languages.NLD,
			"twaalf uur OF vijf minuten voor negen OF tien over een sochtends",
			"NL_TIM:12:00:00|NL_DUR:12:00 UN_ABC:OF NL_TIM:08:55:00 UN_ABC:OF NL_TIM:01:10:00",
			"twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends");
		testTranslation(t,Languages.ENG,
			"to Germany or France",
			"UN_ABC:to EN_CNT:DE|UN_ABC:Germany UN_ABC:or EN_CNT:FR|UN_ABC:France",
			"to Germany or France");
		testTranslation(t,Languages.NLD,
			"naar Duitsland of Frankrijk",
			"UN_ABC:naar NL_CNT:DE|UN_ABC:Duitsland UN_ABC:of NL_CNT:FR|UN_ABC:Frankrijk",
			"naar Duitsland of Frankrijk");
		testTranslation(t,Languages.ENG,
			"You asshole",
			"UN_ABC:You EN_PRF:1|UN_ABC:asshole",
			"You asshole");
		testTranslation(t,Languages.NLD,
			"Jij klootzak",
			"UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak",
			"Jij klootzak");
		testTranslation(t,"",
			"Can I book a room for 5 people?",
			"UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?",
			"Can I book a room for 5 people?");
		testTranslation(t,"",
			"ten times five",
			"EN_NUM:10|NL_PRE:8|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:5|UN_ABC:five",
			"ten multiplied by five");
		testTranslation(t,"",
			"tien keer vijf",
			"NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:5|UN_ABC:vijf",
			"tien vermenigvuldigd met vijf");
		testTranslation(t,"",
			"fifteen british pound",
			"EN_NUM:15|UN_ABC:fifteen EN_CUR:GBP",
			"fifteen british pound sterling");
		testTranslation(t,"",
			"vijftien euro",
			"NL_NUM:15|UN_ABC:vijftien EN_CUR:EUR|NL_CUR:EUR|UN_ABC:euro",
			"vijftien euro");
		testTranslation(t,"",
			":-) ]0: {;",
			"UN_SML:7 UN_FRN:28 UN_SML:42",
			":-) ]0: {;");
		
		UniversalMathematic math = (UniversalMathematic) t.getEntityObject(Languages.UNI,EntityObject.TYPE_MATHEMATIC);
		
		ZStringSymbolParser expression = new ZStringSymbolParser("10 " + UniversalMathematic.MULTIPLICATION + " 3 " + UniversalMathematic.DIVISION + " 5");
		float result = math.calculate(expression);
		assertEqual("" + result,"6.0","The calculation did not produce the expected result");

		expression = new ZStringSymbolParser("10 " + UniversalMathematic.SUBTRACTION + " 3 " + UniversalMathematic.ADDITION + " 5");
		result = math.calculate(expression);
		assertEqual("" + result,"12.0","The calculation did not produce the expected result");

		expression = new ZStringSymbolParser("2 " + UniversalMathematic.ADDITION + " 2");
		result = math.calculate(expression);
		assertEqual("" + result,"4.0","The calculation did not produce the expected result");

		expression = new ZStringSymbolParser("10 " + UniversalMathematic.MULTIPLICATION + " 3 " + UniversalMathematic.ADDITION + " 25 " + UniversalMathematic.DIVISION + " 5 " + UniversalMathematic.SUBTRACTION + " 2");
		result = math.calculate(expression);
		assertEqual("" + result,"33.0","The calculation did not produce the expected result");

		expression = new ZStringSymbolParser("10 " + UniversalMathematic.SUBTRACTION + " 20");
		result = math.calculate(expression);
		assertEqual("" + result,"-10.0","The calculation did not produce the expected result");

		expression = new ZStringSymbolParser("10");
		result = math.calculate(expression);
		assertEqual("" + result,"10.0","The calculation did not produce the expected result");

		expression = new ZStringSymbolParser("10 D 3");
		result = math.calculate(expression);
		assertEqual("" + result,"3.3333333","The calculation did not produce the expected result");

		expression = new ZStringSymbolParser("10 " + UniversalMathematic.MULTIPLICATION + " 3 " + UniversalMathematic.ADDITION + " 25 " + UniversalMathematic.DIVISION + " 5 " + UniversalMathematic.SUBTRACTION + " 2");
		expression.append(" EQ ");
		expression.append("33 ");
		boolean r = math.evaluate(expression);
		assertEqual(r,true,"The evaluation did not produce the expected result");

		expression = new ZStringSymbolParser("10 " + UniversalMathematic.MULTIPLICATION + " 3 " + UniversalMathematic.ADDITION + " 25 " + UniversalMathematic.DIVISION + " 5 " + UniversalMathematic.SUBTRACTION + " 2");
		expression.append(" LT ");
		expression.append("11 " + UniversalMathematic.MULTIPLICATION + " 3 " + UniversalMathematic.ADDITION + " 25 " + UniversalMathematic.DIVISION + " 5 " + UniversalMathematic.SUBTRACTION + " 2");
		r = math.evaluate(expression);
		assertEqual(r,true,"The evaluation did not produce the expected result");
		
		t.destroy();
	}
	
	private void testTranslation(Translator t,String language,String seq,String expTran,String expRetran) {
		System.out.println();

		List<String> languages = new ArrayList<String>();
		List<String> types = new ArrayList<String>();
		
		if (language.length()>0) {
			languages.add(language);
			languages.add(Languages.UNI);
		}
		
		ZStringSymbolParser sequence = new ZStringSymbolParser(seq);
		ZStringSymbolParser expectedTranslation = new ZStringSymbolParser(expTran);
		ZStringSymbolParser expectedRetranslation = new ZStringSymbolParser(expRetran);

		System.out.println("Sequence: '" + sequence + "'");

		Date started = new Date();
		ZStringSymbolParser translation = t.translateToInternalValues(sequence,languages,types);
		System.out.println("Translating the sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		System.out.println("Translation: '" + translation + "'");
		
		started = new Date();
		ZStringSymbolParser retranslation = t.translateToExternalValues(translation);
		System.out.println("Retranslating the sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		System.out.println("Retranslation: '" + retranslation + "'");
		
		assertEqual(translation,expectedTranslation,"The translation does not match expectation");
		assertEqual(retranslation,expectedRetranslation,"The retranslation does not match expectation");
	}
}
