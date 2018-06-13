package nl.zeesoft.zsmc.test;

import java.util.Date;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.SequenceMatcher;

public class TestSequenceMatcher extends TestObject {
	public static final String QNA_FILE_NAME = "resources/nl-qna.txt";
	
	public TestSequenceMatcher(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSequenceMatcher(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *SequenceMatcher* to find a match for a sequence.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SequenceMatcher");
		System.out.println("SequenceMatcher matcher = new SequenceMatcher();");
		System.out.println("// Initialize the SequenceMatcher");
		System.out.println("matcher.initialize(\"tab-separated-file-name.tsv\");");
		System.out.println("// Use SequenceMatcher to correct a word");
		System.out.println("ZStringSymbolParserString match = matcher.match(new ZStringSymbolParser(\"some sequence\"),\"optionalContextSymbol\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSequenceMatcher.class));
		System.out.println(" * " + getTester().getLinkForClass(SequenceMatcher.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it takes to initialize the classifier  ");
		System.out.println(" * The matches found for a set of input sequences and how long it took to find them  ");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		SequenceMatcher sm = new SequenceMatcher();
		String err = sm.initialize(QNA_FILE_NAME);
		assertEqual(err.length(),0,"Reading the file produced an unexpected error");
		if (err.length()==0) {
			System.out.println("Initializing the SequenceMatcher took: " + ((new Date()).getTime() - started.getTime()) + " ms");

			assertEqual(sm.getContextSequences().size(),82,"The total number of sequences does not match expectation");
			assertEqual(sm.getContextSequences().get("").size(),6362,"The total number of context sequences does not match expectation");

			ZStringSymbolParser sequence = new ZStringSymbolParser("Wat kost dat?");
			testSequenceMatch(sm,sequence,"",new ZStringSymbolParser("Zorg thuis : wat kost dat? De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen."));
			
			String context = sm.classify(sequence);
			testSequenceMatch(sm,sequence,context,new ZStringSymbolParser("Hoe kan dat? In onze systemen is dan niet vastgelegd dat deze rekening ook bij u hoort. Ga langs bij een van onze bankkantoren om de persoonsgegevens te laten aanpassen."));

			sequence = new ZStringSymbolParser("Wat kost");
			testSequenceMatch(sm,sequence,context,new ZStringSymbolParser("Wat kost een buitenlandse betaling? Bekijk het overzicht met de tarieven."));
			
			sequence = new ZStringSymbolParser("geld over?");
			testSequenceMatch(sm,sequence,"",new ZStringSymbolParser("Alle rekeningen betaald en geld over? Sparen ligt voor de hand , maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen , hypotheek aflossen , pensioen aanvullen en schenken."));

			sequence = new ZStringSymbolParser("Waar kan ik mijn transacties zien?");
			testSequenceMatch(sm,sequence,"",new ZStringSymbolParser("Waar kan ik mijn transacties inzien? Via Mijn ICS Business kunt u online uw transacties , uw limiet , het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen."));
		}
	}
	
	private void testSequenceMatch(SequenceMatcher sm, ZStringSymbolParser sequence, String context, ZStringSymbolParser expectedMatch) {
		System.out.println();
		Date started = new Date();
		ZStringSymbolParser match = sm.match(sequence,context);
		String input = sequence.toString();
		if (context.length()>0) {
			input += " (" + context + ")";
		}
		System.out.println(input + " -> " + match);
		System.out.println("Matching the input sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		assertEqual(match,expectedMatch,"The match sequence does not match expectation");
	}
}
