Zeesoft Smart Dialogs
=====================
The Zeesoft Smart Dialogs (ZSD) is an open source library for Java application development.  

It provides support for;  
 * Context sensitive symbolic corrections; word spelling corrections  
 * Sequence context classification; sentence context classification  
 * Context sensitive sequence matching; find matching sentences  
 * Symbolic entity value translation; find variable values  

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
Initializing the SymbolCorrector took: 14 ms
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
Average correction time: 237 ms
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
Initializing the SequenceClassifier took: 3081 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 3 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 1 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 6 ms

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
Initializing the SequenceMatcher took: 6014 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over , dan hoef je niets te regelen.'
Matching the input sequence took: 19 ms

'Wat kost dat?' -> 'Zorg thuis : wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 55 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO , betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 2 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand , maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen , hypotheek aflossen , pensioen aanvullen en schenken.'
Matching the input sequence took: 35 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties , uw limiet , het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 48 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 28 ms

'Hypotheek berekenen' -> 'null'
Matching the input sequence took: 1 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 30 ms

'Fraude' -> 'En belangrijker : hoe kunt voorkomen slachtoffer te worden van CEO Fraude? [OUTPUT] Criminelen kunnen veel informatie over bedrijven op internet vinden. Check daarom regelmatig wat voor informatie u over uw bedrijf en de medewerkers online heeft staan. Maak het criminelen zo moeilijk mogelijk om online namen , functies en emailadressen te stelen.'
Matching the input sequence took: 9 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder ( bijvoorbeeld de CEO of de CFO ) uit uw organisatie , om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 17 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 43 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 94 ms

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
Initializing the EntityValueTranslator took: 1709 ms

Sequence: 'Eat three donuts at 9:00 or count to 110'
Translating the sequence took: 4 ms
Translation: 'UNI_ABC:Eat ENG_NUM:3|UNI_ABC:three UNI_ABC:donuts UNI_ABC:at UNI_TIM:09:00:00 UNI_ABC:or UNI_ABC:count UNI_ABC:to UNI_NUM:110'
Retranslating the sequence took: 0 ms
Retranslation: 'Eat three donuts at 09:00:00 or count to 110'

Sequence: 'Eet drie donuts om 9:00 of tel tot 110'
Translating the sequence took: 2 ms
Translation: 'UNI_ABC:Eet NLD_NUM:3|UNI_ABC:drie UNI_ABC:donuts UNI_ABC:om UNI_TIM:09:00:00 UNI_ABC:of UNI_ABC:tel UNI_ABC:tot UNI_NUM:110'
Retranslating the sequence took: 1 ms
Retranslation: 'Eet drie donuts om 09:00:00 of tel tot 110'

Sequence: 'I finished twohundredandtwentyfourth or 225th'
Translating the sequence took: 1 ms
Translation: 'UNI_ABC:I UNI_ABC:finished ENG_ORD:224|UNI_ABC:twohundredandtwentyfourth UNI_ABC:or ENG_OR2:225'
Retranslating the sequence took: 0 ms
Retranslation: 'I finished twohundredandtwentyfourth or 225th'

Sequence: 'Ik ben tweehonderdvierentwintigste geworden'
Translating the sequence took: 3 ms
Translation: 'UNI_ABC:Ik UNI_ABC:ben NLD_ORD:224|UNI_ABC:tweehonderdvierentwintigste|NLD_NAM:firstName:UNI_ABC:Tweehonderdvierentwintigste UNI_ABC:geworden|NLD_NAM:lastName:UNI_ABC:Geworden'
Retranslating the sequence took: 0 ms
Retranslation: 'Ik ben tweehonderdvierentwintigste geworden'

Sequence: 'februari march october december'
Translating the sequence took: 1 ms
Translation: 'NLD_MNT:2|UNI_ABC:februari ENG_MNT:3|UNI_ABC:march ENG_MNT:10|UNI_ABC:october ENG_MNT:12|NLD_MNT:12|UNI_ABC:december'
Retranslating the sequence took: 0 ms
Retranslation: 'februari march october december'

Sequence: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'
Translating the sequence took: 1 ms
Translation: 'ENG_DUR:33:41 UNI_MTH:D NLD_DUR:33:41'
Retranslating the sequence took: 0 ms
Retranslation: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'

Sequence: 'yesterday OR today OR the 1st of october'
Translating the sequence took: 3 ms
Translation: 'ENG_DAT:2018-07-15|UNI_ABC:yesterday UNI_ABC:OR ENG_DAT:2018-07-16|UNI_ABC:today UNI_ABC:OR ENG_DAT:2018-10-01'
Retranslating the sequence took: 1 ms
Retranslation: 'july fifteenth twothousandeighteen OR july sixteenth twothousandeighteen OR october first twothousandeighteen'

Sequence: 'gisteren OF vandaag OF 1 oktober'
Translating the sequence took: 0 ms
Translation: 'NLD_DAT:2018-07-15|UNI_ABC:gisteren UNI_ABC:OF NLD_DAT:2018-07-16|UNI_ABC:vandaag UNI_ABC:OF NLD_DAT:2018-10-01'
Retranslating the sequence took: 1 ms
Retranslation: 'vijftien juli tweeduizendachttien OF zestien juli tweeduizendachttien OF een oktober tweeduizendachttien'

Sequence: 'twelve o'clock OR five minutes to nine OR ten past one in the morning'
Translating the sequence took: 1 ms
Translation: 'ENG_TIM:12:00:00 UNI_ABC:OR ENG_TIM:08:55:00 UNI_ABC:OR ENG_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twelve o'clock OR fiftyfive past eight OR ten past one in the morning'

Sequence: 'twaalf uur OF vijf minuten voor negen OF tien over een sochtends'
Translating the sequence took: 1 ms
Translation: 'NLD_TIM:12:00:00|NLD_DUR:12:00 UNI_ABC:OF NLD_TIM:08:55:00 UNI_ABC:OF NLD_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends'

Sequence: 'My name is Andrew from the Sea'
Translating the sequence took: 1 ms
Translation: 'UNI_ABC:My UNI_ABC:name UNI_ABC:is UNI_ABC:Andrew|ENG_NAM:firstName:UNI_ABC:Andrew ENG_PRE:4|ENG_NAM:preposition:ENG_PRE:4 UNI_ABC:Sea|ENG_NAM:lastName:UNI_ABC:Sea'
Retranslating the sequence took: 1 ms
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
Translating the sequence took: 1 ms
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
Translating the sequence took: 1 ms
Translation: 'UNI_ABC:Jij NLD_PRF:2|UNI_ABC:klootzak'
Retranslating the sequence took: 0 ms
Retranslation: 'Jij klootzak'

Sequence: 'Can I book a room for 5 people?'
Translating the sequence took: 2 ms
Translation: 'UNI_ABC:Can UNI_ABC:I UNI_ABC:book UNI_ABC:a|ENG_NAM:firstName:UNI_ABC:A UNI_ABC:room|ENG_NAM:lastName:UNI_ABC:Room UNI_ABC:for UNI_NUM:5 UNI_ABC:people ?'
Retranslating the sequence took: 0 ms
Retranslation: 'Can I book a room for 5 people?'

Sequence: 'ten times five'
Translating the sequence took: 1 ms
Translation: 'ENG_NUM:10|NLD_PRE:8|UNI_ABC:ten ENG_MTH:M|UNI_ABC:times ENG_NUM:5|UNI_ABC:five'
Retranslating the sequence took: 0 ms
Retranslation: 'ten multiplied by five'

Sequence: 'tien keer vijf'
Translating the sequence took: 1 ms
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
Converting 332871 entity values took: 326 ms

