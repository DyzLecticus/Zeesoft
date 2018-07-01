package nl.zeesoft.zsd.test;

import java.util.Date;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.EntityToJson;
import nl.zeesoft.zsd.EntityValueTranslator;

public class TestEntityToJson extends TestObject {
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
		System.out.println("// Create the EntityValueTranslator");
		System.out.println("EntityValueTranslator translator = new EntityValueTranslator();");
		System.out.println("// Initialize the EntityValueTranslator");
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
		System.out.println("Converting " + json.rootElement.children.size() + " entity values took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		assertEqual(json.rootElement.children.size(),332223,"The number of children does not match expectation");
	}
}
