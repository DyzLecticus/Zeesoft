Zeesoft Smart Dialogs
=====================
The Zeesoft Smart Dialogs (ZSD) is an open source library for Java application development.  

It provides support for;  
 * Context sensitive symbolic corrections; word spelling corrections  
 * Sequence context classification; sentence context classification  
 * Context sensitive sequence matching; find matching sentences  
 * Symbolic entity value translation; find variable values  
 * Dialog response generation; create a smart and scalable chatbot  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSD/releases/zsd-0.9.0.zip) to download the latest ZSD release (version 0.9.0).  
All ZSD releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSD/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSD](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/ZSD.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zsd.test.TestTsvToJson
---------------------------------
This test shows how to use a *TsvToJson* instance to convert a TSV file into a JSON file.
Both formats can be used to initialize *Analyzer* instances.

**Example implementation**  
~~~~
// Create the TsvToJson instance
TsvToJson convertor = new TsvToJson();
// Convert some TSV data
JsFile json = convertor.parseTsv(new ZStringBuilder("Question\tAnswer\tContext\nWhat?\tThat!\tWTF\n"));
~~~~

Class references;  
 * [TestTsvToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestTsvToJson.java)
 * [TsvToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/util/TsvToJson.java)
 * [Analyzer](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/sequence/Analyzer.java)

**Test output**  
The output of this test shows the input TSV data and the converted JSON.  
~~~~
Question	Answer	Context
What is that?	It is a plant!	contextWhatQuestions
Why is that?	Because it can!	contextWhyQuestions

{
  "sequences": [
    {
      "input": "What is that?",
      "output": "It is a plant!",
      "context": "contextWhatQuestions"
    },
    {
      "input": "Why is that?",
      "output": "Because it can!",
      "context": "contextWhyQuestions"
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestSequencePreprocessor
--------------------------------------------
This test shows how to use a *SequencePreprocessor* instance preprocess a sequence.

**Example implementation**  
~~~~
// Create the preprocessor
SequencePreprocessor sp = new SequencePreprocessor();
// Initialize the preprocessor
sp.initialize();
// Use the processor to process a sequence
ZStringSymbolParser sequence sp.process(new ZStringSymbolParser("Symbol sequence"));
~~~~

Class references;  
 * [TestSequencePreprocessor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSequencePreprocessor.java)
 * [SequencePreprocessor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SequencePreprocessor.java)

**Test output**  
The output of this test shows some sequences and their corresponding preprocessed form.  
~~~~
<- 'I'd have done it but it wouldn't have been fun'
-> 'I would have done it but it would not have been fun.'

<- 'Ik moet 's morgensvroeg opstaan'
-> 'Ik moet smorgens vroeg opstaan.'
~~~~

nl.zeesoft.zsd.test.TestSymbolCorrector
---------------------------------------
This test shows how to use the *SymbolCorrector* to correct word spelling.

**Example implementation**  
~~~~
// Create the SymbolCorrector
SymbolCorrector corrector = new SymbolCorrector();
// Initialize the SymbolCorrector
corrector.initialize(new ZStringSymbolParser("Some text containing correctly spelled words and complete meaningfull sentences."));
// Use SymbolCorrector to correct a word
String correction = corrector.correct("contaning");
~~~~

Class references;  
 * [TestSymbolCorrector](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSymbolCorrector.java)
 * [SymbolCorrector](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SymbolCorrector.java)

**Test output**  
The output of this test shows the number of generated variations and corrections for certain words.
~~~~
Initializing the SymbolCorrector took: 16 ms
Single variations for 'a'; 79
Single variations for 'an'; 133
Single variations for 'the'; 186
Single variations for 'piza'; 239
Double variations for 'stfu'; 5
Double variations for 'pizza'; 306

Correction for 'pizzas'; pizza
Correction for 'piza'; pizza
Correction for 'stuf'; stuff
Correction for 'staf'; stuff
Correction for 'coolset'; coolest
Correction for 'klevin'; kelvin
Correction for 'klevni'; kelvin
Correction for 'tenfold'; tenfold
Correction for 'maus'; mouse
Correction for 'excpton'; exception
Correction for 'amzaingg'; amazing
Correction for 'antidisestablishmantarianims'; antidisestablishmentarianism
Average correction time: 207 ms
~~~~

nl.zeesoft.zsd.test.TestSequenceClassifier
------------------------------------------
This test shows how to use the *SequenceClassifier* to classify a sequence.

**Example implementation**  
~~~~
// Create the SequenceClassifier
SequenceClassifier classifier = new SequenceClassifier();
// Initialize the SequenceClassifier
classifier.initialize("tab-separated-file-name.tsv");
// Use SequenceClassifier to classify a sequence
String context = classifier.classify(new ZStringSymbolParser("some sequence"));
~~~~

Class references;  
 * [TestSequenceClassifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSequenceClassifier.java)
 * [SequenceClassifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SequenceClassifier.java)

**Test output**  
The output of this test shows;  
 * The time it takes to initialize the classifier  
 * The classification results including the time it takes, for a set of input sequences  
~~~~
Initializing the SequenceClassifier took: 2825 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 0 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 0 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 0 ms

Context probabilities for 'Wat kost dat?', threshold: 0.5
'nlPriveKinderenengeldzaken': 0.042902821500088964 / 1.0
'nlPrivatebankingUwvermogen': 0.028009059187044 / 0.6528488851714781
'nlPrivatebankingBetalen': 0.023943110276929132 / 0.5580777543239082
'nlPrivePensioen': 0.02363316176222444 / 0.5508533223665822
'nlPriveBeleggen': 0.022273672816980207 / 0.5191656874346602

Context probabilities for 'Wat kost dat?', threshold: 0.7
'nlPriveKinderenengeldzaken': 0.042902821500088964 / 1.0
~~~~

nl.zeesoft.zsd.test.TestSequenceMatcher
---------------------------------------
This test shows how to use the *SequenceMatcher* to find a match for a sequence.

**Example implementation**  
~~~~
// Create the SequenceMatcher
SequenceMatcher matcher = new SequenceMatcher();
// Initialize the SequenceMatcher
matcher.initialize("tab-separated-file-name.tsv");
// Use SequenceMatcher to find a matching sequence
ZStringSymbolParserString match = matcher.match(new ZStringSymbolParser("some sequence"),"optionalContextSymbol");
~~~~

Class references;  
 * [TestSequenceMatcher](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSequenceMatcher.java)
 * [SequenceMatcher](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SequenceMatcher.java)

**Test output**  
The output of this test shows;  
 * The time it takes to initialize the classifier  
 * The matches found for a set of input sequences and how long it takes to find them  
~~~~
Initializing the SequenceMatcher took: 5564 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over , dan hoef je niets te regelen.'
Matching the input sequence took: 32 ms

'Wat kost dat?' -> 'Zorg thuis : wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 47 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO , betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 0 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand , maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen , hypotheek aflossen , pensioen aanvullen en schenken.'
Matching the input sequence took: 31 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties , uw limiet , het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 62 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 16 ms

'Hypotheek berekenen' -> 'null'
Matching the input sequence took: 0 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 31 ms

'Fraude' -> 'En belangrijker : hoe kunt voorkomen slachtoffer te worden van CEO Fraude? [OUTPUT] Criminelen kunnen veel informatie over bedrijven op internet vinden. Check daarom regelmatig wat voor informatie u over uw bedrijf en de medewerkers online heeft staan. Maak het criminelen zo moeilijk mogelijk om online namen , functies en emailadressen te stelen.'
Matching the input sequence took: 16 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder ( bijvoorbeeld de CEO of de CFO ) uit uw organisatie , om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 15 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 63 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 156 ms

Matches for sequence: 'Wat kost dat?', threshold: 0.7
'Zorg thuis : wat kost dat? [OUTPUT] De meeste mensen oriënte[ ...]': 0.1308245542490438 / 1.0
'Lees ook mijn blog Wat kost uw kind? [OUTPUT] Hiervoor zijn [ ...]': 0.11600853095056579 / 0.88674891052735
'Op de vraag ' Wat kost een kind? [OUTPUT] Twee kinderen kost[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost de LEI? [OUTPUT] Het jaar waarin u de LEI heeft aan[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost een buitenlandse betaling? [OUTPUT] Bekijk het over[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost een studie? [OUTPUT] De enige hulpmiddelen die de o[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost een uitgebreid pensioenadvies? [OUTPUT] Voor een ui[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost een uitvaart? [OUTPUT] Elk jaar stuurt ABN AMRO Lev[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost het en waarop moet u letten? [OUTPUT] U wordt een d[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost het om ABN AMRO te bellen? [OUTPUT] Als u belt naar[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost het? [OUTPUT] Voor dit mandaat betaalt u kosten. De[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost studeren? [OUTPUT] Plus natuurlijk kosten voor bijv[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost uw kind en hoe zorgt u ervoor dat het later zijn of[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost uw kind? [OUTPUT] Zo heeft u altijd overzicht , waa[ ...]': 0.11600853095056579 / 0.88674891052735

Matches for sequence: 'Wat kost dat?', threshold: 0.9
'Zorg thuis : wat kost dat? [OUTPUT] De meeste mensen oriënte[ ...]': 0.1308245542490438 / 1.0
~~~~

nl.zeesoft.zsd.test.TestEntityValueTranslator
---------------------------------------------
This test shows how to use the *EntityValueTranslator* to translate a sequence to and from internal values.

**Example implementation**  
~~~~
// Create the EntityValueTranslator
EntityValueTranslator translator = new EntityValueTranslator();
// Initialize the EntityValueTranslator
translator.initialize();
// Use EntityValueTranslator to translate a sequence
ZStringSymbolParser translated = translator.translateToInternalValues(new ZStringSymbolParser("some sequence"));
ZStringSymbolParser retranslated = translator.translateToExternalValues(translated);
~~~~

This test uses the *MockEntityValueTranslator*.

Class references;  
 * [TestEntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestEntityValueTranslator.java)
 * [MockEntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/MockEntityValueTranslator.java)
 * [EntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/EntityValueTranslator.java)

**Test output**  
The output of this test shows;  
 * The time it takes to initialize the translator  
 * The translation results including the time it takes, for a set of input sequences  
~~~~
Initializing the EntityValueTranslator took: 1518 ms

Sequence: 'Eat three donuts at 9:00 or count to 110'
Translating the sequence took: 16 ms
Translation: 'UNI_ABC:Eat ENG_NUM:3|UNI_ABC:three UNI_ABC:donuts UNI_ABC:at UNI_TIM:09:00:00 UNI_ABC:or UNI_ABC:count UNI_ABC:to UNI_NUM:110'
Retranslating the sequence took: 0 ms
Retranslation: 'Eat three donuts at 09:00:00 or count to 110'

Sequence: 'Eet drie donuts om 9:00 of tel tot 110'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:Eet NLD_NUM:3|UNI_ABC:drie UNI_ABC:donuts UNI_ABC:om UNI_TIM:09:00:00 UNI_ABC:of UNI_ABC:tel UNI_ABC:tot UNI_NUM:110'
Retranslating the sequence took: 0 ms
Retranslation: 'Eet drie donuts om 09:00:00 of tel tot 110'

Sequence: 'I finished twohundredandtwentyfourth or 225th'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:I UNI_ABC:finished ENG_ORD:224|UNI_ABC:twohundredandtwentyfourth UNI_ABC:or ENG_OR2:225'
Retranslating the sequence took: 0 ms
Retranslation: 'I finished twohundredandtwentyfourth or 225th'

Sequence: 'Ik ben tweehonderdvierentwintigste geworden'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:Ik UNI_ABC:ben NLD_ORD:224|UNI_ABC:tweehonderdvierentwintigste|NLD_NAM:firstName:UNI_ABC:Tweehonderdvierentwintigste UNI_ABC:geworden|NLD_NAM:lastName:UNI_ABC:Geworden'
Retranslating the sequence took: 0 ms
Retranslation: 'Ik ben tweehonderdvierentwintigste geworden'

Sequence: 'februari march october december'
Translating the sequence took: 0 ms
Translation: 'NLD_MNT:2|UNI_ABC:februari ENG_MNT:3|UNI_ABC:march ENG_MNT:10|UNI_ABC:october ENG_MNT:12|NLD_MNT:12|UNI_ABC:december'
Retranslating the sequence took: 0 ms
Retranslation: 'februari march october december'

Sequence: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'
Translating the sequence took: 0 ms
Translation: 'ENG_DUR:33:41 UNI_MTH:D NLD_DUR:33:41'
Retranslating the sequence took: 0 ms
Retranslation: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'

Sequence: 'yesterday OR today OR the 1st of october'
Translating the sequence took: 0 ms
Translation: 'ENG_DAT:2018-07-15|UNI_ABC:yesterday UNI_ABC:OR ENG_DAT:2018-07-16|UNI_ABC:today UNI_ABC:OR ENG_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'july fifteenth twothousandeighteen OR july sixteenth twothousandeighteen OR october first twothousandeighteen'

Sequence: 'gisteren OF vandaag OF 1 oktober'
Translating the sequence took: 0 ms
Translation: 'NLD_DAT:2018-07-15|UNI_ABC:gisteren UNI_ABC:OF NLD_DAT:2018-07-16|UNI_ABC:vandaag UNI_ABC:OF NLD_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'vijftien juli tweeduizendachttien OF zestien juli tweeduizendachttien OF een oktober tweeduizendachttien'

Sequence: 'twelve o'clock OR five minutes to nine OR ten past one in the morning'
Translating the sequence took: 0 ms
Translation: 'ENG_TIM:12:00:00 UNI_ABC:OR ENG_TIM:08:55:00 UNI_ABC:OR ENG_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twelve o'clock OR fiftyfive past eight OR ten past one in the morning'

Sequence: 'twaalf uur OF vijf minuten voor negen OF tien over een sochtends'
Translating the sequence took: 0 ms
Translation: 'NLD_TIM:12:00:00|NLD_DUR:12:00 UNI_ABC:OF NLD_TIM:08:55:00 UNI_ABC:OF NLD_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends'

Sequence: 'My name is Andrew from the Sea'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:My UNI_ABC:name UNI_ABC:is UNI_ABC:Andrew|ENG_NAM:firstName:UNI_ABC:Andrew ENG_PRE:4|ENG_NAM:preposition:ENG_PRE:4 UNI_ABC:Sea|ENG_NAM:lastName:UNI_ABC:Sea'
Retranslating the sequence took: 0 ms
Retranslation: 'My name is Andrew from the Sea'

Sequence: 'Mijn naam is Andre van der Zee'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:is UNI_ABC:Andre|NLD_NAM:firstName:UNI_ABC:Andre NLD_PRE:3|NLD_NAM:preposition:NLD_PRE:3 UNI_ABC:Zee|NLD_NAM:lastName:UNI_ABC:Zee'
Retranslating the sequence took: 0 ms
Retranslation: 'Mijn naam is Andre van der Zee'

Sequence: 'Hoe heet jij? gekke henkie'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:Hoe UNI_ABC:heet UNI_ABC:jij ? UNI_ABC:gekke|NLD_NAM:firstName:UNI_ABC:Gekke UNI_ABC:henkie|NLD_NAM:lastName:UNI_ABC:Henkie'
Retranslating the sequence took: 0 ms
Retranslation: 'Hoe heet jij? gekke henkie'

Sequence: 'gekste der henkies is mijn naam'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies UNI_ABC:is UNI_ABC:mijn UNI_ABC:naam'
Retranslating the sequence took: 0 ms
Retranslation: 'gekste der henkies is mijn naam'

Sequence: 'to Germany or France'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:to ENG_CNT:DE|UNI_ABC:Germany UNI_ABC:or ENG_CNT:FR|UNI_ABC:France'
Retranslating the sequence took: 0 ms
Retranslation: 'to Germany or France'

Sequence: 'naar Duitsland of Frankrijk'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:naar NLD_CNT:DE|UNI_ABC:Duitsland UNI_ABC:of NLD_CNT:FR|UNI_ABC:Frankrijk'
Retranslating the sequence took: 0 ms
Retranslation: 'naar Duitsland of Frankrijk'

Sequence: 'You asshole'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:You ENG_PRF:1|UNI_ABC:asshole'
Retranslating the sequence took: 0 ms
Retranslation: 'You asshole'

Sequence: 'Jij klootzak'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:Jij NLD_PRF:2|UNI_ABC:klootzak'
Retranslating the sequence took: 0 ms
Retranslation: 'Jij klootzak'

Sequence: 'Can I book a room for 5 people?'
Translating the sequence took: 0 ms
Translation: 'UNI_ABC:Can UNI_ABC:I UNI_ABC:book UNI_ABC:a|ENG_NAM:firstName:UNI_ABC:A UNI_ABC:room|ENG_NAM:lastName:UNI_ABC:Room UNI_ABC:for UNI_NUM:5 UNI_ABC:people ?'
Retranslating the sequence took: 16 ms
Retranslation: 'Can I book a room for 5 people?'

Sequence: 'ten times five'
Translating the sequence took: 0 ms
Translation: 'ENG_NUM:10|NLD_PRE:8|UNI_ABC:ten ENG_MTH:M|UNI_ABC:times ENG_NUM:5|UNI_ABC:five'
Retranslating the sequence took: 0 ms
Retranslation: 'ten multiplied by five'

Sequence: 'tien keer vijf'
Translating the sequence took: 0 ms
Translation: 'NLD_NUM:10|UNI_ABC:tien NLD_MTH:M|UNI_ABC:keer NLD_NUM:5|UNI_ABC:vijf'
Retranslating the sequence took: 0 ms
Retranslation: 'tien vermenigvuldigd met vijf'
~~~~

nl.zeesoft.zsd.test.TestBaseConfigurationToJson
-----------------------------------------------
This test shows how to convert a *BaseConfiguration* instance to JSON.

**Example implementation**  
~~~~
// Create the configuration
BaseConfiguration bc = new BaseConfiguration();
// Convert the configuration to JSON
JsFile json = bc.toJson();
~~~~

Class references;  
 * [TestBaseConfigurationToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestBaseConfigurationToJson.java)
 * [BaseConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/BaseConfiguration.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
{
  "name": "Test",
  "primaryLanguage": "ENG",
  "baseDir": "testBase/",
  "extendDir": "testExtend/",
  "overrideDir": "testOverride/",
  "maxMsInterpretPerSymbol": 111,
  "maxMsInterpretPerSequence": 2222,
  "maxMsDialogPerSequence": 1111,
  "supportedLanguages": [
    {
      "code": "ENG",
      "alphabet": "abcdefghijklmnopqrstuvwxyz",
      "supportedMasterContexts": [
        "Generic",
        "Room"
      ]
    },
    {
      "code": "NLD",
      "alphabet": "àáçèéêëïóöúüabcdefghijklmnopqrstuvwxyz",
      "supportedMasterContexts": [
        "Generic",
        "Room"
      ]
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestEntityToJson
------------------------------------
This test shows how to use an *EntityToJson* instance to convert a list of entities into a JSON file.

**Example implementation**  
~~~~
// Create the translator
EntityValueTranslator translator = new EntityValueTranslator();
// Initialize the translator
translator.initialize();
// Create the EntityToJson instance
EntityToJson convertor = new EntityToJson();
// Convert the entities to JSON
JsFile json = convertor.getJsonForEntities(translator.getEntities(),"optionalContextSymbol");
~~~~

This test uses the *MockEntityValueTranslator*.

Class references;  
 * [TestEntityToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestEntityToJson.java)
 * [MockEntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/MockEntityValueTranslator.java)
 * [EntityToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/util/EntityToJson.java)

**Test output**  
The output of this test shows a sample of the converted JSON.  
~~~~
Converting 332871 entity values took: 297 ms

Sample JSON;
{
  "sequences": [
    {
      "input": "My name is {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "I am {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "What is your name? {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "Who are you? {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "What is your name? My name is {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "Who are you? My name is {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "What is your name? I am {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "Who are you? I am {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "{firstName} {preposition} {lastName} is my name.",
      "context": "NAM"
    },
    {
      "input": "{firstName} {preposition} {lastName} am I.",
      "context": "NAM"
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestDialogToJson
------------------------------------
This test shows how to use a *DialogToJson* instance to convert a list of dialogs into a JSON file.

**Example implementation**  
~~~~
// Create dialog set
DialogSet ds = new DialogSet();
// Initialize the dialog set
ds.initialize();
// Create the DialogToJson instance
DialogToJson convertor = new DialogToJson();
// Convert the dialogs to JSON
JsFile json = convertor.getJsonForDialogs(ds.getDialogs());
~~~~

This test uses the *MockEntityValueTranslator*.

Class references;  
 * [TestDialogToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogToJson.java)
 * [DialogToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/util/DialogToJson.java)

**Test output**  
The output of this test shows a sample of the converted JSON.  
~~~~
Converting 2747 dialog examples took: 266 ms

Sample JSON;
{
  "sequences": [
    {
      "input": "Hello.",
      "context": "ENG"
    },
    {
      "input": "Hello!",
      "context": "ENG"
    },
    {
      "input": "Hi.",
      "context": "ENG"
    },
    {
      "input": "Hi!",
      "context": "ENG"
    },
    {
      "input": "Good morning.",
      "context": "ENG"
    },
    {
      "input": "Good morning!",
      "context": "ENG"
    },
    {
      "input": "Good afternoon.",
      "context": "ENG"
    },
    {
      "input": "Good afternoon!",
      "context": "ENG"
    },
    {
      "input": "Good evening.",
      "context": "ENG"
    },
    {
      "input": "Good evening!",
      "context": "ENG"
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestDialogSetToJson
---------------------------------------
This test shows how to use a *DialogSetToJson* instance to convert a list of dialogs into a JSON file.

**Example implementation**  
~~~~
// Create the dialog set
DialogSet ds = new DialogSet();
// Initialize the dialog set
ds.initialize();
// Create the DialogToJson instance
DialogSetToJson convertor = new DialogSetToJson();
// Convert the english dialogs to JSON
JsFile json = convertor.getJsonForDialogs(ds,"Optional language code"));
~~~~

This test uses the *MockEntityValueTranslator*.

Class references;  
 * [TestDialogSetToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogSetToJson.java)
 * [DialogSetToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/util/DialogSetToJson.java)

**Test output**  
The output of this test shows a sample of the converted JSON.  
~~~~
Converting 6 dialogs took: 0 ms

Sample JSON;
{
  "dialogs": [
    {
      "language": "ENG",
      "masterContext": "Generic",
      "context": "Handshake",
      "handler": "nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler",
      "examples": [
        {
          "input": "Hello.",
          "output": "Hello. My name is {selfName}. What is your name?"
        },
        {
          "input": "Hello!",
          "output": "Hello. My name is {selfName}. What is your name?"
        },
        {
          "input": "Hi.",
          "output": "Hi. My name is {selfName}. What is your name?"
        },
        {
          "input": "Hi!",
          "output": "Hi. My name is {selfName}. What is your name?"
        },
        {
          "input": "Good morning.",
          "output": "Good morning. My name is {selfName}. What is your name?"
        },
        {
          "input": "Good morning!",
          "output": "Good morning. My name is {selfName}. What is your name?"
        },
        {
          "input": "Good afternoon.",
          "output": "Good afternoon. My name is {selfName}. What is your name?"
        },
        {
          "input": "Good afternoon!",
          "output": "Good afternoon. My name is {selfName}. What is your name?"
        },
        {
          "input": "Good evening.",
          "output": "Good evening. My name is {selfName}. What is your name?"
        },
        {
          "input": "Good evening!",
          "output": "Good evening. My name is {selfName}. What is your name?"
        },
        {
          "input": "What is your name?",
          "output": "My name is {selfName}. What is your name?"
        },
        {
          "input": "Who are you?",
          "output": "My name is {selfName}. What is your name?"
        },
        {
          "input": "My name is {firstName} {preposition} {lastName}."
        },
        {
          "input": "I am {firstName} {preposition} {lastName}."
        },
        {
          "input": "What is your name? {firstName} {preposition} {lastName}."
        },
        {
          "input": "Who are you? {firstName} {preposition} {lastName}."
        },
        {
          "input": "What is your name? My name is {firstName} {preposition} {lastName}."
        },
        {
          "input": "Who are you? My name is {firstName} {preposition} {lastName}."
        },
        {
          "input": "What is your name? I am {firstName} {preposition} {lastName}."
        },
        {
          "input": "Who are you? I am {firstName} {preposition} {lastName}."
        },
        {
          "input": "{firstName} {preposition} {lastName} is my name."
        },
        {
          "input": "{firstName} {preposition} {lastName} am I."
        },
        {
          "input": "My name is {firstName} {lastName}."
        },
        {
          "input": "I am {firstName} {lastName}."
        },
        {
          "input": "What is your name? {firstName} {lastName}."
        },
        {
          "input": "Who are you? {firstName} {lastName}."
        },
        {
          "input": "What is your name? My name is {firstName} {lastName}."
        },
        {
          "input": "Who are you? My name is {firstName} {lastName}."
        },
        {
          "input": "What is your name? I am {firstName} {lastName}."
        },
        {
          "input": "Who are you? I am {firstName} {lastName}."
        },
        {
          "input": "{firstName} {lastName} is my name."
        },
        {
          "input": "{firstName} {lastName} am I."
        },
        {
          "input": "My name is {firstName}."
        },
        {
          "input": "I am {firstName}."
        },
        {
          "input": "What is your name? {firstName}."
        },
        {
          "input": "Who are you? {firstName}."
        },
        {
          "input": "What is your name? My name is {firstName}."
        },
        {
          "input": "Who are you? My name is {firstName}."
        },
        {
          "input": "What is your name? I am {firstName}."
        },
        {
          "input": "Who are you? I am {firstName}."
        },
        {
          "input": "{firstName} is my name."
        },
        {
          "input": "{firstName} am I."
        },
        {
          "input": "What is your firstname? {firstName}."
        },
        {
          "input": "What is your firstname? My firstname is {firstName}."
        },
        {
          "input": "{firstName} is my firstname."
        },
        {
          "input": "What is your lastname? {preposition} {lastName}."
        },
        {
          "input": "What is your lastname? My lastname is {preposition} {lastName}."
        },
        {
          "input": "{preposition} {lastName} is my lastname."
        },
        {
          "input": "What is your lastname? {lastName}."
        },
        {
          "input": "What is your lastname? My lastname is {lastName}."
        },
        {
          "input": "{lastName} is my lastname."
        }
      ],
      "variables": [
        {
          "name": "firstName",
          "type": "ABC",
          "complexName": "firstName",
          "complexType": "NAM",
          "prompts": [
            "What is your name?",
            "What is your firstname?"
          ]
        },
        {
          "name": "lastName",
          "type": "ABC",
          "complexName": "lastName",
          "complexType": "NAM",
          "prompts": [
            "What is your lastname?"
          ]
        },
        {
          "name": "nextDialog",
          "type": "ABC",
          "prompts": [
            "What can I do for you {fullName}?",
            "How can I help you {fullName}?"
          ]
        },
        {
          "name": "preposition",
          "type": "PRE",
          "complexName": "preposition",
          "complexType": "NAM"
        }
      ]
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestRequestResponseToJson
---------------------------------------------
This test shows how to convert a *DialogRequest* and *DialogResponse* objects from and to JSON respectively.

**Example implementation**  
~~~~
// Create the dialog request
DialogRequest req = new DialogRequest();
// Create the JSON
JsFile json = new JsFile();
// Convert the dialog request from JSON
req.fromJson(json);
// Create the dialog response
DialogResponse res = new DialogResponse();
// Convert the dialog response to JSON
json = res.toJson();
~~~~

Class references;  
 * [TestRequestResponseToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestRequestResponseToJson.java)
 * [DialogRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogRequest.java)
 * [DialogResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogResponse.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
Dialog request JSON;
{
  "prompt": "test prompt?",
  "input": "test input.",
  "language": "ENG",
  "masterContext": "Generic",
  "context": "Handshake",
  "appendDebugLog": true,
  "classifyLanguage": true,
  "correctInput": true,
  "classifyMasterContext": true,
  "classifyContext": true,
  "classifyContextThreshold": 0.8,
  "translateEntityValues": true,
  "translateEntityTypes": [
    "NAM"
  ],
  "matchThreshold": 0.8,
  "randomizeOutput": false,
  "dialogVariableValues": [
    {
      "name": "testVariable",
      "externalValue": "testExtVal",
      "internalValue": "testIntVal"
    }
  ]
}

Dialog response JSON;
{
  "responseLanguages": [
    {
      "symbol": "ENG",
      "prob": 0.4,
      "probNormalized": 1.0
    },
    {
      "symbol": "NLD",
      "prob": 0.2,
      "probNormalized": 0.5
    }
  ],
  "correctedInput": "Test corrected input.",
  "responseMasterContexts": [],
  "responseContexts": [],
  "entityValueTranslation": "Test entity value translation.",
  "entityValueTranslationCorrected": "Test corrected entity value translation.",
  "debugLog": "",
  "contextOutputs": [
    {
      "context": "testContext",
      "output": "Test output!",
      "prompt": "Test prompt!"
    }
  ],
  "responseDialogVariableValues": [
    {
      "name": "testVariable",
      "externalValue": "testExtVal",
      "internalValue": "testIntVal"
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestInitializer
-----------------------------------
This test shows how to use an *Initializer* instance to instantiate and initialize multiple classes simultaneously.

**Example implementation**  
~~~~
// Create the Initializer
Initializer init = new Initializer();
// Add a listener
init.addListener(this);
// Add a class to initialize
init.addClass("uniqueName",SequenceClassifier.class.getName(),"resources/nl-qna.txt");
// Start the initialization
init.start();
~~~~

Class references;  
 * [TestInitializer](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestInitializer.java)
 * [Initializer](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/initialize/Initializer.java)

**Test output**  
The output of this test shows the time it takes to initialize two objects simultaneously.  
~~~~
Initializing Translator took 2203 ms
Initializing Classifier took 4190 ms
Initialized all classes
~~~~

nl.zeesoft.zsd.test.TestInterpreterConfiguration
------------------------------------------------
This test uses a lot of JSON files to initialize an *InterpreterConfiguration* instance.

**Example implementation**  
~~~~
// Create the InterpreterConfiguration
InterpreterConfiguration config = new InterpreterConfiguration();
// Add a listener
config.addListener(this);
// Start the initialization
config.initialize();
~~~~

This test uses the *MockInterpreterConfiguration*.

Class references;  
 * [TestInterpreterConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestInterpreterConfiguration.java)
 * [InterpreterConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/interpret/InterpreterConfiguration.java)

**Test output**  
The output of this test shows the time it takes to initialize all objects simultaneously.  
~~~~
Classes:
- nl.zeesoft.zsd.SequencePreprocessor (LanguagePreprocessor) <= resources/LanguagePreprocessor.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageClassifier) <= resources/LanguageClassifier.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextENG) <= resources/LanguageMasterContextENG.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENGGeneric) <= resources/LanguageContextENGGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENGRoom) <= resources/LanguageContextENGRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextNLD) <= resources/LanguageMasterContextNLD.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLDGeneric) <= resources/LanguageContextNLDGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLDRoom) <= resources/LanguageContextNLDRoom.json
- nl.zeesoft.zsd.test.FixedDateEntityValueTranslator (EntityValueTranslator)

Initializing LanguagePreprocessor took 32 ms
Initializing LanguageContextNLDRoom took 405 ms
Initializing LanguageContextENGRoom took 485 ms
Initializing LanguageContextENGGeneric took 845 ms
Initializing LanguageContextNLDGeneric took 929 ms
Initializing LanguageMasterContextENG took 1043 ms
Initializing LanguageMasterContextNLD took 1047 ms
Initializing EntityValueTranslator took 1950 ms
Initializing LanguageClassifier took 29331 ms
Initialized all classes
~~~~

nl.zeesoft.zsd.test.TestLanguageClassifier
------------------------------------------
This test uses a large JSON dataset to test a *SequenceClassifier* instance intended to be used as a language classifier.
~~~~
'Wat is your name?' (ENG) => 'What is your name?'
'Wat is your name?' (NLD) => 'Wat is jouw kamer?'

Classified sequence: 'Wie ben jij?' -> NLD
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'Wie ben jij?' -> NLD
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'what is name?' -> ENG
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'twothousand' -> ENG
Classifying the input sequence took: 0 ms

Context probabilities for 'wat is your name?', threshold: 0.3
'ENG': 0.2240081199192735 / 1.0
'NLD': 0.07731508388040782 / 0.34514411311639104

Context probabilities for 'wat is your name?', threshold: 0.7
'ENG': 0.2240081199192735 / 1.0
~~~~

nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier
-------------------------------------------------------
This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as master context classifiers.
~~~~
Classified sequence: 'Who are you?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Who are you?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Who are you?', threshold: 0.0
'Generic': 0.37143800600183163 / 1.0
'Room': 0.09837321048650063 / 0.2648442240614859

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.38433098752695866 / 1.0
~~~~

nl.zeesoft.zsd.test.TestLanguageContextClassifier
-------------------------------------------------
This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as context classifiers.
~~~~
Classified sequence: 'What is your goal?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your goal?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your goal?', threshold: 0.0
'QuestionAndAnswer': 0.40128470536117433 / 1.0
'Handshake': 0.1525693900445177 / 0.3802023551014693

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.4077415952222868 / 1.0
'Handshake': 0.2062919143333239 / 0.5059378703339319

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.3569187153641163 / 1.0
'QuestionAndAnswer': 0.18246053449876898 / 0.5112103306564605

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 3 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.45811228395120057 / 1.0
'QuestionAndAnswer': 0.1994655112675311 / 0.4354074716075038

Classified sequence: 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.28278171083906256 / 1.0
'Handshake': 0.21082181722898374 / 0.7455284735474537
'Math': 0.1493229588714879 / 0.5280502703955665

Classified sequence: 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.2925903373858782 / 1.0
'Handshake': 0.2664545159593401 / 0.910674352201627
~~~~

nl.zeesoft.zsd.test.TestSequenceInterpreter
-------------------------------------------
This test show how to use a *SequenceInterpreter* to process an *InterpreterRequest*.

**Example implementation**  
~~~~
// Create the InterpreterConfiguration
InterpreterConfiguration config = new InterpreterConfiguration();
// Add a listener
config.addListener(this);
// Start the initialization (and wait until it's done)
config.initialize();
// Create the sequence interpreter
SequenceInterpreter interpreter = new SequenceInterpreter(config);
// Create the interpreter request
InterpreterRequest request = new InterpreterRequest("The optional question that prompted the input","The input sequence");
// Use the interpreter to process the request
InterpreterResponse response = interpreter.handleInterpreterRequest(request);
~~~~

Class references;  
 * [TestSequenceInterpreter](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSequenceInterpreter.java)
 * [InterpreterConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/interpret/InterpreterConfiguration.java)
 * [InterpreterRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/interpret/InterpreterRequest.java)
 * [InterpreterResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/interpret/InterpreterResponse.java)
 * [SequenceInterpreter](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SequenceInterpreter.java)

**Test output**  
The output of this test shows several sequence interpreter requests and the debug log of corresponding responses.  
~~~~
Request prompt: '', input: 'Hallo.'
Response debug log;
23:34:19:507: Classify language for sequence: Hallo.
23:34:19:507: Classified language: NLD
23:34:19:507: Translate sequence: Hallo.
23:34:19:507: Translated sequence: UNI_ABC:Hallo .
23:34:19:507: Correction time limit: 200
23:34:19:509: Correcting sequence: Hallo.
23:34:19:509: Classify master context for sequence: Hallo.
23:34:19:509: Classified master context: Generic
23:34:19:509: Classify context for sequence: Hallo.
23:34:19:509: Classified context: Handshake
Interpreting the request took: 3 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
23:34:19:510: Classify language for sequence: Mijn naam si gekste der henkies.
23:34:19:510: Classified language: NLD
23:34:19:510: Translate sequence: Mijn naam si gekste der henkies.
23:34:19:512: Translated sequence: UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:si UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies .
23:34:19:513: Correction time limit: 700
23:34:19:513: Correcting sequence: Mijn naam si gekste der henkies.
23:34:19:911: Corrected sequence: Mijn naam is gekste der henkies.
23:34:19:911: Classify master context for sequence: Mijn naam is gekste der henkies.
23:34:19:911: Classified master context: Generic
23:34:19:911: Classify context for sequence: Mijn naam is gekste der henkies.
23:34:19:911: Classified context: Handshake
23:34:19:911: Translate corrected sequence: Mijn naam is gekste der henkies.
23:34:19:913: Translated corrected sequence: UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:is UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies .
Interpreting the request took: 404 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
23:34:19:913: Classify language for sequence: Albert einstein.
23:34:19:914: Classify language for sequence: What is your name? Albert einstein.
23:34:19:914: Classified language: ENG
23:34:19:915: Translate sequence: What is your name? Albert einstein.
23:34:19:916: Translated sequence: UNI_ABC:Albert|ENG_NAM:firstName:UNI_ABC:Albert UNI_ABC:einstein|ENG_NAM:lastName:UNI_ABC:Einstein .
23:34:19:916: Correction time limit: 300
23:34:19:916: Correcting sequence: Albert einstein.
23:34:20:070: Classify master context for sequence: Albert einstein.
23:34:20:070: Classify master context for sequence: What is your name? Albert einstein.
23:34:20:070: Classified master context: Generic
23:34:20:070: Classify context for sequence: Albert einstein.
23:34:20:070: Classify context for sequence: What is your name? Albert einstein.
23:34:20:071: Classified context: Handshake
Interpreting the request took: 158 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
23:34:20:071: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
23:34:20:071: Selected primary language: ENG
23:34:20:071: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
23:34:20:072: Translated sequence: UNI_ABC:Wruio UNI_ABC:wwtiop UNI_ABC:wtwrpoi UNI_ABC:weptiwpipw UNI_ABC:ipwopkm UNI_ABC:eopipwqwrqqiop UNI_ABC:qwerqwer UNI_ABC:qrqpoqe UNI_ABC:qpxnxc UNI_ABC:qwpgsjkdbvhsdfkljjv .
23:34:20:073: Correction time limit: 1100
23:34:20:073: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
23:34:21:173: Classify master context for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
Interpreting the request took: 1103 ms
~~~~

nl.zeesoft.zsd.test.TestDialogHandlerConfiguration
--------------------------------------------------
This test uses a lot of JSON files to initialize a *DialogHandlerConfiguration* instance.

**Example implementation**  
~~~~
// Create the DialogHandlerConfiguration
DialogHandlerConfiguration config = new DialogHandlerConfiguration();
// Add a listener
config.addListener(this);
// Start the initialization
config.initialize();
~~~~

This test uses the *MockDialogHandlerConfiguration*.

Class references;  
 * [TestDialogHandlerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogHandlerConfiguration.java)
 * [DialogHandlerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogHandlerConfiguration.java)

**Test output**  
The output of this test shows the time it takes to initialize all remaining objects simultaneously.  
~~~~
Classes:
- nl.zeesoft.zsd.SequencePreprocessor (LanguagePreprocessor) <= resources/LanguagePreprocessor.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageClassifier) <= resources/LanguageClassifier.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextENG) <= resources/LanguageMasterContextENG.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENGGeneric) <= resources/LanguageContextENGGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENGRoom) <= resources/LanguageContextENGRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextNLD) <= resources/LanguageMasterContextNLD.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLDGeneric) <= resources/LanguageContextNLDGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLDRoom) <= resources/LanguageContextNLDRoom.json
- nl.zeesoft.zsd.test.FixedDateEntityValueTranslator (EntityValueTranslator)
- nl.zeesoft.zsd.dialog.DialogSet (DialogSet) <= resources/DialogSetENG.json, resources/DialogSetNLD.json

Initializing DialogSet took 328 ms
Initialized all classes
~~~~

nl.zeesoft.zsd.test.TestDialogHandler
-------------------------------------
This test show how to use a *DialogHandler* to process an *DialogRequest*.

**Example implementation**  
~~~~
// Create the DialogHandlerConfiguration
DialogHandlerConfiguration config = new DialogHandlerConfiguration();
// Add a listener
config.addListener(this);
// Start the initialization (and wait until it's done)
config.initialize();
// Create the dialog handler
DialogHandler handler = new DialogHandler(config);
// Create the handler request
DialogRequest request = new DialogRequest("The optional question that prompted the input","The input sequence");
// Use the handler to process the request
DialogResponse response = interpreter.handleDialogRequest(request);
~~~~

Class references;  
 * [TestDialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogHandler.java)
 * [DialogHandlerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogHandlerConfiguration.java)
 * [DialogRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogRequest.java)
 * [DialogResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogResponse.java)
 * [DialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/DialogHandler.java)

**Test output**  
The output of this test shows several dialog handler requests and the debug log of corresponding responses.  
~~~~
Request prompt: '', input: 'Hallo.'
Response debug log;
23:34:23:424: Classify language for sequence: Hallo.
23:34:23:424: Classified language: NLD
23:34:23:424: Translate sequence: Hallo.
23:34:23:424: Translated sequence: UNI_ABC:Hallo .
23:34:23:424: Correction time limit: 200
23:34:23:424: Correcting sequence: Hallo.
23:34:23:424: Classify master context for sequence: Hallo.
23:34:23:424: Classified master context: Generic
23:34:23:424: Classify context for sequence: Hallo.
23:34:23:425: Classified context: Handshake
23:34:23:425: Handling dialog: NLD/Generic/Handshake
23:34:23:425:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
23:34:23:425:     Found matches for sequence: 2
23:34:23:425:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. Wat is jouw naam? (0.3365834004834811 / 1.0)
23:34:23:425:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. Wat is jouw naam? (0.3365834004834811 / 1.0)
23:34:23:426:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
23:34:23:426:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 2 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
23:34:23:426: Classify language for sequence: Wie ben jij?
23:34:23:426: Classified language: NLD
23:34:23:426: Translate sequence: Wie ben jij?
23:34:23:431: Translated sequence: UNI_ABC:Wie UNI_ABC:ben UNI_ABC:jij ?
23:34:23:432: Correction time limit: 400
23:34:23:432: Correcting sequence: Wie ben jij?
23:34:23:432: Classify master context for sequence: Wie ben jij?
23:34:23:433: Classified master context: Generic
23:34:23:433: Classify context for sequence: Wie ben jij?
23:34:23:433: Classified context: Handshake
23:34:23:433: Handling dialog: NLD/Generic/Handshake
23:34:23:433:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
23:34:23:434:     Found matches for sequence: 1
23:34:23:434:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. Wat is jouw naam? (1.5860864893902769 / 1.0)
23:34:23:434:     Set dialog output: Mijn naam is Dyz Lecticus.
23:34:23:434:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 8 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
23:34:23:434: Classify language for sequence: Albert einstein.
23:34:23:435: Classify language for sequence: What is your name? Albert einstein.
23:34:23:435: Classified language: ENG
23:34:23:436: Translate sequence: What is your name? Albert einstein.
23:34:23:436: Translated sequence: UNI_ABC:Albert|ENG_NAM:firstName:UNI_ABC:Albert UNI_ABC:einstein|ENG_NAM:lastName:UNI_ABC:Einstein .
23:34:23:436: Correction time limit: 300
23:34:23:436: Correcting sequence: Albert einstein.
23:34:23:631: Classify master context for sequence: Albert einstein.
23:34:23:631: Classify master context for sequence: What is your name? Albert einstein.
23:34:23:631: Classified master context: Generic
23:34:23:631: Classify context for sequence: Albert einstein.
23:34:23:632: Classify context for sequence: What is your name? Albert einstein.
23:34:23:632: Classified context: Handshake
23:34:23:632: Handling dialog: ENG/Generic/Handshake
23:34:23:632:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
23:34:23:632:     Updated variable firstName: UNI_ABC:Albert = Albert
23:34:23:632:     Updated variable lastName: UNI_ABC:Einstein = Einstein
23:34:23:632:     Updated variable fullName: Albert Einstein
23:34:23:632:     Prompt variable: nextDialog
23:34:23:632:     Set dialog output: 
23:34:23:632:     Set dialog prompt: What can I do for you Albert Einstein?
23:34:23:632: Handling dialog: ENG/Generic/QuestionAndAnswer
23:34:23:632:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
23:34:23:632:     Failed to find matches for sequence: Albert einstein.
Handling the request took: 198 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
23:34:23:632: Classify language for sequence: Mijn naam si gekste der henkies.
23:34:23:633: Classified language: NLD
23:34:23:633: Translate sequence: Mijn naam si gekste der henkies.
23:34:23:634: Translated sequence: UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:si UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies .
23:34:23:634: Correction time limit: 700
23:34:23:634: Correcting sequence: Mijn naam si gekste der henkies.
23:34:24:107: Corrected sequence: Mijn naam is gekste der henkies.
23:34:24:107: Classify master context for sequence: Mijn naam is gekste der henkies.
23:34:24:107: Classified master context: Generic
23:34:24:107: Classify context for sequence: Mijn naam is gekste der henkies.
23:34:24:109: Classified context: Handshake
23:34:24:109: Translate corrected sequence: Mijn naam is gekste der henkies.
23:34:24:109: Translated corrected sequence: UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:is UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies .
23:34:24:109: Handling dialog: NLD/Generic/Handshake
23:34:24:109:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
23:34:24:109:     Updated variable firstName: UNI_ABC:Gekste = Gekste
23:34:24:109:     Updated variable lastName: UNI_ABC:Henkies = Henkies
23:34:24:109:     Updated variable preposition: NLD_PRE:6 = der
23:34:24:109:     Updated variable fullName: Gekste der Henkies
23:34:24:109:     Prompt variable: nextDialog
23:34:24:109:     Set dialog output: 
23:34:24:109:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 477 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
23:34:24:110: Classify language for sequence: Who created you?
23:34:24:110: Classified language: ENG
23:34:24:110: Translate sequence: Who created you?
23:34:24:110: Translated sequence: UNI_ABC:Who UNI_ABC:created UNI_ABC:you ?
23:34:24:110: Correction time limit: 400
23:34:24:110: Correcting sequence: Who created you?
23:34:24:110: Classify master context for sequence: Who created you?
23:34:24:110: Classified master context: Generic
23:34:24:110: Classify context for sequence: Who created you?
23:34:24:110: Classified context: QuestionAndAnswer
23:34:24:110: Handling dialog: ENG/Generic/QuestionAndAnswer
23:34:24:110:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
23:34:24:110:     Found matches for sequence: 1
23:34:24:110:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.2964390448261416 / 1.0)
23:34:24:112:     Set dialog output: My software was written by André van der Zee.
Handling the request took: 3 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
23:34:24:112: Classify language for sequence: You asshole!
23:34:24:112: Classified language: ENG
23:34:24:112: Translate sequence: You asshole!
23:34:24:112: Translated sequence: UNI_ABC:You ENG_PRF:1|UNI_ABC:asshole !
23:34:24:112: Correction time limit: 300
23:34:24:112: Correcting sequence: You asshole!
23:34:24:112: Classify master context for sequence: You asshole!
23:34:24:112: Classified master context: Generic
23:34:24:112: Classify context for sequence: You asshole!
23:34:24:112: Classified context: Profanity
23:34:24:112: Handling dialog: ENG/Generic/Profanity
23:34:24:112:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
23:34:24:112:     Found matches for sequence: 2
23:34:24:112:     - Asshole! [OUTPUT] I do not appreciate that kind of language. (0.3334304584304584 / 1.0)
23:34:24:112:     - Asshole! [OUTPUT] I will pretend I did not read that. (0.3334304584304584 / 1.0)
23:34:24:112:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 0 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
23:34:24:112: Classify language for sequence: Jij klootzak!
23:34:24:113: Classified language: NLD
23:34:24:113: Translate sequence: Jij klootzak!
23:34:24:113: Translated sequence: UNI_ABC:Jij NLD_PRF:2|UNI_ABC:klootzak !
23:34:24:113: Correction time limit: 300
23:34:24:113: Correcting sequence: Jij klootzak!
23:34:24:113: Classify master context for sequence: Jij klootzak!
23:34:24:113: Classified master context: Generic
23:34:24:113: Classify context for sequence: Jij klootzak!
23:34:24:113: Classified context: Profanity
23:34:24:113: Handling dialog: NLD/Generic/Profanity
23:34:24:113:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
23:34:24:113:     Found matches for sequence: 2
23:34:24:113:     - Klootzak! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.35121381886087766 / 1.0)
23:34:24:113:     - Klootzak! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.35121381886087766 / 1.0)
23:34:24:114:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 6 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
23:34:24:118: Classify language for sequence: Can I book a room for 5 people?
23:34:24:118: Classified language: ENG
23:34:24:118: Translate sequence: Can I book a room for 5 people?
23:34:24:119: Translated sequence: UNI_ABC:Can UNI_ABC:I UNI_ABC:book UNI_ABC:a|ENG_NAM:firstName:UNI_ABC:A UNI_ABC:room|ENG_NAM:lastName:UNI_ABC:Room UNI_ABC:for UNI_NUM:5 UNI_ABC:people ?
23:34:24:119: Correction time limit: 900
23:34:24:119: Correcting sequence: Can I book a room for 5 people?
23:34:24:120: Classify master context for sequence: Can I book a room for 5 people?
23:34:24:120: Classified master context: Room
23:34:24:120: Classify context for sequence: Can I book a room for 5 people?
23:34:24:120: Classified context: Booking
23:34:24:120: Handling dialog: ENG/Room/Booking
23:34:24:120:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.RoomBookingHandler
23:34:24:120:     Updated variable bookPeople: UNI_NUM:5 = 5
23:34:24:121:     Prompt variable: bookDate
23:34:24:121:     Set dialog output: 
23:34:24:121:     Set dialog prompt: On what date?
Handling the request took: 3 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
23:34:24:121: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
23:34:24:122: Classified language: ENG
23:34:24:122: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
23:34:24:124: Translated sequence: UNI_ABC:Can UNI_ABC:I UNI_ABC:book UNI_ABC:a|ENG_NAM:firstName:UNI_ABC:A UNI_ABC:room|ENG_NAM:lastName:UNI_ABC:Room UNI_ABC:for UNI_NUM:12 UNI_ABC:people UNI_ABC:on ENG_DAT:2018-10-01 UNI_ABC:at ENG_TIM:12:00:00 UNI_ABC:for ENG_DUR:02:00 ?
23:34:24:124: Correction time limit: 1332
23:34:24:124: Correcting sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
23:34:24:125: Classify master context for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
23:34:24:125: Classified master context: Room
23:34:24:125: Classify context for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
23:34:24:125: Classified context: Booking
23:34:24:125: Handling dialog: ENG/Room/Booking
23:34:24:125:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.RoomBookingHandler
23:34:24:126:     Updated variable bookDate: ENG_DAT:2018-10-01 = october first twothousandeighteen
23:34:24:126:     Updated variable bookTime: ENG_TIM:12:00:00 = twelve o'clock
23:34:24:126:     Updated variable bookDuration: ENG_DUR:02:00 = two hours
23:34:24:126:     Updated variable bookPeople: UNI_NUM:12 = 12
23:34:24:126:     Prompt variable: bookConfirmation
23:34:24:126:     Set dialog output: 
23:34:24:126:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 5 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
23:34:24:127: Classify language for sequence: What does it cost to book a room?
23:34:24:127: Classified language: ENG
23:34:24:127: Translate sequence: What does it cost to book a room?
23:34:24:128: Translated sequence: UNI_ABC:What UNI_ABC:does UNI_ABC:it UNI_ABC:cost UNI_ABC:to UNI_ABC:book UNI_ABC:a UNI_ABC:room ?
23:34:24:128: Correction time limit: 900
23:34:24:128: Correcting sequence: What does it cost to book a room?
23:34:24:129: Classify master context for sequence: What does it cost to book a room?
23:34:24:129: Classified master context: Room
23:34:24:129: Classify context for sequence: What does it cost to book a room?
23:34:24:129: Classified context: QuestionAndAnswer
23:34:24:129: Handling dialog: ENG/Room/QuestionAndAnswer
23:34:24:129:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
23:34:24:129:     Found matches for sequence: 1
23:34:24:129:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (5.3062499999999995 / 1.0)
23:34:24:129:     Set dialog output: There are no costs attached to booking a room.
Handling the request took: 92 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
23:34:24:218: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
23:34:24:219: Classified language: ENG
23:34:24:219: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
23:34:24:223: Translated sequence: UNI_ABC:How UNI_ABC:much UNI_ABC:is ENG_NUM:10|UNI_ABC:ten ENG_MTH:M|UNI_ABC:times ENG_NUM:40|UNI_ABC:fourty ENG_MTH:D ENG_NUM:20|UNI_ABC:twenty ENG_MTH:A|UNI_ABC:plus ENG_NUM:3|UNI_ABC:three|ENG_NAM:firstName:UNI_ABC:Three ENG_MTH:M|UNI_ABC:times|ENG_NAM:lastName:UNI_ABC:Times ENG_NUM:6|UNI_ABC:six ?
23:34:24:223: Correction time limit: 1332
23:34:24:223: Correcting sequence: How much is ten times fourty divided by twenty plus three times six?
23:34:24:223: Classify master context for sequence: How much is ten times fourty divided by twenty plus three times six?
23:34:24:223: Classified master context: Generic
23:34:24:223: Classify context for sequence: How much is ten times fourty divided by twenty plus three times six?
23:34:24:224: Classified context: Math
23:34:24:224: Handling dialog: ENG/Generic/Math
23:34:24:224:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
23:34:24:224:     Updated variable number1: ENG_NUM:10 = ten
23:34:24:224:     Updated variable number2: ENG_NUM:40 = fourty
23:34:24:224:     Updated variable number3: ENG_NUM:20 = twenty
23:34:24:224:     Updated variable number4: ENG_NUM:3 = three
23:34:24:224:     Updated variable number5: ENG_NUM:6 = six
23:34:24:225:     Updated variable operator1: ENG_MTH:M = multiplied by
23:34:24:225:     Updated variable operator2: ENG_MTH:D = divided by
23:34:24:225:     Updated variable operator3: ENG_MTH:A = plus
23:34:24:225:     Updated variable operator4: ENG_MTH:M = multiplied by
23:34:24:225:     Calculate expression: 10 M 40 D 20 A 3 M 6
23:34:24:225:     Calculated expression: 38.0
23:34:24:225:     Updated variable exact: exactly
23:34:24:225:     Updated variable result: thirtyeight
23:34:24:225:     Prompt variable: nextDialog
23:34:24:225:     Set dialog output: Exactly thirtyeight.
23:34:24:225:     Set dialog prompt: What else can I do for you?
Handling the request took: 7 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
23:34:24:226: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
23:34:24:227: Classified language: NLD
23:34:24:227: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
23:34:24:230: Translated sequence: UNI_ABC:Hoeveel UNI_ABC:is NLD_NUM:10|UNI_ABC:tien NLD_MTH:M|UNI_ABC:keer NLD_NUM:40|UNI_ABC:veertig NLD_MTH:D NLD_NUM:20|UNI_ABC:twintig NLD_MTH:A|UNI_ABC:plus NLD_NUM:3|UNI_ABC:drie|NLD_NAM:firstName:UNI_ABC:Drie NLD_MTH:M|UNI_ABC:keer|NLD_NAM:lastName:UNI_ABC:Keer NLD_NUM:6|UNI_ABC:zes ?
23:34:24:230: Correction time limit: 1300
23:34:24:230: Correcting sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
23:34:24:230: Classify master context for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
23:34:24:230: Classified master context: Generic
23:34:24:230: Classify context for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
23:34:24:231: Classified context: Math
23:34:24:231: Handling dialog: NLD/Generic/Math
23:34:24:231:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
23:34:24:231:     Updated variable number1: NLD_NUM:10 = tien
23:34:24:231:     Updated variable number2: NLD_NUM:40 = veertig
23:34:24:231:     Updated variable number3: NLD_NUM:20 = twintig
23:34:24:231:     Updated variable number4: NLD_NUM:3 = drie
23:34:24:231:     Updated variable number5: NLD_NUM:6 = zes
23:34:24:231:     Updated variable operator1: NLD_MTH:M = vermenigvuldigd met
23:34:24:231:     Updated variable operator2: NLD_MTH:D = gedeeld door
23:34:24:232:     Updated variable operator3: NLD_MTH:A = plus
23:34:24:232:     Updated variable operator4: NLD_MTH:M = vermenigvuldigd met
23:34:24:232:     Calculate expression: 10 M 40 D 20 A 3 M 6
23:34:24:232:     Calculated expression: 38.0
23:34:24:232:     Updated variable exact: precies
23:34:24:232:     Updated variable result: achtendertig
23:34:24:232:     Prompt variable: nextDialog
23:34:24:232:     Set dialog output: Precies achtendertig.
23:34:24:232:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 93 ms
~~~~

Test results
------------
All 19 tests have been executed successfully (222 assertions).  
Total test duration: 60587 ms (total sleep duration: 28200 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 609 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 424 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 456 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 462 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 471 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 118976 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 118987 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 118986 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 119127 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 119027 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 119057 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 119037 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 550275 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 550276 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 550277 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 550278 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 550284 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 553431 Kb / 540 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 553442 Kb / 540 Mb
