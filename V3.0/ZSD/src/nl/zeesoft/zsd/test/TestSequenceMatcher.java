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
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen."));
			
			sequence = new ZStringSymbolParser("Wat kost dat?");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen."));

			context = sm.classify(sequence);
			testSequenceMatch(sm,sequence,context,false,new ZStringSymbolParser("Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas."));

			sequence = new ZStringSymbolParser("geld over?");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken."));

			sequence = new ZStringSymbolParser("Waar kan ik mijn transacties zien?");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen."));
			
			sequence = new ZStringSymbolParser("overboeken");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk."));

			sequence = new ZStringSymbolParser("Hypotheek berekenen");
			testSequenceMatch(sm,sequence,"",false,null);
			testSequenceMatch(sm,sequence,"",true,new ZStringSymbolParser("Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent."));
			
			sequence = new ZStringSymbolParser("Fraude");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("En belangrijker: hoe kunt voorkomen slachtoffer te worden van CEO Fraude? [OUTPUT] Criminelen kunnen veel informatie over bedrijven op internet vinden. Check daarom regelmatig wat voor informatie u over uw bedrijf en de medewerkers online heeft staan. Maak het criminelen zo moeilijk mogelijk om online namen, functies en emailadressen te stelen."));
			testSequenceMatch(sm,sequence,"",true,new ZStringSymbolParser("Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten."));
			
			sequence = new ZStringSymbolParser("Heeft de abn amro rechtsbijstandverzekering");
			testSequenceMatch(sm,sequence,"",false,new ZStringSymbolParser("Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering."));
			testSequenceMatch(sm,sequence,"",true,new ZStringSymbolParser("Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering."));
			
			sequence = new ZStringSymbolParser("Wat kost dat?");
			List<SequenceMatcherResult> matches = null;
			double t = 0D;
			
			System.out.println();
			t = 0.7D;
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
			t = 0.9D;
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
