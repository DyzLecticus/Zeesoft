package nl.zeesoft.zsd.test;

import java.util.Date;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.util.EntityToJson;

public class TestEntityToJson extends TestObject {
	private static final int	SEQUENCE_ELEMENTS	= 333335;
	
	public TestEntityToJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestEntityToJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use an *EntityToJson* instance to convert a list of entities into a JSON file.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the translator");
		System.out.println("EntityValueTranslator translator = new EntityValueTranslator();");
		System.out.println("// Initialize the translator");
		System.out.println("translator.initialize();");
		System.out.println("// Create the EntityToJson instance");
		System.out.println("EntityToJson convertor = new EntityToJson();");
		System.out.println("// Convert the entities to JSON");
		System.out.println("JsFile json = convertor.getJsonForEntities(translator.getEntities(),\"optionalContextSymbol\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockEntityValueTranslator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestEntityToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(MockEntityValueTranslator.class));
		System.out.println(" * " + getTester().getLinkForClass(EntityToJson.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a sample of the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		EntityValueTranslator t = (EntityValueTranslator) getTester().getMockedObject(MockEntityValueTranslator.class.getName());
		EntityToJson convertor = new EntityToJson();
		Date started = new Date();
		JsFile json = convertor.getJsonForEntities(t.getEntities(),"");
		assertEqual(json.rootElement.children.size(),1,"The number of children does not match expectation");
		if (json.rootElement.children.size()>0) {
			System.out.println("Converting " + json.rootElement.children.get(0).children.size() + " entity values took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			assertEqual(json.rootElement.children.get(0).children.size(),SEQUENCE_ELEMENTS,"The number of sequence elements does not match expectation");
			showJsonSample(json,10);
		}
	}
	
	protected void showJsonSample(JsFile json,int num) {
		JsFile sample = new JsFile();
		sample.rootElement = new JsElem();
		sample.rootElement.children.add(new JsElem(json.rootElement.children.get(0).name,true));
		int i = 0;
		for (JsElem elem: json.rootElement.children.get(0).children) {
			sample.rootElement.children.get(0).children.add(elem);
			i++;
			if (i>=num) {
				break;
			}
		}
		System.out.println("");
		System.out.println("Sample JSON;");
		System.out.println("" + sample.toStringBuilderReadFormat());
	}
}
