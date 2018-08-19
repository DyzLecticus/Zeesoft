package nl.zeesoft.zsd.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SequenceMatcher;
import nl.zeesoft.zsd.sequence.SequenceMatcherResult;

public class TestSequenceMatcher extends TestObject {
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
		System.out.println("// Use SequenceMatcher to find a matching sequence");
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
		System.out.println(" * The matches found for a set of input sequences and how long it takes to find them  ");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		SequenceMatcher sm = new SequenceMatcher();
		ZStringBuilder err = sm.initialize(TestSequenceClassifier.QNA_FILE_NAME);
		assertEqual(err.length(),0,"Reading the file produced an unexpected error");
		if (err.length()==0) {
			System.out.println("Initializing the SequenceMatcher took: " + ((new Date()).getTime() - started.getTime()) + " ms");

			assertEqual(sm.getKnownSequences().size(),82,"The total number of sequences does not match expectation");
			assertEqual(sm.getKnownSequences().get("").size(),6362,"The total number of context sequences does not match expectation");

			ZStringSymbolParser sequence = null;
			String context = "";

			sequence = new ZStringSymbolParser("Heb");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Heb ik een pensioengat? [OUTPUT] Het pensioen dat u later krijgt, is vaak minder dan het inkomen dat u nu verdient. Terwijl uw vaste lasten misschien gelijk blijven. Als u na uw pensioen minder dan 70% van uw laatstverdiende salaris ontvangt, heeft u een 'pensioengat'."));
			
			sequence = new ZStringSymbolParser("Wat kost dat?");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen."));

			context = sm.classify(sequence);
			testSequenceMatch(sm,sequence,context,false,new ZStringSymbolParser("Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas."));

			sequence = new ZStringSymbolParser("geld over?");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken."));

			sequence = new ZStringSymbolParser("Waar kan ik mijn transacties zien?");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen."));
			
			sequence = new ZStringSymbolParser("overboeken");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Doet de bank of verzekeraar langer over het overboeken? [OUTPUT] Dan krijgt u een rentevergoeding."));

			sequence = new ZStringSymbolParser("Hypotheek berekenen");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Uw Aflossingsvrije Hypotheek wijzigen naar een Annuïteiten Hypotheek of een Lineaire Hypotheek? [OUTPUT] U betaalt dan elke maand een deel van uw hypotheek terug. Zo weet u zeker dat u aan het einde van de looptijd uw lening helemaal heeft terugbetaald. Uw einddatum en rentecontract wijzigen hier niet door."));
			
			sequence = new ZStringSymbolParser("Fraude");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen."));
			
			sequence = new ZStringSymbolParser("Heeft de abn amro rechtsbijstandverzekering");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering."));
			testSequenceMatch(sm,sequence,"",true,new ZStringSymbolParser("Geldt mijn ABN AMRO Rechtsbijstandverzekering ook in het buitenland? [OUTPUT] Dit is afhankelijk van welke modules u heeft afgesloten. Bekijk uw polisblad voor uw dekkingen of bekijk de modules."));
			
			sequence = new ZStringSymbolParser("Wat kost dat?");
			List<SequenceMatcherResult> matches = null;
			double t = 0D;
			
			System.out.println();
			t = 0.33D;
			matches = sm.getMatches(sequence,"",false,t);
			System.out.println("Matches for sequence: '" + sequence + "', threshold: " + t);
			for (SequenceMatcherResult match: matches) {
				String seq = match.result.sequence.toString();
				if (seq.length()>60) {
					seq = seq.substring(0,60) + "[ ...]";
				}
				System.out.println("'" + seq + "': " + match.prob + " / " + match.probNormalized);
			}
			assertEqual(matches.size(),15,"The matcher did not return the expected number of sequences");
			
			System.out.println();
			t = 0.5D;
			matches = sm.getMatches(sequence,"",false,t);
			System.out.println("Matches for sequence: '" + sequence + "', threshold: " + t);
			for (SequenceMatcherResult match: matches) {
				String seq = match.result.sequence.toString();
				if (seq.length()>60) {
					seq = seq.substring(0,60) + "[ ...]";
				}
				System.out.println("'" + seq + "': " + match.prob + " / " + match.probNormalized);
			}
			assertEqual(matches.size(),1,"The matcher did not return the expected number of sequences");
			
			System.out.println();
			sequence = new ZStringSymbolParser("Wat kost de Betaalpas?");
			t = 0.25D;
			matches = sm.getMatches(sequence,"",false,t);
			System.out.println("Matches for sequence: '" + sequence + "', threshold: " + t);
			for (SequenceMatcherResult match: matches) {
				String seq = match.result.sequence.toString();
				if (seq.length()>60) {
					seq = seq.substring(0,60) + "[ ...]";
				}
				System.out.println("'" + seq + "': " + match.prob + " / " + match.probNormalized);
			}
			assertEqual(matches.size(),4,"The matcher did not return the expected number of sequences");
		}
	}
	
	private void testSequenceMatch(SequenceMatcher sm, ZStringSymbolParser sequence, String context, boolean caseInsensitive, ZStringSymbolParser expectedMatch) {
		System.out.println();
		Date started = new Date();
		ZStringSymbolParser match = sm.match(sequence,context,caseInsensitive);
		String input = "'" + sequence.toString();
		if (context.length()>0 || caseInsensitive) {
			input += "' (";
			if (context.length()>0) {
				input += context;
			}
			if (caseInsensitive) {
				if (context.length()>0) {
					input += " ";
				}
				input += "case insensitive";
			}
			input += ")";
		} else {
			input += "'";
		}
		System.out.println(input + " -> '" + match + "'");
		System.out.println("Matching the input sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		if (expectedMatch==null) {
			if (match!=null) {
				assertEqual(match.toString(),"null","The match sequence does not match expectation");
			}
		} else if (match!=null) {
			assertEqual(match,expectedMatch,"The match sequence does not match expectation");
		} else {
			assertEqual("null",expectedMatch.toString(),"The match sequence does not match expectation");
		}
	}
}
