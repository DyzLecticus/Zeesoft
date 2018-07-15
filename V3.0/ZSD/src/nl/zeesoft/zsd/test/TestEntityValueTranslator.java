package nl.zeesoft.zsd.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;

public class TestEntityValueTranslator extends TestObject {
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
		getTester().describeMock(MockEntityValueTranslator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestEntityValueTranslator.class));
		System.out.println(" * " + getTester().getLinkForClass(MockEntityValueTranslator.class));
		System.out.println(" * " + getTester().getLinkForClass(EntityValueTranslator.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it takes to initialize the translator  ");
		System.out.println(" * The translation results including the time it takes, for a set of input sequences  ");
	}
	
	@Override
	protected void test(String[] args) {
		EntityValueTranslator t = (EntityValueTranslator) getTester().getMockedObject(MockEntityValueTranslator.class.getName());

		testTranslation(t,"",
			"Eat three donuts at 9:00 or count to 110",
			"UNI_ABC:Eat ENG_NUM:3|UNI_ABC:three UNI_ABC:donuts UNI_ABC:at UNI_TIM:09:00:00 UNI_ABC:or UNI_ABC:count UNI_ABC:to UNI_NUM:110",
			"Eat three donuts at 09:00:00 or count to 110");
		testTranslation(t,BaseConfiguration.LANG_NLD,
			"Eet drie donuts om 9:00 of tel tot 110",
			"UNI_ABC:Eet NLD_NUM:3|UNI_ABC:drie UNI_ABC:donuts UNI_ABC:om UNI_TIM:09:00:00 UNI_ABC:of UNI_ABC:tel UNI_ABC:tot UNI_NUM:110",
			"Eet drie donuts om 09:00:00 of tel tot 110");
		testTranslation(t,"",
			"I finished twohundredandtwentyfourth or 225th",
			"UNI_ABC:I UNI_ABC:finished ENG_ORD:224|UNI_ABC:twohundredandtwentyfourth UNI_ABC:or ENG_OR2:225",
			"I finished twohundredandtwentyfourth or 225th");
		testTranslation(t,"",
			"Ik ben tweehonderdvierentwintigste geworden",
			"UNI_ABC:Ik UNI_ABC:ben NLD_ORD:224|UNI_ABC:tweehonderdvierentwintigste|NLD_NAM:firstName:UNI_ABC:Tweehonderdvierentwintigste UNI_ABC:geworden|NLD_NAM:lastName:UNI_ABC:Geworden",
			"Ik ben tweehonderdvierentwintigste geworden");
		testTranslation(t,"",
			"februari march october december",
			"NLD_MNT:2|UNI_ABC:februari ENG_MNT:3|UNI_ABC:march ENG_MNT:10|UNI_ABC:october ENG_MNT:12|NLD_MNT:12|UNI_ABC:december",
			"februari march october december");
		testTranslation(t,"",
			"thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten",
			"ENG_DUR:33:41 / NLD_DUR:33:41",
			"thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten");
		testTranslation(t,"",
			"yesterday OR today OR the 1st of october",
			"ENG_DAT:2018-07-15|UNI_ABC:yesterday UNI_ABC:OR ENG_DAT:2018-07-16|UNI_ABC:today UNI_ABC:OR ENG_DAT:2018-10-01",
			"july fifteenth twothousandeighteen OR july sixteenth twothousandeighteen OR october first twothousandeighteen");
		testTranslation(t,BaseConfiguration.LANG_NLD,
			"gisteren OF vandaag OF 1 oktober",
			"NLD_DAT:2018-07-15|UNI_ABC:gisteren UNI_ABC:OF NLD_DAT:2018-07-16|UNI_ABC:vandaag UNI_ABC:OF NLD_DAT:2018-10-01",
			"vijftien juli tweeduizendachttien OF zestien juli tweeduizendachttien OF een oktober tweeduizendachttien");
		testTranslation(t,"",
			"twelve o'clock OR five minutes to nine OR ten past one in the morning",
			"ENG_TIM:12:00:00 UNI_ABC:OR ENG_TIM:08:55:00 UNI_ABC:OR ENG_TIM:01:10:00",
			"twelve o'clock OR fiftyfive past eight OR ten past one in the morning");
		testTranslation(t,BaseConfiguration.LANG_NLD,
			"twaalf uur OF vijf minuten voor negen OF tien over een sochtends",
			"NLD_TIM:12:00:00|NLD_DUR:12:00 UNI_ABC:OF NLD_TIM:08:55:00 UNI_ABC:OF NLD_TIM:01:10:00",
			"twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends");
		testTranslation(t,BaseConfiguration.LANG_ENG,
			"My name is Andrew from the Sea",
			"UNI_ABC:My UNI_ABC:name UNI_ABC:is UNI_ABC:Andrew|ENG_NAM:firstName:UNI_ABC:Andrew ENG_PRE:4|ENG_NAM:preposition:ENG_PRE:4 UNI_ABC:Sea|ENG_NAM:lastName:UNI_ABC:Sea",
			"My name is Andrew from the Sea");
		testTranslation(t,BaseConfiguration.LANG_NLD,
			"Mijn naam is Andre van der Zee",
			"UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:is UNI_ABC:Andre|NLD_NAM:firstName:UNI_ABC:Andre NLD_PRE:3|NLD_NAM:preposition:NLD_PRE:3 UNI_ABC:Zee|NLD_NAM:lastName:UNI_ABC:Zee",
			"Mijn naam is Andre van der Zee");
		testTranslation(t,BaseConfiguration.LANG_NLD,
			"Hoe heet jij? gekke henkie",
			"UNI_ABC:Hoe UNI_ABC:heet UNI_ABC:jij? UNI_ABC:gekke|NLD_NAM:firstName:UNI_ABC:Gekke UNI_ABC:henkie|NLD_NAM:lastName:UNI_ABC:Henkie",
			"Hoe heet jij? gekke henkie");
		testTranslation(t,BaseConfiguration.LANG_NLD,
			"gekste der henkies is mijn naam",
			"UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies UNI_ABC:is UNI_ABC:mijn UNI_ABC:naam",
			"gekste der henkies is mijn naam");
		testTranslation(t,BaseConfiguration.LANG_ENG,
			"to Germany or France",
			"UNI_ABC:to ENG_CNT:DE|UNI_ABC:Germany UNI_ABC:or ENG_CNT:FR|UNI_ABC:France",
			"to Germany or France");
		testTranslation(t,BaseConfiguration.LANG_NLD,
			"naar Duitsland of Frankrijk",
			"UNI_ABC:naar NLD_CNT:DE|UNI_ABC:Duitsland UNI_ABC:of NLD_CNT:FR|UNI_ABC:Frankrijk",
			"naar Duitsland of Frankrijk");
		testTranslation(t,BaseConfiguration.LANG_ENG,
			"You asshole",
			"UNI_ABC:You ENG_PRF:1|UNI_ABC:asshole",
			"You asshole");
		testTranslation(t,BaseConfiguration.LANG_NLD,
			"Jij klootzak",
			"UNI_ABC:Jij NLD_PRF:2|UNI_ABC:klootzak",
			"Jij klootzak");

		System.out.println();
		ZStringSymbolParser sequence = new ZStringSymbolParser("UNI_ABC:Hoe UNI_ABC:heet UNI_ABC:jij? UNI_ABC:gekke|NLD_NAM:firstName:UNI_ABC:Gekke UNI_ABC:henkie|NLD_NAM:lastName:UNI_ABC:Henkie");
		List<String> iValues = sequence.toSymbols();
		int size = iValues.size();
		List<String> values = t.getTypeValuesFromInternalValues(iValues,"ABC","lastName","NAM");
		assertEqual(iValues.size(),(size - 1),"The number of internal values does not match expectation");
		assertEqual(values.size(),1,"The number of type values does not match expectation");
		if (values.size()>0) {
			assertEqual(values.get(0),"UNI_ABC:Henkie","The value does not match expectation");
		}
	}
	
	private void testTranslation(EntityValueTranslator t,String language,String seq,String expTran,String expRetran) {
		System.out.println();

		List<String> languages = new ArrayList<String>();
		List<String> types = new ArrayList<String>();
		
		if (language.length()>0) {
			languages.add(language);
			languages.add(BaseConfiguration.LANG_UNI);
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