Sample JSON;
{
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
Converting 2747 dialog examples took: 355 ms

Sample JSON;
{
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
The output of this test shows the converted JSON.  
~~~~
Converting 6 dialogs took: 1 ms

Sample JSON;
{
  {
    "language": "ENG",
    "masterContext": "Generic",
    "context": "Handshake",
    "handler": "nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler",
    "examples": [
      {
        "input": "Hello.",
        "output": "Hello. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Hello!",
        "output": "Hello. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Hi.",
        "output": "Hi. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Hi!",
        "output": "Hi. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Good morning.",
        "output": "Good morning. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Good morning!",
        "output": "Good morning. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Good afternoon.",
        "output": "Good afternoon. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Good afternoon!",
        "output": "Good afternoon. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Good evening.",
        "output": "Good evening. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Good evening!",
        "output": "Good evening. Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "What is your name?",
        "output": "Mijn naam is {selfName}. Wat is jouw naam?"
      },
      {
        "input": "Who are you?",
        "output": "Mijn naam is {selfName}. Wat is jouw naam?"
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
  },
  {
    "language": "ENG",
    "masterContext": "Generic",
    "context": "QuestionAndAnswer",
    "handler": "",
    "examples": [
      {
        "input": "Who is André van der Zee?",
        "output": "André van der Zee is a software engineer from Leiden, The Netherlands."
      },
      {
        "input": "What is the answer to the ultimate question of life, the universe, and everything?",
        "output": "Fourtytwo."
      },
      {
        "input": "How can you help me?",
        "output": "I can help with general questions, booking rooms and questions about room bookings."
      },
      {
        "input": "What can you do for me?",
        "output": "I can help with general questions, booking rooms and questions about room bookings."
      },
      {
        "input": "How can you assist me?",
        "output": "I can help with general questions, booking rooms and questions about room bookings."
      },
      {
        "input": "How can you support me?",
        "output": "I can help with general questions, booking rooms and questions about room bookings."
      },
      {
        "input": "How can you be of service to me?",
        "output": "I can help with general questions, booking rooms and questions about room bookings."
      },
      {
        "input": "What are you?",
        "output": "I am an artificially intelligent personal assistant."
      },
      {
        "input": "Can you describe yourself?",
        "output": "I am an artificially intelligent personal assistant."
      },
      {
        "input": "How would you describe yourself?",
        "output": "I am an artificially intelligent personal assistant."
      },
      {
        "input": "Who wrote your software?",
        "output": "My software was written by André van der Zee."
      },
      {
        "input": "Who programmed you?",
        "output": "My software was written by André van der Zee."
      },
      {
        "input": "Who created you?",
        "output": "My software was written by André van der Zee."
      },
      {
        "input": "Who made you?",
        "output": "My software was written by André van der Zee."
      },
      {
        "input": "Who built you?",
        "output": "My software was written by André van der Zee."
      },
      {
        "input": "What is your goal?",
        "output": "My goal is to understand and help people."
      },
      {
        "input": "What are your goals?",
        "output": "My goal is to understand and help people."
      },
      {
        "input": "Why do you exist?",
        "output": "My goal is to understand and help people."
      },
      {
        "input": "What is your purpose?",
        "output": "My goal is to understand and help people."
      },
      {
        "input": "What is the purpose of your existence?",
        "output": "My goal is to understand and help people."
      },
      {
        "input": "Why were you created?",
        "output": "My goal is to understand and help people."
      },
      {
        "input": "Why were you built?",
        "output": "My goal is to understand and help people."
      }
    ]
  },
  {
    "language": "ENG",
    "masterContext": "Generic",
    "context": "Math",
    "handler": "nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler",
    "examples": [
      {
        "input": "How much is {number1} divided by {number2}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} divided by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} minus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} multiplied by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} plus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} divided by {number2} times {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} divided by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} minus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} multiplied by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} plus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} minus {number2} times {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} divided by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} minus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} multiplied by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} plus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} multiplied by {number2} times {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} divided by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} minus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} multiplied by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} plus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} plus {number2} times {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} divided by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} minus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} multiplied by {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} plus {number3} times {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} divided by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} divided by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} divided by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} divided by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} divided by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} divided by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} minus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} minus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} minus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} minus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} minus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} minus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} multiplied by {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} multiplied by {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} multiplied by {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} multiplied by {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} multiplied by {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} multiplied by {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} plus {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} plus {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} plus {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} plus {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} plus {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} plus {number4} times {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} times {number4}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} times {number4} divided by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} times {number4} minus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} times {number4} multiplied by {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} times {number4} plus {number5}?"
      },
      {
        "input": "How much is {number1} times {number2} times {number3} times {number4} times {number5}?"
      }
    ],
    "variables": [
      {
        "name": "nextDialog",
        "type": "ABC",
        "prompts": [
          "{exact} {result}. What else can I do for you?",
          "{exact} {result}. Is there anything else I can help you with?",
          "That is {exact} {result}. What else can I do for you?",
          "That is {exact} {result}. Is there anything else I can help you with?"
        ]
      },
      {
        "name": "number1",
        "type": "NUM"
      },
      {
        "name": "number2",
        "type": "NUM"
      },
      {
        "name": "number3",
        "type": "NUM"
      },
      {
        "name": "number4",
        "type": "NUM"
      },
      {
        "name": "number5",
        "type": "NUM"
      },
      {
        "name": "operator1",
        "type": "MTH"
      },
      {
        "name": "operator2",
        "type": "MTH"
      },
      {
        "name": "operator3",
        "type": "MTH"
      },
      {
        "name": "operator4",
        "type": "MTH"
      }
    ]
  },
  {
    "language": "ENG",
    "masterContext": "Generic",
    "context": "Profanity",
    "handler": "",
    "examples": [
      {
        "input": "Arse!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Arse!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "arse.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "arse.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Arsehole!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Arsehole!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "arsehole.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "arsehole.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Asshole!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Asshole!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "asshole.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "asshole.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Bastard!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Bastard!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "bastard.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "bastard.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Bitch!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Bitch!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "bitch.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "bitch.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Blighter!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Blighter!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "blighter.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "blighter.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Cock!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Cock!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "cock.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "cock.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Cunt!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Cunt!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "cunt.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "cunt.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Dick!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Dick!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "dick.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "dick.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Dickhead!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Dickhead!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "dickhead.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "dickhead.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Faggot!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Faggot!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "faggot.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "faggot.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Faggy!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Faggy!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "faggy.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "faggy.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Fuck!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Fuck!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "fuck.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "fuck.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Fucker!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Fucker!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "fucker.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "fucker.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Fucking!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Fucking!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "fucking.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "fucking.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Motherfucker!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Motherfucker!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "motherfucker.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "motherfucker.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Munter!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Munter!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "munter.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "munter.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Nigger!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Nigger!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "nigger.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "nigger.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Shit!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Shit!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "shit.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "shit.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Shite!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Shite!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "shite.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "shite.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Shitty!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Shitty!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "shitty.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "shitty.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Slut!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Slut!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "slut.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "slut.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Tosser!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Tosser!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "tosser.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "tosser.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Turd!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Turd!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "turd.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "turd.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Wanker!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Wanker!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "wanker.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "wanker.",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "Whore!",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "Whore!",
        "output": "I will pretend I did not read that."
      },
      {
        "input": "whore.",
        "output": "I do not appreciate that kind of language."
      },
      {
        "input": "whore.",
        "output": "I will pretend I did not read that."
      }
    ]
  },
  {
    "language": "ENG",
    "masterContext": "Room",
    "context": "Booking",
    "handler": "nl.zeesoft.zsd.dialog.dialogs.RoomBookingHandler",
    "examples": [
      {
        "input": "I want to book a room."
      },
      {
        "input": "I want to book a room for {bookPeople} people."
      },
      {
        "input": "I want to book a room for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I want to book a room for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I want to book a room for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I want to book a room on {bookDate}."
      },
      {
        "input": "I want to book a room on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I want to book a room on {bookDate} from {bookTime}."
      },
      {
        "input": "I want to book a room on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I want to book a room from {bookTime}."
      },
      {
        "input": "I want to book a room from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I want to book a room from {bookTime} on {bookDate}."
      },
      {
        "input": "I want to book a room from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I want to book a room for a duration of {bookDuration}."
      },
      {
        "input": "I want to book a room for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I want to book a room for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I want to book a room for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I want to book a room at {bookTime}."
      },
      {
        "input": "I want to book a room at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I want to book a room at {bookTime} on {bookDate}."
      },
      {
        "input": "I want to book a room at {bookTime} from {bookTime}."
      },
      {
        "input": "I want to book a room at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a room."
      },
      {
        "input": "I would like to book a room for {bookPeople} people."
      },
      {
        "input": "I would like to book a room for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I would like to book a room for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I would like to book a room for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a room on {bookDate}."
      },
      {
        "input": "I would like to book a room on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I would like to book a room on {bookDate} from {bookTime}."
      },
      {
        "input": "I would like to book a room on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a room from {bookTime}."
      },
      {
        "input": "I would like to book a room from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I would like to book a room from {bookTime} on {bookDate}."
      },
      {
        "input": "I would like to book a room from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a room for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a room for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I would like to book a room for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I would like to book a room for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I would like to book a room at {bookTime}."
      },
      {
        "input": "I would like to book a room at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I would like to book a room at {bookTime} on {bookDate}."
      },
      {
        "input": "I would like to book a room at {bookTime} from {bookTime}."
      },
      {
        "input": "I would like to book a room at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I need a room."
      },
      {
        "input": "I need a room for {bookPeople} people."
      },
      {
        "input": "I need a room for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I need a room for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I need a room for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I need a room on {bookDate}."
      },
      {
        "input": "I need a room on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I need a room on {bookDate} from {bookTime}."
      },
      {
        "input": "I need a room on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I need a room from {bookTime}."
      },
      {
        "input": "I need a room from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I need a room from {bookTime} on {bookDate}."
      },
      {
        "input": "I need a room from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I need a room for a duration of {bookDuration}."
      },
      {
        "input": "I need a room for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I need a room for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I need a room for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I need a room at {bookTime}."
      },
      {
        "input": "I need a room at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I need a room at {bookTime} on {bookDate}."
      },
      {
        "input": "I need a room at {bookTime} from {bookTime}."
      },
      {
        "input": "I need a room at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "Can I book a room?"
      },
      {
        "input": "Can I book a room for {bookPeople} people?"
      },
      {
        "input": "Can I book a room for {bookPeople} people on {bookDate}?"
      },
      {
        "input": "Can I book a room for {bookPeople} people from {bookTime}?"
      },
      {
        "input": "Can I book a room for {bookPeople} people for a duration of {bookDuration}?"
      },
      {
        "input": "Can I book a room on {bookDate}?"
      },
      {
        "input": "Can I book a room on {bookDate} for {bookPeople} people?"
      },
      {
        "input": "Can I book a room on {bookDate} from {bookTime}?"
      },
      {
        "input": "Can I book a room on {bookDate} for a duration of {bookDuration}?"
      },
      {
        "input": "Can I book a room from {bookTime}?"
      },
      {
        "input": "Can I book a room from {bookTime} for {bookPeople} people?"
      },
      {
        "input": "Can I book a room from {bookTime} on {bookDate}?"
      },
      {
        "input": "Can I book a room from {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "Can I book a room for a duration of {bookDuration}?"
      },
      {
        "input": "Can I book a room for a duration of {bookDuration} for {bookPeople} people?"
      },
      {
        "input": "Can I book a room for a duration of {bookDuration} on {bookDate}?"
      },
      {
        "input": "Can I book a room for a duration of {bookDuration} from {bookTime}?"
      },
      {
        "input": "Can I book a room at {bookTime}?"
      },
      {
        "input": "Can I book a room at {bookTime} for {bookPeople} people?"
      },
      {
        "input": "Can I book a room at {bookTime} on {bookDate}?"
      },
      {
        "input": "Can I book a room at {bookTime} from {bookTime}?"
      },
      {
        "input": "Can I book a room at {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a room?"
      },
      {
        "input": "May I book a room for {bookPeople} people?"
      },
      {
        "input": "May I book a room for {bookPeople} people on {bookDate}?"
      },
      {
        "input": "May I book a room for {bookPeople} people from {bookTime}?"
      },
      {
        "input": "May I book a room for {bookPeople} people for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a room on {bookDate}?"
      },
      {
        "input": "May I book a room on {bookDate} for {bookPeople} people?"
      },
      {
        "input": "May I book a room on {bookDate} from {bookTime}?"
      },
      {
        "input": "May I book a room on {bookDate} for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a room from {bookTime}?"
      },
      {
        "input": "May I book a room from {bookTime} for {bookPeople} people?"
      },
      {
        "input": "May I book a room from {bookTime} on {bookDate}?"
      },
      {
        "input": "May I book a room from {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a room for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a room for a duration of {bookDuration} for {bookPeople} people?"
      },
      {
        "input": "May I book a room for a duration of {bookDuration} on {bookDate}?"
      },
      {
        "input": "May I book a room for a duration of {bookDuration} from {bookTime}?"
      },
      {
        "input": "May I book a room at {bookTime}?"
      },
      {
        "input": "May I book a room at {bookTime} for {bookPeople} people?"
      },
      {
        "input": "May I book a room at {bookTime} on {bookDate}?"
      },
      {
        "input": "May I book a room at {bookTime} from {bookTime}?"
      },
      {
        "input": "May I book a room at {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "I want to book a space."
      },
      {
        "input": "I want to book a space for {bookPeople} people."
      },
      {
        "input": "I want to book a space for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I want to book a space for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I want to book a space for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I want to book a space on {bookDate}."
      },
      {
        "input": "I want to book a space on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I want to book a space on {bookDate} from {bookTime}."
      },
      {
        "input": "I want to book a space on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I want to book a space from {bookTime}."
      },
      {
        "input": "I want to book a space from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I want to book a space from {bookTime} on {bookDate}."
      },
      {
        "input": "I want to book a space from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I want to book a space for a duration of {bookDuration}."
      },
      {
        "input": "I want to book a space for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I want to book a space for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I want to book a space for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I want to book a space at {bookTime}."
      },
      {
        "input": "I want to book a space at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I want to book a space at {bookTime} on {bookDate}."
      },
      {
        "input": "I want to book a space at {bookTime} from {bookTime}."
      },
      {
        "input": "I want to book a space at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a space."
      },
      {
        "input": "I would like to book a space for {bookPeople} people."
      },
      {
        "input": "I would like to book a space for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I would like to book a space for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I would like to book a space for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a space on {bookDate}."
      },
      {
        "input": "I would like to book a space on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I would like to book a space on {bookDate} from {bookTime}."
      },
      {
        "input": "I would like to book a space on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a space from {bookTime}."
      },
      {
        "input": "I would like to book a space from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I would like to book a space from {bookTime} on {bookDate}."
      },
      {
        "input": "I would like to book a space from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a space for a duration of {bookDuration}."
      },
      {
        "input": "I would like to book a space for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I would like to book a space for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I would like to book a space for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I would like to book a space at {bookTime}."
      },
      {
        "input": "I would like to book a space at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I would like to book a space at {bookTime} on {bookDate}."
      },
      {
        "input": "I would like to book a space at {bookTime} from {bookTime}."
      },
      {
        "input": "I would like to book a space at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I need a space."
      },
      {
        "input": "I need a space for {bookPeople} people."
      },
      {
        "input": "I need a space for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I need a space for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I need a space for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I need a space on {bookDate}."
      },
      {
        "input": "I need a space on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I need a space on {bookDate} from {bookTime}."
      },
      {
        "input": "I need a space on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I need a space from {bookTime}."
      },
      {
        "input": "I need a space from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I need a space from {bookTime} on {bookDate}."
      },
      {
        "input": "I need a space from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I need a space for a duration of {bookDuration}."
      },
      {
        "input": "I need a space for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I need a space for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I need a space for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I need a space at {bookTime}."
      },
      {
        "input": "I need a space at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I need a space at {bookTime} on {bookDate}."
      },
      {
        "input": "I need a space at {bookTime} from {bookTime}."
      },
      {
        "input": "I need a space at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "Can I book a space?"
      },
      {
        "input": "Can I book a space for {bookPeople} people?"
      },
      {
        "input": "Can I book a space for {bookPeople} people on {bookDate}?"
      },
      {
        "input": "Can I book a space for {bookPeople} people from {bookTime}?"
      },
      {
        "input": "Can I book a space for {bookPeople} people for a duration of {bookDuration}?"
      },
      {
        "input": "Can I book a space on {bookDate}?"
      },
      {
        "input": "Can I book a space on {bookDate} for {bookPeople} people?"
      },
      {
        "input": "Can I book a space on {bookDate} from {bookTime}?"
      },
      {
        "input": "Can I book a space on {bookDate} for a duration of {bookDuration}?"
      },
      {
        "input": "Can I book a space from {bookTime}?"
      },
      {
        "input": "Can I book a space from {bookTime} for {bookPeople} people?"
      },
      {
        "input": "Can I book a space from {bookTime} on {bookDate}?"
      },
      {
        "input": "Can I book a space from {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "Can I book a space for a duration of {bookDuration}?"
      },
      {
        "input": "Can I book a space for a duration of {bookDuration} for {bookPeople} people?"
      },
      {
        "input": "Can I book a space for a duration of {bookDuration} on {bookDate}?"
      },
      {
        "input": "Can I book a space for a duration of {bookDuration} from {bookTime}?"
      },
      {
        "input": "Can I book a space at {bookTime}?"
      },
      {
        "input": "Can I book a space at {bookTime} for {bookPeople} people?"
      },
      {
        "input": "Can I book a space at {bookTime} on {bookDate}?"
      },
      {
        "input": "Can I book a space at {bookTime} from {bookTime}?"
      },
      {
        "input": "Can I book a space at {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a space?"
      },
      {
        "input": "May I book a space for {bookPeople} people?"
      },
      {
        "input": "May I book a space for {bookPeople} people on {bookDate}?"
      },
      {
        "input": "May I book a space for {bookPeople} people from {bookTime}?"
      },
      {
        "input": "May I book a space for {bookPeople} people for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a space on {bookDate}?"
      },
      {
        "input": "May I book a space on {bookDate} for {bookPeople} people?"
      },
      {
        "input": "May I book a space on {bookDate} from {bookTime}?"
      },
      {
        "input": "May I book a space on {bookDate} for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a space from {bookTime}?"
      },
      {
        "input": "May I book a space from {bookTime} for {bookPeople} people?"
      },
      {
        "input": "May I book a space from {bookTime} on {bookDate}?"
      },
      {
        "input": "May I book a space from {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a space for a duration of {bookDuration}?"
      },
      {
        "input": "May I book a space for a duration of {bookDuration} for {bookPeople} people?"
      },
      {
        "input": "May I book a space for a duration of {bookDuration} on {bookDate}?"
      },
      {
        "input": "May I book a space for a duration of {bookDuration} from {bookTime}?"
      },
      {
        "input": "May I book a space at {bookTime}?"
      },
      {
        "input": "May I book a space at {bookTime} for {bookPeople} people?"
      },
      {
        "input": "May I book a space at {bookTime} on {bookDate}?"
      },
      {
        "input": "May I book a space at {bookTime} from {bookTime}?"
      },
      {
        "input": "May I book a space at {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "I want to reserve a room."
      },
      {
        "input": "I want to reserve a room for {bookPeople} people."
      },
      {
        "input": "I want to reserve a room for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I want to reserve a room for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I want to reserve a room for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I want to reserve a room on {bookDate}."
      },
      {
        "input": "I want to reserve a room on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I want to reserve a room on {bookDate} from {bookTime}."
      },
      {
        "input": "I want to reserve a room on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I want to reserve a room from {bookTime}."
      },
      {
        "input": "I want to reserve a room from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I want to reserve a room from {bookTime} on {bookDate}."
      },
      {
        "input": "I want to reserve a room from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I want to reserve a room for a duration of {bookDuration}."
      },
      {
        "input": "I want to reserve a room for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I want to reserve a room for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I want to reserve a room for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I want to reserve a room at {bookTime}."
      },
      {
        "input": "I want to reserve a room at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I want to reserve a room at {bookTime} on {bookDate}."
      },
      {
        "input": "I want to reserve a room at {bookTime} from {bookTime}."
      },
      {
        "input": "I want to reserve a room at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a room."
      },
      {
        "input": "I would like to reserve a room for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a room for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I would like to reserve a room for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I would like to reserve a room for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a room on {bookDate}."
      },
      {
        "input": "I would like to reserve a room on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a room on {bookDate} from {bookTime}."
      },
      {
        "input": "I would like to reserve a room on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a room from {bookTime}."
      },
      {
        "input": "I would like to reserve a room from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a room from {bookTime} on {bookDate}."
      },
      {
        "input": "I would like to reserve a room from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a room for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a room for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a room for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I would like to reserve a room for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I would like to reserve a room at {bookTime}."
      },
      {
        "input": "I would like to reserve a room at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a room at {bookTime} on {bookDate}."
      },
      {
        "input": "I would like to reserve a room at {bookTime} from {bookTime}."
      },
      {
        "input": "I would like to reserve a room at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I need a room."
      },
      {
        "input": "I need a room for {bookPeople} people."
      },
      {
        "input": "I need a room for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I need a room for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I need a room for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I need a room on {bookDate}."
      },
      {
        "input": "I need a room on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I need a room on {bookDate} from {bookTime}."
      },
      {
        "input": "I need a room on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I need a room from {bookTime}."
      },
      {
        "input": "I need a room from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I need a room from {bookTime} on {bookDate}."
      },
      {
        "input": "I need a room from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I need a room for a duration of {bookDuration}."
      },
      {
        "input": "I need a room for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I need a room for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I need a room for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I need a room at {bookTime}."
      },
      {
        "input": "I need a room at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I need a room at {bookTime} on {bookDate}."
      },
      {
        "input": "I need a room at {bookTime} from {bookTime}."
      },
      {
        "input": "I need a room at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "Can I reserve a room?"
      },
      {
        "input": "Can I reserve a room for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a room for {bookPeople} people on {bookDate}?"
      },
      {
        "input": "Can I reserve a room for {bookPeople} people from {bookTime}?"
      },
      {
        "input": "Can I reserve a room for {bookPeople} people for a duration of {bookDuration}?"
      },
      {
        "input": "Can I reserve a room on {bookDate}?"
      },
      {
        "input": "Can I reserve a room on {bookDate} for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a room on {bookDate} from {bookTime}?"
      },
      {
        "input": "Can I reserve a room on {bookDate} for a duration of {bookDuration}?"
      },
      {
        "input": "Can I reserve a room from {bookTime}?"
      },
      {
        "input": "Can I reserve a room from {bookTime} for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a room from {bookTime} on {bookDate}?"
      },
      {
        "input": "Can I reserve a room from {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "Can I reserve a room for a duration of {bookDuration}?"
      },
      {
        "input": "Can I reserve a room for a duration of {bookDuration} for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a room for a duration of {bookDuration} on {bookDate}?"
      },
      {
        "input": "Can I reserve a room for a duration of {bookDuration} from {bookTime}?"
      },
      {
        "input": "Can I reserve a room at {bookTime}?"
      },
      {
        "input": "Can I reserve a room at {bookTime} for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a room at {bookTime} on {bookDate}?"
      },
      {
        "input": "Can I reserve a room at {bookTime} from {bookTime}?"
      },
      {
        "input": "Can I reserve a room at {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a room?"
      },
      {
        "input": "May I reserve a room for {bookPeople} people?"
      },
      {
        "input": "May I reserve a room for {bookPeople} people on {bookDate}?"
      },
      {
        "input": "May I reserve a room for {bookPeople} people from {bookTime}?"
      },
      {
        "input": "May I reserve a room for {bookPeople} people for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a room on {bookDate}?"
      },
      {
        "input": "May I reserve a room on {bookDate} for {bookPeople} people?"
      },
      {
        "input": "May I reserve a room on {bookDate} from {bookTime}?"
      },
      {
        "input": "May I reserve a room on {bookDate} for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a room from {bookTime}?"
      },
      {
        "input": "May I reserve a room from {bookTime} for {bookPeople} people?"
      },
      {
        "input": "May I reserve a room from {bookTime} on {bookDate}?"
      },
      {
        "input": "May I reserve a room from {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a room for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a room for a duration of {bookDuration} for {bookPeople} people?"
      },
      {
        "input": "May I reserve a room for a duration of {bookDuration} on {bookDate}?"
      },
      {
        "input": "May I reserve a room for a duration of {bookDuration} from {bookTime}?"
      },
      {
        "input": "May I reserve a room at {bookTime}?"
      },
      {
        "input": "May I reserve a room at {bookTime} for {bookPeople} people?"
      },
      {
        "input": "May I reserve a room at {bookTime} on {bookDate}?"
      },
      {
        "input": "May I reserve a room at {bookTime} from {bookTime}?"
      },
      {
        "input": "May I reserve a room at {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "I want to reserve a space."
      },
      {
        "input": "I want to reserve a space for {bookPeople} people."
      },
      {
        "input": "I want to reserve a space for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I want to reserve a space for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I want to reserve a space for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I want to reserve a space on {bookDate}."
      },
      {
        "input": "I want to reserve a space on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I want to reserve a space on {bookDate} from {bookTime}."
      },
      {
        "input": "I want to reserve a space on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I want to reserve a space from {bookTime}."
      },
      {
        "input": "I want to reserve a space from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I want to reserve a space from {bookTime} on {bookDate}."
      },
      {
        "input": "I want to reserve a space from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I want to reserve a space for a duration of {bookDuration}."
      },
      {
        "input": "I want to reserve a space for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I want to reserve a space for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I want to reserve a space for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I want to reserve a space at {bookTime}."
      },
      {
        "input": "I want to reserve a space at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I want to reserve a space at {bookTime} on {bookDate}."
      },
      {
        "input": "I want to reserve a space at {bookTime} from {bookTime}."
      },
      {
        "input": "I want to reserve a space at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a space."
      },
      {
        "input": "I would like to reserve a space for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a space for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I would like to reserve a space for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I would like to reserve a space for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a space on {bookDate}."
      },
      {
        "input": "I would like to reserve a space on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a space on {bookDate} from {bookTime}."
      },
      {
        "input": "I would like to reserve a space on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a space from {bookTime}."
      },
      {
        "input": "I would like to reserve a space from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a space from {bookTime} on {bookDate}."
      },
      {
        "input": "I would like to reserve a space from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a space for a duration of {bookDuration}."
      },
      {
        "input": "I would like to reserve a space for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a space for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I would like to reserve a space for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I would like to reserve a space at {bookTime}."
      },
      {
        "input": "I would like to reserve a space at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I would like to reserve a space at {bookTime} on {bookDate}."
      },
      {
        "input": "I would like to reserve a space at {bookTime} from {bookTime}."
      },
      {
        "input": "I would like to reserve a space at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I need a space."
      },
      {
        "input": "I need a space for {bookPeople} people."
      },
      {
        "input": "I need a space for {bookPeople} people on {bookDate}."
      },
      {
        "input": "I need a space for {bookPeople} people from {bookTime}."
      },
      {
        "input": "I need a space for {bookPeople} people for a duration of {bookDuration}."
      },
      {
        "input": "I need a space on {bookDate}."
      },
      {
        "input": "I need a space on {bookDate} for {bookPeople} people."
      },
      {
        "input": "I need a space on {bookDate} from {bookTime}."
      },
      {
        "input": "I need a space on {bookDate} for a duration of {bookDuration}."
      },
      {
        "input": "I need a space from {bookTime}."
      },
      {
        "input": "I need a space from {bookTime} for {bookPeople} people."
      },
      {
        "input": "I need a space from {bookTime} on {bookDate}."
      },
      {
        "input": "I need a space from {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "I need a space for a duration of {bookDuration}."
      },
      {
        "input": "I need a space for a duration of {bookDuration} for {bookPeople} people."
      },
      {
        "input": "I need a space for a duration of {bookDuration} on {bookDate}."
      },
      {
        "input": "I need a space for a duration of {bookDuration} from {bookTime}."
      },
      {
        "input": "I need a space at {bookTime}."
      },
      {
        "input": "I need a space at {bookTime} for {bookPeople} people."
      },
      {
        "input": "I need a space at {bookTime} on {bookDate}."
      },
      {
        "input": "I need a space at {bookTime} from {bookTime}."
      },
      {
        "input": "I need a space at {bookTime} for a duration of {bookDuration}."
      },
      {
        "input": "Can I reserve a space?"
      },
      {
        "input": "Can I reserve a space for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a space for {bookPeople} people on {bookDate}?"
      },
      {
        "input": "Can I reserve a space for {bookPeople} people from {bookTime}?"
      },
      {
        "input": "Can I reserve a space for {bookPeople} people for a duration of {bookDuration}?"
      },
      {
        "input": "Can I reserve a space on {bookDate}?"
      },
      {
        "input": "Can I reserve a space on {bookDate} for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a space on {bookDate} from {bookTime}?"
      },
      {
        "input": "Can I reserve a space on {bookDate} for a duration of {bookDuration}?"
      },
      {
        "input": "Can I reserve a space from {bookTime}?"
      },
      {
        "input": "Can I reserve a space from {bookTime} for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a space from {bookTime} on {bookDate}?"
      },
      {
        "input": "Can I reserve a space from {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "Can I reserve a space for a duration of {bookDuration}?"
      },
      {
        "input": "Can I reserve a space for a duration of {bookDuration} for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a space for a duration of {bookDuration} on {bookDate}?"
      },
      {
        "input": "Can I reserve a space for a duration of {bookDuration} from {bookTime}?"
      },
      {
        "input": "Can I reserve a space at {bookTime}?"
      },
      {
        "input": "Can I reserve a space at {bookTime} for {bookPeople} people?"
      },
      {
        "input": "Can I reserve a space at {bookTime} on {bookDate}?"
      },
      {
        "input": "Can I reserve a space at {bookTime} from {bookTime}?"
      },
      {
        "input": "Can I reserve a space at {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a space?"
      },
      {
        "input": "May I reserve a space for {bookPeople} people?"
      },
      {
        "input": "May I reserve a space for {bookPeople} people on {bookDate}?"
      },
      {
        "input": "May I reserve a space for {bookPeople} people from {bookTime}?"
      },
      {
        "input": "May I reserve a space for {bookPeople} people for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a space on {bookDate}?"
      },
      {
        "input": "May I reserve a space on {bookDate} for {bookPeople} people?"
      },
      {
        "input": "May I reserve a space on {bookDate} from {bookTime}?"
      },
      {
        "input": "May I reserve a space on {bookDate} for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a space from {bookTime}?"
      },
      {
        "input": "May I reserve a space from {bookTime} for {bookPeople} people?"
      },
      {
        "input": "May I reserve a space from {bookTime} on {bookDate}?"
      },
      {
        "input": "May I reserve a space from {bookTime} for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a space for a duration of {bookDuration}?"
      },
      {
        "input": "May I reserve a space for a duration of {bookDuration} for {bookPeople} people?"
      },
      {
        "input": "May I reserve a space for a duration of {bookDuration} on {bookDate}?"
      },
      {
        "input": "May I reserve a space for a duration of {bookDuration} from {bookTime}?"
      },
      {
        "input": "May I reserve a space at {bookTime}?"
      },
      {
        "input": "May I reserve a space at {bookTime} for {bookPeople} people?"
      },
      {
        "input": "May I reserve a space at {bookTime} on {bookDate}?"
      },
      {
        "input": "May I reserve a space at {bookTime} from {bookTime}?"
      },
      {
        "input": "May I reserve a space at {bookTime} for a duration of {bookDuration}?"
      }
    ],
    "variables": [
      {
        "name": "bookDate",
        "type": "DAT",
        "prompts": [
          "On what date?",
          "On what date do you need the room?",
          "On what date do you want to book the room?"
        ]
      },
      {
        "name": "bookTime",
        "type": "TIM",
        "prompts": [
          "From what time?",
          "From what time do you need the room?",
          "From what time do you want to book the room?"
        ]
      },
      {
        "name": "bookDuration",
        "type": "DUR",
        "prompts": [
          "For how long?",
          "For how long do you need the room?",
          "For how long do you want to book the room?"
        ]
      },
      {
        "name": "bookPeople",
        "type": "NUM",
        "prompts": [
          "How many people?",
          "How many people will be using the room?",
          "How many people must the room accomodate?"
        ]
      },
      {
        "name": "bookConfirmation",
        "type": "CNF",
        "prompts": [
          "Do I understand correctly that you want a room on {bookDate}, for {bookPeople} people, from {bookTime}, for {bookDuration}?",
          "Do I understand correctly that you want a room on {bookDate}, for {bookPeople} people, from {bookTime}, for a duration of {bookDuration}?",
          "You want a room on {bookDate}, for {bookPeople} people, from {bookTime}, for a duration of {bookDuration}. Is that correct?"
        ]
      },
      {
        "name": "nextDialog",
        "type": "ABC",
        "prompts": [
          "Is there anything else I can do for you?",
          "Is there anything else I can help you with?"
        ]
      }
    ]
  },
  {
    "language": "ENG",
    "masterContext": "Room",
    "context": "QuestionAndAnswer",
    "handler": "",
    "examples": [
      {
        "input": "What does it cost to book a room?",
        "output": "There are no costs attached to booking a room."
      },
      {
        "input": "Are there any costs attached to booking van een room?",
        "output": "There are no costs attached to booking a room."
      },
      {
        "input": "What does it cost to booking a space?",
        "output": "There are no costs attached to booking a room."
      },
      {
        "input": "Are there any costs attached to booking van een space?",
        "output": "There are no costs attached to booking a room."
      },
      {
        "input": "What does it cost to reserve a room?",
        "output": "There are no costs attached to booking a room."
      },
      {
        "input": "Are there any costs attached to reserving van een room?",
        "output": "There are no costs attached to booking a room."
      },
      {
        "input": "What does it cost to reserving a space?",
        "output": "There are no costs attached to booking a room."
      },
      {
        "input": "Are there any costs attached to reserving van een space?",
        "output": "There are no costs attached to booking a room."
      }
    ]
  }
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
Initializing Translator took 1857 ms
Initializing Classifier took 5061 ms
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

Initializing LanguagePreprocessor took 82 ms
Initializing LanguageContextNLDRoom took 414 ms
Initializing LanguageContextENGRoom took 429 ms
Initializing LanguageContextENGGeneric took 828 ms
Initializing LanguageContextNLDGeneric took 849 ms
Initializing LanguageMasterContextENG took 1001 ms
Initializing LanguageMasterContextNLD took 1015 ms
Initializing EntityValueTranslator took 1817 ms
Initializing LanguageClassifier took 30124 ms
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
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'what is name?' -> ENG
Classifying the input sequence took: 0 ms

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
Classifying the input sequence took: 1 ms
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
20:45:07:194: Classify language for sequence: Hallo.
20:45:07:194: Classified language: NLD
20:45:07:194: Translate sequence: Hallo.
20:45:07:194: Translated sequence: UNI_ABC:Hallo .
20:45:07:194: Correction time limit: 200
20:45:07:195: Correcting sequence: Hallo.
20:45:07:195: Classify master context for sequence: Hallo.
20:45:07:195: Classified master context: Generic
20:45:07:195: Classify context for sequence: Hallo.
20:45:07:195: Classified context: Handshake
Interpreting the request took: 1 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
20:45:07:195: Classify language for sequence: Mijn naam si gekste der henkies.
20:45:07:195: Classified language: NLD
20:45:07:195: Translate sequence: Mijn naam si gekste der henkies.
20:45:07:198: Translated sequence: UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:si UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies .
20:45:07:198: Correction time limit: 700
20:45:07:198: Correcting sequence: Mijn naam si gekste der henkies.
20:45:07:708: Corrected sequence: Mijn naam is gekste der henkies.
20:45:07:709: Classify master context for sequence: Mijn naam is gekste der henkies.
20:45:07:709: Classified master context: Generic
20:45:07:709: Classify context for sequence: Mijn naam is gekste der henkies.
20:45:07:709: Classified context: Handshake
20:45:07:709: Translate corrected sequence: Mijn naam is gekste der henkies.
20:45:07:710: Translated corrected sequence: UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:is UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies .
Interpreting the request took: 515 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
20:45:07:710: Classify language for sequence: Albert einstein.
20:45:07:710: Classify language for sequence: What is your name? Albert einstein.
20:45:07:710: Classified language: ENG
20:45:07:711: Translate sequence: What is your name? Albert einstein.
20:45:07:713: Translated sequence: UNI_ABC:Albert|ENG_NAM:firstName:UNI_ABC:Albert UNI_ABC:einstein|ENG_NAM:lastName:UNI_ABC:Einstein .
20:45:07:713: Correction time limit: 300
20:45:07:713: Correcting sequence: Albert einstein.
20:45:07:892: Classify master context for sequence: Albert einstein.
20:45:07:893: Classify master context for sequence: What is your name? Albert einstein.
20:45:07:893: Classified master context: Generic
20:45:07:893: Classify context for sequence: Albert einstein.
20:45:07:893: Classify context for sequence: What is your name? Albert einstein.
20:45:07:893: Classified context: Handshake
Interpreting the request took: 183 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
20:45:07:893: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
20:45:07:894: Selected primary language: ENG
20:45:07:894: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
20:45:07:895: Translated sequence: UNI_ABC:Wruio UNI_ABC:wwtiop UNI_ABC:wtwrpoi UNI_ABC:weptiwpipw UNI_ABC:ipwopkm UNI_ABC:eopipwqwrqqiop UNI_ABC:qwerqwer UNI_ABC:qrqpoqe UNI_ABC:qpxnxc UNI_ABC:qwpgsjkdbvhsdfkljjv .
20:45:07:895: Correction time limit: 1100
20:45:07:895: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
20:45:08:995: Classify master context for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
Interpreting the request took: 1102 ms
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

Initializing DialogSet took 338 ms
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
20:45:11:214: Classify language for sequence: Hallo.
20:45:11:214: Classified language: NLD
20:45:11:214: Translate sequence: Hallo.
20:45:11:214: Translated sequence: UNI_ABC:Hallo .
20:45:11:215: Correction time limit: 200
20:45:11:215: Correcting sequence: Hallo.
20:45:11:215: Classify master context for sequence: Hallo.
20:45:11:215: Classified master context: Generic
20:45:11:215: Classify context for sequence: Hallo.
20:45:11:215: Classified context: Handshake
20:45:11:215: Handling dialog: NLD/Generic/Handshake
20:45:11:215:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:45:11:216:     Found matches for sequence: 2
20:45:11:216:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. Wat is jouw naam? (0.3365834004834811 / 1.0)
20:45:11:216:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. Wat is jouw naam? (0.3365834004834811 / 1.0)
20:45:11:216:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
20:45:11:216:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 2 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
20:45:11:216: Classify language for sequence: Wie ben jij?
20:45:11:216: Classified language: NLD
20:45:11:216: Translate sequence: Wie ben jij?
20:45:11:218: Translated sequence: UNI_ABC:Wie UNI_ABC:ben UNI_ABC:jij ?
20:45:11:218: Correction time limit: 400
20:45:11:218: Correcting sequence: Wie ben jij?
20:45:11:218: Classify master context for sequence: Wie ben jij?
20:45:11:218: Classified master context: Generic
20:45:11:218: Classify context for sequence: Wie ben jij?
20:45:11:218: Classified context: Handshake
20:45:11:218: Handling dialog: NLD/Generic/Handshake
20:45:11:218:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:45:11:219:     Found matches for sequence: 1
20:45:11:219:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. Wat is jouw naam? (1.5860864893902769 / 1.0)
20:45:11:219:     Set dialog output: Mijn naam is Dyz Lecticus.
20:45:11:219:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 3 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
20:45:11:219: Classify language for sequence: Albert einstein.
20:45:11:219: Classify language for sequence: What is your name? Albert einstein.
20:45:11:220: Classified language: ENG
20:45:11:225: Translate sequence: What is your name? Albert einstein.
20:45:11:227: Translated sequence: UNI_ABC:Albert|ENG_NAM:firstName:UNI_ABC:Albert UNI_ABC:einstein|ENG_NAM:lastName:UNI_ABC:Einstein .
20:45:11:227: Correction time limit: 300
20:45:11:227: Correcting sequence: Albert einstein.
20:45:11:442: Classify master context for sequence: Albert einstein.
20:45:11:442: Classify master context for sequence: What is your name? Albert einstein.
20:45:11:442: Classified master context: Generic
20:45:11:442: Classify context for sequence: Albert einstein.
20:45:11:442: Classify context for sequence: What is your name? Albert einstein.
20:45:11:442: Classified context: Handshake
20:45:11:442: Handling dialog: ENG/Generic/Handshake
20:45:11:442:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:45:11:443:     Updated variable firstName: UNI_ABC:Albert
20:45:11:443:     Updated variable lastName: UNI_ABC:Einstein
20:45:11:443:     Prompt variable: nextDialog
20:45:11:443:     Set dialog output: 
20:45:11:443:     Set dialog prompt: What can I do for you Albert Einstein?
20:45:11:443: Handling dialog: ENG/Generic/QuestionAndAnswer
20:45:11:443:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:45:11:443:     Failed to find matches for sequence: Albert einstein.
Handling the request took: 224 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
20:45:11:443: Classify language for sequence: Mijn naam si gekste der henkies.
20:45:11:443: Classified language: NLD
20:45:11:443: Translate sequence: Mijn naam si gekste der henkies.
20:45:11:444: Translated sequence: UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:si UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies .
20:45:11:444: Correction time limit: 700
20:45:11:444: Correcting sequence: Mijn naam si gekste der henkies.
20:45:11:897: Corrected sequence: Mijn naam is gekste der henkies.
20:45:11:897: Classify master context for sequence: Mijn naam is gekste der henkies.
20:45:11:897: Classified master context: Generic
20:45:11:897: Classify context for sequence: Mijn naam is gekste der henkies.
20:45:11:897: Classified context: Handshake
20:45:11:897: Translate corrected sequence: Mijn naam is gekste der henkies.
20:45:11:899: Translated corrected sequence: UNI_ABC:Mijn UNI_ABC:naam UNI_ABC:is UNI_ABC:gekste|NLD_NAM:firstName:UNI_ABC:Gekste NLD_PRE:6|UNI_ABC:der|NLD_NAM:preposition:NLD_PRE:6 UNI_ABC:henkies|NLD_NAM:lastName:UNI_ABC:Henkies .
20:45:11:899: Handling dialog: NLD/Generic/Handshake
20:45:11:899:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:45:11:899:     Updated variable firstName: UNI_ABC:Gekste
20:45:11:899:     Updated variable lastName: UNI_ABC:Henkies
20:45:11:899:     Updated variable preposition: NLD_PRE:6
20:45:11:899:     Prompt variable: nextDialog
20:45:11:899:     Set dialog output: 
20:45:11:899:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 456 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
20:45:11:899: Classify language for sequence: Who created you?
20:45:11:900: Classified language: ENG
20:45:11:900: Translate sequence: Who created you?
20:45:11:900: Translated sequence: UNI_ABC:Who UNI_ABC:created UNI_ABC:you ?
20:45:11:900: Correction time limit: 400
20:45:11:900: Correcting sequence: Who created you?
20:45:11:900: Classify master context for sequence: Who created you?
20:45:11:900: Classified master context: Generic
20:45:11:900: Classify context for sequence: Who created you?
20:45:11:900: Classified context: QuestionAndAnswer
20:45:11:900: Handling dialog: ENG/Generic/QuestionAndAnswer
20:45:11:900:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:45:11:901:     Found matches for sequence: 1
20:45:11:901:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.31881153305203935 / 1.0)
20:45:11:901:     Set dialog output: My software was written by André van der Zee.
Handling the request took: 2 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
20:45:11:901: Classify language for sequence: You asshole!
20:45:11:901: Classified language: ENG
20:45:11:901: Translate sequence: You asshole!
20:45:11:901: Translated sequence: UNI_ABC:You ENG_PRF:1|UNI_ABC:asshole !
20:45:11:901: Correction time limit: 300
20:45:11:901: Correcting sequence: You asshole!
20:45:11:901: Classify master context for sequence: You asshole!
20:45:11:902: Classified master context: Generic
20:45:11:902: Classify context for sequence: You asshole!
20:45:11:902: Classified context: Profanity
20:45:11:902: Handling dialog: ENG/Generic/Profanity
20:45:11:902:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:45:11:902:     Found matches for sequence: 2
20:45:11:902:     - Asshole! [OUTPUT] I do not appreciate that kind of language. (0.3334304584304584 / 1.0)
20:45:11:902:     - Asshole! [OUTPUT] I will pretend I did not read that. (0.3334304584304584 / 1.0)
20:45:11:902:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 1 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
20:45:11:902: Classify language for sequence: Jij klootzak!
20:45:11:902: Classified language: NLD
20:45:11:902: Translate sequence: Jij klootzak!
20:45:11:903: Translated sequence: UNI_ABC:Jij NLD_PRF:2|UNI_ABC:klootzak !
20:45:11:903: Correction time limit: 300
20:45:11:903: Correcting sequence: Jij klootzak!
20:45:11:903: Classify master context for sequence: Jij klootzak!
20:45:11:903: Classified master context: Generic
20:45:11:903: Classify context for sequence: Jij klootzak!
20:45:11:903: Classified context: Profanity
20:45:11:903: Handling dialog: NLD/Generic/Profanity
20:45:11:903:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:45:11:903:     Found matches for sequence: 2
20:45:11:903:     - Klootzak! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.35121381886087766 / 1.0)
20:45:11:903:     - Klootzak! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.35121381886087766 / 1.0)
20:45:11:903:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 93 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
20:45:11:995: Classify language for sequence: Can I book a room for 5 people?
20:45:11:995: Classified language: ENG
20:45:11:995: Translate sequence: Can I book a room for 5 people?
20:45:11:996: Translated sequence: UNI_ABC:Can UNI_ABC:I UNI_ABC:book UNI_ABC:a|ENG_NAM:firstName:UNI_ABC:A UNI_ABC:room|ENG_NAM:lastName:UNI_ABC:Room UNI_ABC:for UNI_NUM:5 UNI_ABC:people ?
20:45:11:996: Correction time limit: 900
20:45:11:996: Correcting sequence: Can I book a room for 5 people?
20:45:11:996: Classify master context for sequence: Can I book a room for 5 people?
20:45:11:996: Classified master context: Room
20:45:11:996: Classify context for sequence: Can I book a room for 5 people?
20:45:11:996: Classified context: Booking
20:45:11:997: Handling dialog: ENG/Room/Booking
20:45:11:997:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.RoomBookingHandler
20:45:11:997:     Updated variable bookPeople: UNI_NUM:5
20:45:11:997:     Prompt variable: bookDate
20:45:11:997:     Set dialog output: 
20:45:11:997:     Set dialog prompt: On what date?
Handling the request took: 2 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
20:45:11:997: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
20:45:11:998: Classified language: ENG
20:45:11:998: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
20:45:12:000: Translated sequence: UNI_ABC:Can UNI_ABC:I UNI_ABC:book UNI_ABC:a|ENG_NAM:firstName:UNI_ABC:A UNI_ABC:room|ENG_NAM:lastName:UNI_ABC:Room UNI_ABC:for UNI_NUM:12 UNI_ABC:people UNI_ABC:on ENG_DAT:2018-10-01 UNI_ABC:at ENG_TIM:12:00:00 UNI_ABC:for ENG_DUR:02:00 ?
20:45:12:001: Correction time limit: 1332
20:45:12:001: Correcting sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
20:45:12:001: Classify master context for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
20:45:12:001: Classified master context: Room
20:45:12:001: Classify context for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
20:45:12:002: Classified context: Booking
20:45:12:002: Handling dialog: ENG/Room/Booking
20:45:12:002:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.RoomBookingHandler
20:45:12:002:     Updated variable bookDate: ENG_DAT:2018-10-01
20:45:12:002:     Updated variable bookTime: ENG_TIM:12:00:00
20:45:12:002:     Updated variable bookDuration: ENG_DUR:02:00
20:45:12:002:     Updated variable bookPeople: UNI_NUM:12
20:45:12:002:     Prompt variable: bookConfirmation
20:45:12:003:     Set dialog output: 
20:45:12:003:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 6 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
20:45:12:003: Classify language for sequence: What does it cost to book a room?
20:45:12:003: Classified language: ENG
20:45:12:003: Translate sequence: What does it cost to book a room?
20:45:12:005: Translated sequence: UNI_ABC:What UNI_ABC:does UNI_ABC:it UNI_ABC:cost UNI_ABC:to UNI_ABC:book UNI_ABC:a UNI_ABC:room ?
20:45:12:005: Correction time limit: 900
20:45:12:005: Correcting sequence: What does it cost to book a room?
20:45:12:005: Classify master context for sequence: What does it cost to book a room?
20:45:12:005: Classified master context: Room
20:45:12:005: Classify context for sequence: What does it cost to book a room?
20:45:12:005: Classified context: QuestionAndAnswer
20:45:12:005: Handling dialog: ENG/Room/QuestionAndAnswer
20:45:12:005:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:45:12:006:     Found matches for sequence: 1
20:45:12:006:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (5.3062499999999995 / 1.0)
20:45:12:006:     Set dialog output: There are no costs attached to booking a room.
Handling the request took: 93 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
20:45:12:096: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
20:45:12:096: Classified language: ENG
20:45:12:096: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
20:45:12:100: Translated sequence: UNI_ABC:How UNI_ABC:much UNI_ABC:is ENG_NUM:10|UNI_ABC:ten ENG_MTH:M|UNI_ABC:times ENG_NUM:40|UNI_ABC:fourty ENG_MTH:D ENG_NUM:20|UNI_ABC:twenty ENG_MTH:A|UNI_ABC:plus ENG_NUM:3|UNI_ABC:three|ENG_NAM:firstName:UNI_ABC:Three ENG_MTH:M|UNI_ABC:times|ENG_NAM:lastName:UNI_ABC:Times ENG_NUM:6|UNI_ABC:six ?
20:45:12:100: Correction time limit: 1332
20:45:12:100: Correcting sequence: How much is ten times fourty divided by twenty plus three times six?
20:45:12:100: Classify master context for sequence: How much is ten times fourty divided by twenty plus three times six?
20:45:12:100: Classified master context: Generic
20:45:12:100: Classify context for sequence: How much is ten times fourty divided by twenty plus three times six?
20:45:12:101: Classified context: Math
20:45:12:101: Handling dialog: ENG/Generic/Math
20:45:12:101:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
20:45:12:101:     Updated variable number1: ENG_NUM:10
20:45:12:101:     Updated variable number2: ENG_NUM:40
20:45:12:101:     Updated variable number3: ENG_NUM:20
20:45:12:101:     Updated variable number4: ENG_NUM:3
20:45:12:101:     Updated variable number5: ENG_NUM:6
20:45:12:101:     Updated variable operator1: ENG_MTH:M
20:45:12:101:     Updated variable operator2: ENG_MTH:D
20:45:12:101:     Updated variable operator3: ENG_MTH:A
20:45:12:101:     Updated variable operator4: ENG_MTH:M
20:45:12:102:     Prompt variable: nextDialog
20:45:12:102:     Set dialog output: Exactly thirtyeight.
20:45:12:102:     Set dialog prompt: What else can I do for you?
Handling the request took: 6 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
20:45:12:103: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
20:45:12:103: Classified language: NLD
20:45:12:103: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
20:45:12:106: Translated sequence: UNI_ABC:Hoeveel UNI_ABC:is NLD_NUM:10|UNI_ABC:tien NLD_MTH:M|UNI_ABC:keer NLD_NUM:40|UNI_ABC:veertig NLD_MTH:D NLD_NUM:20|UNI_ABC:twintig NLD_MTH:A|UNI_ABC:plus NLD_NUM:3|UNI_ABC:drie|NLD_NAM:firstName:UNI_ABC:Drie NLD_MTH:M|UNI_ABC:keer|NLD_NAM:lastName:UNI_ABC:Keer NLD_NUM:6|UNI_ABC:zes ?
20:45:12:106: Correction time limit: 1300
20:45:12:106: Correcting sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
20:45:12:106: Classify master context for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
20:45:12:106: Classified master context: Generic
20:45:12:106: Classify context for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
20:45:12:106: Classified context: Math
20:45:12:106: Handling dialog: NLD/Generic/Math
20:45:12:106:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
20:45:12:107:     Updated variable number1: NLD_NUM:10
20:45:12:107:     Updated variable number2: NLD_NUM:40
20:45:12:107:     Updated variable number3: NLD_NUM:20
20:45:12:107:     Updated variable number4: NLD_NUM:3
20:45:12:107:     Updated variable number5: NLD_NUM:6
20:45:12:107:     Updated variable operator1: NLD_MTH:M
20:45:12:107:     Updated variable operator2: NLD_MTH:D
20:45:12:107:     Updated variable operator3: NLD_MTH:A
20:45:12:107:     Updated variable operator4: NLD_MTH:M
20:45:12:107:     Prompt variable: nextDialog
20:45:12:107:     Set dialog output: Precies achtendertig.
20:45:12:107:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 5 ms
~~~~

Test results
------------
All 19 tests have been executed successfully (222 assertions).  
Total test duration: 63352 ms (total sleep duration: 28000 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 609 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 424 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 456 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 462 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 471 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 118976 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 118987 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 118986 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 119125 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 119027 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 119057 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 119036 Kb / 116 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 550265 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 550266 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 550267 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 550268 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 550274 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 553365 Kb / 540 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 553375 Kb / 540 Mb
